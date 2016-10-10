
import java.util.*;

public class Astar {
	ArrayList<Integer> Estimate = new ArrayList<Integer>();  
    private int[] shortestpath;
    private Floyd floyd;
    private Map map;
    Astar (Floyd shortpath, Map map){
    	this.floyd=shortpath;
        this.map=map;
    }
//    public void doastar(int [][]edge , int  start ,int end){
//        PriorityQueue<Integer> openlist=new PriorityQueue<Integer>();
//        PriorityQueue<Integer> closelist=new PriorityQueue<Integer>();
//        openlist.add(start);
//        int []g=new int[map.nodetotal];
//        int []h=new int[map.nodetotal];
//        int []f=new int[map.nodetotal];
//        int []parent=new int[map.nodetotal];
//        for (int i = 0; i < map.nodetotal; i++) {
//            g[i]=Integer.MAX_VALUE;
//            h[i]=Integer.MAX_VALUE;
//            f[i]=Integer.MAX_VALUE;
//            parent[i]=i;
//        }
//        g[start]=0;
//        f[start]=0;
//        while (openlist.size()>0){
//            int currentnode=openlist.poll();
//            if(end==currentnode)
//                break;
//            int []succ=getSucc(currentnode);
//            for (int i = 0; i < succ.length; i++) {
//                boolean inopen;
//                if(closelist.contains(succ[i]))
//                    continue;
//                inopen=openlist.contains(currentnode);
//                int tempg;
//                if(g[currentnode]==Integer.MAX_VALUE)
//                    tempg=Integer.MAX_VALUE;
//                tempg=g[currentnode]+edge[currentnode][succ[i]];
//                if(inopen && tempg>g[succ[i]])
//                    continue;
//                parent[succ[i]]=currentnode;
//                if(inopen){
//                    g[succ[i]]=tempg;
//                }
//                else {
//                    g[succ[i]]=tempg;
//                    openlist.add(succ[i]);
//                }
//            }
//            closelist.add(currentnode);
//        }
//    }


    private int[] getSucc(int u){
        ArrayList<Integer> t=new ArrayList<Integer>();
        int id=map.graph.getFirstNeighbor(u);
        t.add(id);
        while (id!=-1){
            id=map.graph.getNextNeighbor(u,id);
            if(id!=-1){
                t.add(id);
            }
        }
        int []s=new int[t.size()];
        for (int i = 0; i < s.length; i++) {
            s[i]=t.get(i);
        }
        return s;
    }

	public void doastar(int[][] edge, int start, int end){
    	/**********************************************************
    	 *  Definition & Initiation
    	 */
    	int vertexnumber = edge.length;
        shortestpath=new int[vertexnumber];
        shortestpath[start]=0;
        
        int[] f=new int [vertexnumber];
        int[] g=new int [vertexnumber];
        int[] h=new int [vertexnumber];
        for(int i=0;i<vertexnumber;i++){
        	f[i]=Integer.MAX_VALUE;
        	g[i]=Integer.MAX_VALUE;
        	h[i]=0;
        }
        f[start]=0;
        g[start]=0;
        int gpath = 0;// for function  g[]
        int newf = 0;
        
        int[] visited = new int[vertexnumber];//Not in both open or close list
        for(int i=0;i<vertexnumber;i++){
        	visited[i]=-1;//Not in both open or close list
        }
        visited[start] = 1;// In open list
        int opennum=1;// Number of noses in Open list
        int closenum=0;// Number of noses in Close list (Why not used?)
        
        int [][] edgetmp=new int[vertexnumber][vertexnumber];
        
        for (int i = 0; i < vertexnumber; i++) {
            for (int j = 0; j < vertexnumber; j++) {
                edgetmp[i][j]=edge[i][j];
            }
        }
    	/**
    	 *  Definition & Initiation
    	 ***************************************************************************/

    	/***************************************************************************
         * Main Process
         ***************************************************************************/
        // While open list has some nodes in it
      
       while(opennum!=0 && visited[end]!=0 ){							    
    	   	int tempmin=0,currentnode=0;
            //get the node with min-f[i] in the Openlist
    	   	for(int i=0;i<opennum;i++){
    	   		if(tempmin>f[i]){
    			   tempmin=f[i];
    			   currentnode=i;
    		   }
    	   	}
            //remove from open list and add to close list
		    opennum--;
		    closenum++;
		    visited[currentnode] = 0;
            //for adjacent nodes
	    	  
		   for(int i=0;i<vertexnumber;i++){

			   if(visited[i]!=0 && edgetmp[currentnode][i]<Integer.MAX_VALUE ){
				   if(visited[i]==-1){					// if the node is not in Open/Close_list
					   visited[i]=1;					// Put the node in open_list
					   opennum++;

                       if(gpath==Integer.MAX_VALUE)
                           g[i]=Integer.MAX_VALUE;
                       else
                           g[i]  = edge[currentnode][i] + gpath;	       		            // Calculate its g[] value
                       gpath = g[i];
	    					   
					   h[i]=floyd.getpathlength(i,end);					// Calculate its h[] value
                       if(g[i]==Integer.MAX_VALUE||h[i]==Integer.MAX_VALUE)
                           f[i]=Integer.MAX_VALUE;
                       else
                           f[i]=g[i]+h[i];

				   }//end if(visited[i]==-1)
	    	   				
				   if(visited[i]==1){					//if the node is in Open_list, begin to compare
					   g[i]  = edge[currentnode][i] + gpath;	       		            // Calculate its g[] value
					   h[i]=floyd.getpathlength(i,end);					// Calculate its h[] value
                       if(h[i]==Integer.MAX_VALUE||g[i]==Integer.MAX_VALUE)
                           newf=Integer.MAX_VALUE;
                       else
                           newf=g[i]+h[i];
                       newf=g[i]+h[i];
					   if(f[i]>newf){
						   f[i]=newf;
					   }
				   }//end if(visited[i]==1)

			   }
		   }//end for

       } // end while
       
       for (int i = 0; i < closenum; i++) {
    	   Estimate.add(f[i]);
       }
    }//end func.doastar
    
    public int[] getpath(int start, int end){
    	int[] result = new int [Estimate.size()];
    	for(int i=0;i<Estimate.size();i++){
    		result[i]=Estimate.get(i);
    	}
    	
    	Arrays.sort(result);   
    	return result;
    }
    
    
    public int getpathlength(int end){
        return shortestpath[end];
    }
}

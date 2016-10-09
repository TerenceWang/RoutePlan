
import java.util.*;

public class Astar {
	ArrayList<Integer> Estimate = new ArrayList<Integer>();  
    private int[] shortestpath;
    private Floyd floyd;
    
    Astar (Floyd shortpath){
    	this.floyd=shortpath;
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
         */                   // While open list has some nodes in it 
      
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

					   g[i]  = edge[currentnode][i] + gpath;	       		            // Calculate its g[] value
					   gpath = g[i];
	    					   
					   h[i]=floyd.getpathlength(i,end);					// Calculate its h[] value
					   f[i]=g[i]+h[i];
				   }//end if(visited[i]==-1)
	    	   				
				   if(visited[i]==1){					//if the node is in Open_list, begin to compare
					   g[i]  = edge[currentnode][i] + gpath;	       		            // Calculate its g[] value
					   h[i]=floyd.getpathlength(i,end);					// Calculate its h[] value
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

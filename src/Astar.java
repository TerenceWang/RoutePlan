
import java.util.*;

import javax.management.OperationsException;

import org.omg.CORBA.Current;

public class Astar {
	ArrayList<Integer> Estimate = new ArrayList<Integer>();  
    private int[] shortestpath;
    private Floyd floyd;

    private int pathlength;
    Astar (Floyd shortpath){
    	this.floyd=shortpath;
        pathlength=0;
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
        int newf = 0;
        
        ArrayList<Integer> openSet = new ArrayList<Integer>();

        ArrayList<Integer> closedSet = new ArrayList<Integer>();
        int[] gValue = new int[vertexnumber];
        openSet.add(start);
        
        
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
       while(openSet.size()!=0){		
    	   

	       int currentnode = getMinFvalue(openSet, f);
	      
	       openSet.remove(openSet.indexOf(currentnode));
	       closedSet.add(currentnode);
    	   	  

   //for adjacent nodes
		   ArrayList<Integer> adjacentNodes = getAdjacentNodes(currentnode, vertexnumber, edgetmp);
		   for(int adj : adjacentNodes){
			   if(closedSet.contains(adj))
				   continue;
			   if(!openSet.contains(adj)){
				 
				   if(currentnode == start){
					   g[adj]  = edge[start][adj];
					   gValue[adj] = g[adj];
				   }else{
					   g[adj]= gValue[adj]+ edge[currentnode][adj];
					   gValue[adj] = g[adj];
				   }



                   int []s=floyd.getpath(adj,end);
                   int result=0;
                   for (int i = 0; i < s.length-1; i++) {
                        result+=edge[s[i]][s[i+1]];
                   }
//                   h[adj]=floyd.getpathlength(adj,end);
                   h[adj]=result;


                   f[adj]=h[adj]+g[adj];
                   openSet.add(adj); // add node to openList
			   }else{
				   g[adj]= gValue[adj]+ edge[currentnode][adj];     		            // Calculate its g[] value

                   int []s=floyd.getpath(adj,end);
                   int result=0;
                   for (int i = 0; i < s.length-1; i++) {
                       result+=edge[s[i]][s[i+1]];
                   }
                   h[adj]=result;
//                   h[adj]=floyd.getpathlength(adj,end);					// Calculate its h[] value

                   newf=g[adj]+h[adj];
				   if(f[adj]>newf){
					   f[adj]=newf;
				   }
			   }
		   }
		   
	    	  
       } // end while
       
       for (int node : closedSet) {
    	   Estimate.add(node);
    	   if(node==end)
    		   break;
       }
        for (int i = 0; i < Estimate.size()-1; i++) {
            pathlength+=edge[Estimate.get(i)][Estimate.get(i+1)];
        }

    }//end func.doastar
    
    public int[] getpath(int start, int end){
    	int[] result = new int [Estimate.size()];
    	for(int i=0;i<Estimate.size();i++){
    		result[i]=Estimate.get(i);
    	}
    	
    	return result;
    }

    
    public int getpathlength(int end){
        return pathlength;
    }
    public int getMinFvalue(ArrayList<Integer> openSet, int[] f){
	   	int tempmin=Integer.MAX_VALUE,currentnode=0;
	   	for(int i : openSet){
	   		if(tempmin>=f[i]){
			   tempmin=f[i];
			   currentnode=i;
			   
			  
		   }
	   	}
    	return currentnode;
    }
    public ArrayList<Integer> getAdjacentNodes(int currentnode, int vertexnumber, int[][] edgetmp){
    	ArrayList<Integer> adjs = new ArrayList<Integer>();
    	for(int i=0;i<vertexnumber;i++){
    		if(edgetmp[currentnode][i]<Integer.MAX_VALUE){
    		    adjs.add(i);
    		}

    	}
		return adjs;
    	
    }
}

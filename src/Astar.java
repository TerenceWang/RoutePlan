
import java.util.*;

import javax.management.OperationsException;

import org.omg.CORBA.Current;
 /**
  *  Astar is the class which realized the process of A* algorithm.
  */
public class Astar {
	ArrayList<Integer> Estimate = new ArrayList<Integer>();  
    private int[] shortestpath;
    private Floyd floyd;

    private int pathlength;
    Astar (Floyd shortpath){
    	this.floyd=shortpath;
        pathlength=0;
    }
    /**
     * The main function of Astar in which we achieve every step of
     * A* algorithm
     * @param edge[][] the weight of roads between two adjacent nodes
     * @param start The start point of route planning
     * @param end  The Objective point of route planning
     */
	public void doastar(int[][] edge, int start, int end){


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

    	/**
    	 * The main loop of processing operation the Openlist and CloseList
         */
       while(openSet.size()!=0){		
    	   int currentnode = getMinFvalue(openSet, f);
	      
	       openSet.remove(openSet.indexOf(currentnode));
	       closedSet.add(currentnode);

		   ArrayList<Integer> adjacentNodes = getAdjacentNodes(currentnode,edge);
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
                   h[adj]=result;


                   f[adj]=h[adj]+g[adj];
                   openSet.add(adj);
			   }else{
				   g[adj]= gValue[adj]+ edge[currentnode][adj];     		            // Calculate its g[] value

                   int []s=floyd.getpath(adj,end);
                   int result=0;
                   for (int i = 0; i < s.length-1; i++) {
                       result+=edge[s[i]][s[i+1]];
                   }
                   h[adj]=result;

                   newf=g[adj]+h[adj];
				   if(f[adj]>newf){
					   f[adj]=newf;
				   }
			   }
		   }
		   
	    	  
       }
       
       for (int node : closedSet) {
    	   Estimate.add(node);
    	   if(node==end)
    		   break;
       }
        for (int i = 0; i < Estimate.size()-1; i++) {
            pathlength+=edge[Estimate.get(i)][Estimate.get(i+1)];
        }

    }
    /**
     * Get an array of the compute result of the vehicle's route
     * @param start The start point of route planning
     * @param end  The Objective point of route planning
     * @return the vehicle's route
     */
    public int[] getpath(int start, int end){
    	int[] result = new int [Estimate.size()];
    	for(int i=0;i<Estimate.size();i++){
    		result[i]=Estimate.get(i);
    	}
    	
    	return result;
    }

    /**
     * Get the length of the vehicle's route
     * @return Get the length of the vehicle's route
     */
    public int getpathlength(int end){
        return pathlength;
    }

    /**
     * Get the the node with minimum f value in OpenList
     * @param openSet the OpenList
     * @param f the f value of each node
     * @return the node with the minimum f value in OpenList
     */
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
    /**
     * Find the adjacent nodes of current node, if the node exist ,add it in the
     * ArrayList and return all the existing adjacent nodes at last.
     * @param currentnode The current node
     * @param edgetmp The temp value of the weight between every two nodes
     * @return all the existing adjacent nodes at last.
     */
    public ArrayList<Integer> getAdjacentNodes(int currentnode, int[][] edgetmp) {

        int vertexnumbber = edgetmp.length;
        int vertexperline = (int) Math.sqrt(vertexnumbber);
        int row = currentnode / vertexperline;
        int col = currentnode % vertexperline;
        ArrayList<Integer> t = new ArrayList<Integer>();
        int cola = col - 1;
        int colb = col + 1;
        int rowa = row - 1;
        int rowb = row + 1;
        if (cola > -1 && edgetmp[currentnode][cola + row * vertexperline] != Integer.MAX_VALUE) {
            t.add(cola + row * vertexperline);
        }
        if (colb < vertexperline && edgetmp[currentnode][colb + row * vertexperline] != Integer.MAX_VALUE) {
            t.add(colb + row * vertexperline);
        }
        if (rowa > -1 && edgetmp[currentnode][rowa * vertexperline + col] != Integer.MAX_VALUE) {
            t.add(rowa * vertexperline + col);
        }
        if (rowb < vertexperline && edgetmp[currentnode][rowb * vertexperline + col] != Integer.MAX_VALUE) {
            t.add(rowb * vertexperline + col);
        }
        return t;
    }
}

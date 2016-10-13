import java.util.ArrayList;

/**
 * Created by terence on 10/1/16.
 * Structure of the traffic network
 */
public class AMWGraph {
    private ArrayList vertexList;
    private int[][] edges;
    private int numOfEdges;

  /**
   * Initial the whole network
   */
    public AMWGraph(int n) {
        edges=new int[n][n];
        vertexList=new ArrayList(n);
        numOfEdges=0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                edges[i][j]=Integer.MAX_VALUE;
            }

        }
    }
    /**
     * return the number of nodes
     * @return the number of nodes
     */
    public int getNumOfVertex() {
        return vertexList.size();
    }
    /**
     * return the number of roads
     * @return the number of roads
     */
    public int getNumOfEdges() {
        return numOfEdges;
    }
    /**
     * return the NO.i node
     * @return the No.i node
     */
    public Object getValueByIndex(int i) {
        return vertexList.get(i);
    }
    /**
     * return the cost of a specified road
     * @return the cost of the road connected node v1 and node v2
     */
    public int getWeight(int v1,int v2) {
        return edges[v1][v2];
    }
    /**
     * Add a node into the network
     */
    public void insertVertex(Object vertex) {
        vertexList.add(vertexList.size(),vertex);
    }	
    /**
     *  Build an edge in the network between specified two nodes
     */
    public void insertEdge(int v1,int v2,int weight) {
        edges[v1][v2]=weight;
        numOfEdges++;
    }
    /**
     * Delete an edge in the network between specified two nodes
     */
    public void deleteEdge(int v1,int v2) {
        edges[v1][v2]=Integer.MAX_VALUE;
        numOfEdges--;
    }
    /**
     * Get the martix of edges
     * @return the martix of edges
     */
    public int[][] getEdgeMatrix(){
        int n=edges.length;
        int[][] result=new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j]=edges[i][j];
            }
        }
        return result;
    }
    /**
     * Get the adjacent node of current node
     * @param index current node
     * @return an adjacent node
     */
    public int getFirstNeighbor(int index) {
        for(int j=0;j<vertexList.size();j++) {
            if (edges[index][j]<Integer.MAX_VALUE) {
                return j;
            }
        }
        return -1;
    }
    /**
     * Get the adjacent node of the current node's first neighbor
     * @param v1 current node
     * @param v2 current node's first neighbor
     * @return an adjacent node of the current node's first neighbor
     */
    public int getNextNeighbor(int v1,int v2) {
        for (int j=v2+1;j<vertexList.size();j++) {
            if (edges[v1][j]<Integer.MAX_VALUE) {
                return j;
            }
        }
        return -1;
    }
}
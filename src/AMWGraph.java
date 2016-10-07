import java.util.ArrayList;

/**
 * Created by terence on 10/1/16.
 */
public class AMWGraph {
    private ArrayList vertexList;
    private int[][] edges;
    private int numOfEdges;


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

    public int getNumOfVertex() {
        return vertexList.size();
    }

    public int getNumOfEdges() {
        return numOfEdges;
    }

    public Object getValueByIndex(int i) {
        return vertexList.get(i);
    }

    public int getWeight(int v1,int v2) {
        return edges[v1][v2];
    }

    public void insertVertex(Object vertex) {
        vertexList.add(vertexList.size(),vertex);
    }

    public void insertEdge(int v1,int v2,int weight) {
        edges[v1][v2]=weight;
        numOfEdges++;
    }

    public void deleteEdge(int v1,int v2) {
        edges[v1][v2]=Integer.MAX_VALUE;
        numOfEdges--;
    }

    public int[][] getEdgeMatrix(){
        int[][] result=new int[numOfEdges][numOfEdges];
        for (int i = 0; i < numOfEdges; i++) {
            for (int j = 0; j < numOfEdges; j++) {
                result[i][j]=edges[i][j];
            }
        }
        return result;
    }

    public int getFirstNeighbor(int index) {
        for(int j=0;j<vertexList.size();j++) {
            if (edges[index][j]<Integer.MAX_VALUE) {
                return j;
            }
        }
        return -1;
    }

    public int getNextNeighbor(int v1,int v2) {
        for (int j=v2+1;j<vertexList.size();j++) {
            if (edges[v1][j]<Integer.MAX_VALUE) {
                return j;
            }
        }
        return -1;
    }
}
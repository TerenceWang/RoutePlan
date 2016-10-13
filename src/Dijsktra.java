/**
 * Created by terence on 10/1/16.
 */


/**
 * This is the algorithm for the Dijsktra
 */

public class Dijsktra {
    private String[] path;
    private int[] shortestpath;
    Dijsktra(){

    }

    /**
     * The main function of dijsktra
     * @param edge store the edge in a adjacency matrix
     * @param start
     * @return
     */
    public int dodijsktra(int[][] edge, int start){
        int vertexnumber=edge.length;
        shortestpath=new int[vertexnumber];
        path=new String[vertexnumber];
        for(int i=0;i<vertexnumber;i++)
            path[i]=new String(start+","+i);
        int [][] edgetmp=new int[vertexnumber][vertexnumber];
        for (int i = 0; i < vertexnumber; i++) {
            for (int j = 0; j < vertexnumber; j++) {
                edgetmp[i][j]=edge[i][j];
            }
        }

        int[] visited = new int[vertexnumber];
        shortestpath[start] = 0;
        visited[start] = 1;

        for (int count = 0; count < vertexnumber-1; count++) {
            int distance= Integer.MAX_VALUE;
            int k=-1;
            for(int i = 0;i < vertexnumber;i++){
                if(visited[i] == 0 && edgetmp[start][i] < distance){
                    distance = edgetmp[start][i];
                    k = i;
                }
            }
            if(k>-1) {
                shortestpath[k] = distance;
                visited[k] = 1;
            }else{
                return -1;
            }
            for(int i = 0;i < vertexnumber;i++){
                if(visited[i] == 0 && edgetmp[start][k] + edgetmp[k][i] < edgetmp[start][i] && edgetmp[k][i]<Integer.MAX_VALUE){
                    edgetmp[start][i] = edgetmp[start][k] + edgetmp[k][i];
                    path[i]=path[k]+","+i;
                }
            }
        }
        return 0;
    }

    /**
     * get the car's route
     * @param end the end point
     * @return return the car's route
     */
    public int[] getpath(int end ){
        String[] t=path[end].split(",");
        int [] result=new int [t.length];
        for (int i = 0; i < result.length; i++) {
            result[i]=Integer.parseInt(t[i]);
        }
        return result;
    }

    /**
     *
     * Get the length of the car's route
     * @param end the end point
     * @return Get the length of the car's route
     */
    public int getpathlength(int end){
        return shortestpath[end];
    }
}

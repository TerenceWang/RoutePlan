/**
 * Created by terence on 10/1/16.
 */
public class Dijsktra {
    private String[] path;
    private int[] shortestpath;
    Dijsktra(){

    }
    public void dodijsktra(int[][] edge, int start){
        int vertexnumber=edge.length;
        shortestpath=new int[vertexnumber];
        path=new String[vertexnumber];
        for(int i=0;i<vertexnumber;i++)
            path[i]=new String(start+","+i);
        int[] visited = new int[vertexnumber];

        shortestpath[start] = 0;
        visited[start] = 1;

        for (int count = 0; count < vertexnumber-1; count++) {
            int distance= Integer.MAX_VALUE;
            int k=-1;
            for(int i = 0;i < vertexnumber;i++){
                if(visited[i] == 0 && edge[start][i] < distance){
                    distance = edge[start][i];
                    k = i;
                }
            }
            shortestpath[k] = distance;

            visited[k] = 1;
            for(int i = 0;i < vertexnumber;i++){
                if(visited[i] == 0 && edge[start][k] + edge[k][i] < edge[start][i]&&edge[k][i]<Integer.MAX_VALUE
                        ){
                    edge[start][i] = edge[start][k] + edge[k][i];
                    path[i]=path[k]+","+i;
                }
            }
        }
    }


    public int[] getpath(int end ){
        String[] t=path[end].split(",");
        int [] result=new int [t.length];
        for (int i = 0; i < result.length; i++) {
            result[i]=Integer.parseInt(t[i]);
        }
        return result;
    }
    public int getpathlength(int end){
        return shortestpath[end];
    }
}

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
                if(visited[i] == 0 && edge[start][i] < distance && edge[start][i]>0){
                    distance = edge[start][i];
                    k = i;
                }
            }
            shortestpath[k] = distance;

            visited[k] = 1;
            for(int i = 0;i < vertexnumber;i++){
                if(visited[i]==0&&edge[start][i]==0&&edge[k][i]>0){
                    edge[start][i] = edge[start][k] + edge[k][i];
                    path[i]=path[k]+","+i;
                }
                if(visited[i] == 0 && edge[start][k] + edge[k][i] < edge[start][i] && edge[start][k]>0
                        && edge[k][i]>0){
                    edge[start][i] = edge[start][k] + edge[k][i];
                    path[i]=path[k]+","+i;
                }
            }
        }
    }


    public String getpath(int end ){
        return path[end];
    }
    public int getlength(int end){
        return shortestpath[end];
    }
}

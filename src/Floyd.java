import java.util.*;
/**
 * Created by abels on 2016/10/3.
 */
public class Floyd {
    private int[][] pre;
    private int[][] shortestpath;
    Floyd(){

    }

    public void dofloyd(int[][] edge){
        int vertexnumber=edge.length;

        shortestpath = new int[vertexnumber][vertexnumber];

        for(int i = 0; i < vertexnumber; ++i)
            for(int j = 0; j < vertexnumber; ++j) {
                shortestpath[i][j] = edge[i][j];

            }
        pre = new int[vertexnumber][vertexnumber];
        for (int i = 0 ; i < vertexnumber; ++i)
            for (int j = 0; j < vertexnumber; ++j)
                pre[i][j] = j;

        for (int k = 0; k < vertexnumber; ++k)
            for (int i = 0; i < vertexnumber; ++i)
                for (int j = 0; j < vertexnumber; ++j) {

                    if (shortestpath[i][j] > shortestpath[i][k] + shortestpath[k][j] && i != j
                            && shortestpath[i][k] < Integer.MAX_VALUE
                            && shortestpath[k][j] < Integer.MAX_VALUE) {
                        shortestpath[i][j] = shortestpath[i][k] + shortestpath[k][j];
                        pre[i][j] = pre[i][k];
                    }
                    if(i==j){
                        shortestpath[i][j]=0;
                    }
                }
    }


    public int[] getpath(int start, int end){

        List path = new ArrayList<Integer>();

        int temp = start;
        while (temp != -1 && temp != end) {
            path.add(temp);
            temp = pre[temp][end];
        }
        path.add(end);

        int[] pathReturn = new int[path.size()];
        for (int i = 0; i < path.size(); i++) {
            pathReturn[i]= (int) path.get(i);
        }

        return pathReturn;
    }

    public int getpathlength(int start, int end){
        return shortestpath[start][end];
    }
}

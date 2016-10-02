import java.util.ArrayList;

/**
 * Created by abels on 2016/10/3.
 */
public class Floyd {
    private int[][] pre;
    private int vertexnumber;
    private int[][] shortestpath = new int[vertexnumber][vertexnumber];
    Floyd(){

    }
    public void dofloyd(int[][] edge){
        for (int k = 0; k < vertexnumber; ++k)
            for (int i = 0; i < vertexnumber; ++i)
        for (int j = 0; j < vertexnumber; ++j)
            if (shortestpath[i][j] > shortestpath[i][k] + shortestpath[k][j]){
                shortestpath[i][j] = shortestpath[i][k] + shortestpath[k][j];
                pre[i][j] = k;
            }
    }


    public ArrayList<int> getpath(int start, int end){
        ArrayList path;
        path = new ArrayList<int>();
        path.add(pre[start][end]);
        int i = pre[start][end];
        while (pre[start][i] != start) {
            path.add(i);
            i = pre[start][i];
        }
        return path;
    }

    public int getpathlength(ArrayList<int> path){
        return path.size();
    }
}
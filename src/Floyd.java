import java.util.*;
/**
 * Created by abels on 2016/10/3.
 */
public class Floyd {
    private int[][] pre;
    private int[][] shortestpath;
    Floyd(){

    }

    public void dofloyd(int[][] edge, int vertexnumber){
        System.out.println(1);
        shortestpath = new int[vertexnumber][vertexnumber];
        for(int i = 0; i < vertexnumber; ++i)
            for(int j = 0; j < vertexnumber; ++j)
                shortestpath[i][j] = 1000;

        for(int i = 0; i < vertexnumber; ++i)
            for(int j = 0; j < vertexnumber; ++j)
                if (edge[i][j] != 0){
                    shortestpath[i][j] = edge[i][j];
                }

        pre = new int[vertexnumber][vertexnumber];
        for (int i = 0 ; i < vertexnumber; ++i)
            for (int j = 0; j < vertexnumber; ++j)
                pre[i][j] = -1;

        System.out.println(1);

        for (int k = 0; k < vertexnumber; ++k)
            for (int i = 0; i < vertexnumber; ++i)
                for (int j = 0; j < vertexnumber; ++j)
                    if (shortestpath[i][j] > shortestpath[i][k] + shortestpath[k][j] && i != j){
                        shortestpath[i][j] = shortestpath[i][k] + shortestpath[k][j];
                        pre[i][j] = k;
                    }
    }


    public int[] getpath(int start, int end){
        List path = new ArrayList<Integer>();
        path.add(end);
        int temp = pre[start][end];
        while (temp != -1) {
            path.add(temp);
            temp = pre[start][temp];
        }
        path.add(start);
        int[] pathReturn = new int[path.size()];
        for (int i = path.size() - 1; i >= 0; --i)
            pathReturn[i] = (int) path.get(path.size() - i - 1);

        return pathReturn;
    }

    public int getpathlength(int start, int end){
        return shortestpath[start][end];
    }
}

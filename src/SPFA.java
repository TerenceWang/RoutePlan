import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by terence on 10/1/16.
 */
public class SPFA {
    private int [] Path;
    private int[] shortestpath;

    SPFA(){

    }
    public void dospfa(int [][] edge, int start){
        int x;
        int vextextotal=edge.length;
        shortestpath=new int[vextextotal];
        Path=new int[vextextotal];
        Path[start]=start;
        Queue<Integer> q=new LinkedList<Integer>() ;
        boolean  visited[]=new boolean[vextextotal];
        for(int i=0;i<vextextotal;i++)
            shortestpath [i]=Integer.MAX_VALUE;
        shortestpath[start]=0;
        q.offer(start);
        visited[start]=true;
        while(!q.isEmpty()){

            x=q.poll();
            visited[x]=false;  // 置出队的点未标记
            for(int i=0;i<vextextotal;i++)
                if(shortestpath[x]+edge[x][i]<shortestpath[i]&&edge[x][i]>0)  //这里就是所谓的松弛操作了
                {
                    shortestpath[i]=edge[x][i]+shortestpath[x]; //更新路径
                    Path[i]=x;
                    if(!visited[i])  // 未被访问
                    {
                        q.offer(i);
                        visited[i]=true;
                    }
                }
        }

    }
    public int[] getpath(int end){
        int p=end;
        ArrayList<Integer> tmp=new ArrayList<Integer>();
        tmp.add(p);
        while (Path[p]!=p){
            tmp.add(Path[p]);
            p=Path[p];
        }
        int []result=new int[tmp.size()];
        for (int i = tmp.size()-1; i >-1 ; i--) {
            result[i]=tmp.get(tmp.size()-1-i);
        }
        return result;
    }
    public int getpathlength(int end){
        return shortestpath[end];
    }
}

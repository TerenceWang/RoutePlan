import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by terence on 10/1/16.
 * Process SPFA algorithm to get shortest route between start and end point
 */
public class SPFA {
    private int [] Path;
    private int[] shortestpath;

    SPFA(){

    }
    /**
     * The main function, realize the SPFA algorithm
     * @param edge   The weight of roads between two adjacent nodes 
     * @param start  The start point
     */
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
            visited[x]=false;
            for(int i=0;i<vextextotal;i++)
                if(shortestpath[x]+edge[x][i]<shortestpath[i]&&edge[x][i]<Integer.MAX_VALUE)
                {
                    shortestpath[i]=edge[x][i]+shortestpath[x];
                    Path[i]=x;
                    if(!visited[i])
                    {
                        q.offer(i);
                        visited[i]=true;
                    }
                }
        }

    }
    /** 
     * Get an array of the compute result of the vehicle's route
     * @param end  The Objective point of route planning 
     * @return the vehicle's route
     */
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
    /**
     * Get the length of the vehicle's route
     * @return Get the length of the vehicle's route
     */
    public int getpathlength(int end){
        return shortestpath[end];
    }
}

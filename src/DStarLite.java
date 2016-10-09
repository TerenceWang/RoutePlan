import java.util.ArrayList;
import java.util.Currency;
import java.util.PriorityQueue;
import java.util.regex.Matcher;

/**
 * Created by terence on 10/7/16.
 */
public class DStarLite {
    public Map map=new Map();
    public Floyd floyd=new Floyd();
    public int start,end,km,curposition;
    private int timecount;
    private int distancecount;
    private ArrayList<Integer> pathcount;
    private int []rhs;
    private int []g;
    private PriorityQueue<State> priorityqueue;
    private ArrayList<int[][]> timeSeriesMapList;
    DStarLite(){

    }
    DStarLite(Map m,Floyd fd, ArrayList<int[][]> timeSeriesMapList,int start, int end){
        this.map=m;
        this.floyd=fd;
        this.start=start;
        this.curposition=start;
        this.end=end;
        timecount=1;
        distancecount=0;
        pathcount=new ArrayList<Integer>();
        rhs=new int[m.nodetotal];
        g=new int[m.nodetotal];
        priorityqueue=new PriorityQueue<State>();
        this.timeSeriesMapList=timeSeriesMapList;
    }

    private State CalculateKey(int s){
        int first=Math.min(getG(s),getRHS(s))+heuristic(curposition,s)+km;
        if (first < 0)
            first = Integer.MAX_VALUE;
        int second=Math.min(getG(s),getRHS(s));
        return new State(s,first,second);
    }
    private void init(){
        for (int i = 0; i < rhs.length; i++) {
            rhs[i]=Integer.MAX_VALUE;
            g[i]=Integer.MAX_VALUE;
        }
        km=0;
        rhs[end]=0;
        //g[end]=0;
        priorityqueue.add(CalculateKey(end));
    }
    private int getG(int u){
        return g[u];
    }
    private void UpdateVertex(int u){
        if(u!=end){
            int []s=getSucc(u);
            for (int i = 0; i < s.length; i++) {
                if(g[s[i]] != Integer.MAX_VALUE)
                    if(rhs[u]>g[s[i]]+timeSeriesMapList.get(timecount)[u][s[i]])
                        rhs[u]=g[s[i]]+timeSeriesMapList.get(timecount)[u][s[i]];

                    System.out.println(s[i] + " g " + g[s[i]]);
                    System.out.println("map " + timeSeriesMapList.get(timecount)[u][s[i]]);
                    System.out.println(u + " " + rhs[u]);

            }
        }
        State temp=new State(u,0,0);
        if(priorityqueue.contains(temp))
        {
            priorityqueue.remove(temp);
            System.out.println("Remove1 " + temp.vertex + " Remain " + priorityqueue.size());
        }
        System.out.println("g " + g[u] + " rhs " + rhs[u]);
        if(g[u]!=rhs[u])
            priorityqueue.add(CalculateKey(u));

    }
    private void ComputeShortestPath(){
        System.out.println("compute " + priorityqueue.peek().compareTo(CalculateKey(curposition)));
        while (!priorityqueue.isEmpty()
                &&(priorityqueue.peek().compareTo(CalculateKey(curposition))==-1
                ||getRHS(curposition)!=getG(curposition))){
            State kold=priorityqueue.peek();
            State u = priorityqueue.poll();
            System.out.println("Remove2 " + u.vertex + " Remain " + priorityqueue.size());
            State knew=CalculateKey(u.vertex);
            System.out.println(getG(u.vertex) + " g  rhs " +getRHS(u.vertex) + " " + kold.compareTo(knew));
            if(kold.compareTo(knew)==-1)
                priorityqueue.add(knew);
            else if(getG(u.vertex)>getRHS(u.vertex)){
                g[u.vertex]=rhs[u.vertex];
                int []s=getPrev(u.vertex);
                for (int i = 0; i < s.length; i++) {
                    System.out.println("1!111 " + s[i]);
                    UpdateVertex(s[i]);
                }
            }
            else {
                g[u.vertex]=Integer.MAX_VALUE;
                System.out.println("!$%#^$#@$%#@$@$%#^$#@ " + u.vertex);
                int []s=getPrev(u.vertex);
                for (int i = 0; i < s.length; i++) {
                    System.out.println("1!111 " + s[i]);
                    UpdateVertex(s[i]);
                }
                System.out.println("1!111 " + u.vertex);
                UpdateVertex(u.vertex);
            }
        }
    }
    public void doDStarLite(){
        int last=curposition;
        init();
        ComputeShortestPath();
        pathcount.add(start);
        while (curposition!=end){
            int []s=getSucc(curposition);
            int min=Integer.MAX_VALUE;
            System.out.println("Pos before " + curposition);
            int temp=-1;
            for (int i = 0; i < s.length; i++) {
                if (g[s[i]] != Integer.MAX_VALUE && min>timeSeriesMapList.get(timecount)[curposition][s[i]]+g[s[i]]){
                    min=timeSeriesMapList.get(timecount)[curposition][s[i]]+g[s[i]];
                    temp=s[i];
                }
            }
            if (temp == -1)
                System.out.println("!!!!!!!!!");
            int timeold=timecount;
            timecount+=timeSeriesMapList.get(timecount)[curposition][temp];
            distancecount+=timeSeriesMapList.get(0)[curposition][temp];
            pathcount.add(temp);
            curposition=temp;
            System.out.println("Pos " + curposition);
            boolean flag=false;
            for (int i = 0; i < map.nodetotal; i++) {
                for (int j = 0; j < map.nodetotal; j++) {
                    if(timeSeriesMapList.get(timecount)[i][j]==Integer.MAX_VALUE)
                        continue;
                    //System.out.println(i+" " + j + " "+timecount+" "+timeSeriesMapList.get(timecount)[i][j]);
                    if(timeSeriesMapList.get(timecount)[i][j]!=timeSeriesMapList.get(timeold)[i][j]){
                        flag=true;
                        System.out.println("2!222");
                        UpdateVertex(i);
                    }
                }
            }
            if(priorityqueue.isEmpty())
            {
                System.out.println("!REPORT!");
                //priorityqueue.add(CalculateKey(curposition));
            }
            if (flag){
                km += heuristic(last,curposition);
                last=curposition;
                ComputeShortestPath();
            }
        }
    }
    public int[] getpath(){
        int [] result=new int[pathcount.size()];
        for (int i = 0; i < result.length; i++) {
            result[i]=pathcount.get(i);
        }
        return result;
    }
    public int getpathlength(){
        return distancecount;
    }
    public int getTimecount(){
        return timecount;
    }
    private int getRHS(int u){
//        if (u == s_goal) return 0;
//
//        //if the cellHash doesn't contain the State u
//        if (cellHash.get(u) == null)
//            return heuristic(u, s_goal);
//        return cellHash.get(u).rhs;
        return rhs[u];
    }

    private int[] getPrev(int u){
        int vertexnumbber=map.graph.getEdgeMatrix().length;
        int[][] edge=map.graph.getEdgeMatrix();
        int vertexperline=(int)Math.sqrt(vertexnumbber);
        int row=u/vertexperline;
        int col=u%vertexperline;
        ArrayList<Integer> t = new ArrayList<Integer>();
        int cola=col-1;
        int colb=col+1;
        int rowa=row-1;
        int rowb=row+1;
        if(cola>-1&&edge[cola+row*vertexperline][u]!=Integer.MAX_VALUE){
            t.add(cola+row*vertexperline);
        }
        if(colb<vertexperline&&edge[colb+row*vertexperline][u]!=Integer.MAX_VALUE){
            t.add(colb+row*vertexperline);
        }
        if(rowa>-1&&edge[rowa*vertexperline+col][u]!=Integer.MAX_VALUE){
            t.add(rowa*vertexperline+col);
        }
        if(rowb<vertexperline&&edge[rowb*vertexperline+col][u]!=Integer.MAX_VALUE){
            t.add(rowb*vertexperline+col);
        }
        int []s=new int[t.size()];
        for (int i = 0; i < s.length; i++) {
            s[i]=t.get(i);
        }
        return s;
    }
    private int[] getSucc(int u){
        ArrayList<Integer> t=new ArrayList<Integer>();
        int id=map.graph.getFirstNeighbor(u);
        t.add(id);
        while (id!=-1){
            id=map.graph.getNextNeighbor(u,id);
            if(id!=-1){
                t.add(id);
            }
        }
        int []s=new int[t.size()];
        for (int i = 0; i < s.length; i++) {
            s[i]=t.get(i);
        }
        return s;
    }
    private int heuristic(int start,int end){
        return floyd.getpathlength(start,end);
    }


}

import java.util.ArrayList;
import java.util.Currency;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.zip.Inflater;

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
    private int []tag;
    private int []father;
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
        this.tag=new int [m.nodetotal];
        this.father=new int [m.nodetotal];
    }

    private State CalculateKey(int s){

        int first=Math.min(getG(s),getRHS(s));
        if (first!= Integer.MAX_VALUE)
            first += heuristic(curposition,s) + km;
        int second=Math.min(getG(s),getRHS(s));
        return new State(s,first,second);
    }
    private void init(){
        for (int i = 0; i < rhs.length; i++) {
            rhs[i]=Integer.MAX_VALUE;
            g[i]=Integer.MAX_VALUE;
        }
        //=0;
        rhs[end]=0;
        //g[end]=0;
        km = 0;
        priorityqueue.add(CalculateKey(end));
        for (int i = 0; i < father.length; i++) {
            father[i]=i;
        }
        tag[end] = 1;
    }
    private int getG(int u){
        return g[u];
    }
    private void UpdateVertex(int u){
        if(u!=end){
            int []s=getSucc(u);
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < s.length; i++) {
                if(g[s[i]] != Integer.MAX_VALUE)
                    if(min>g[s[i]]+timeSeriesMapList.get(timecount)[u][s[i]])
                        min=g[s[i]]+timeSeriesMapList.get(timecount)[u][s[i]];

//                    System.out.println(s[i] + " g " + g[s[i]]);
//                    System.out.println("map " + timeSeriesMapList.get(timecount)[u][s[i]]);
//                    System.out.println(u + " " + rhs[u]);

            }
            rhs[u] = min;
        }
        State temp=new State(u,0,0);
        if(priorityqueue.contains(temp))
        {
            priorityqueue.remove(temp);
            //System.out.println("Remove1 " + temp.vertex + " Remain " + priorityqueue.size());
        }
        //System.out.println("g " + g[u] + " rhs " + rhs[u]);
        if(g[u]!=rhs[u])
        {
            //System.out.println("ADD " + u + " Remain " + priorityqueue.size());
            priorityqueue.add(CalculateKey(u));
        }

    }
    private void CoumpueShortestPathLabelVersion(){
        State topv=priorityqueue.peek();
        while (!priorityqueue.isEmpty()
                &&(priorityqueue.peek().compareTo(CalculateKey(curposition))==-1
                ||getRHS(curposition)!=getG(curposition))){
            if(g[topv.vertex]>rhs[topv.vertex]){
                g[topv.vertex]=rhs[topv.vertex];
                tag[topv.vertex]=2;
                priorityqueue.remove(topv);
                int[] k=getPrev(topv.vertex);
                for (int i = 0; i < k.length; i++) {
                    update_lower(k[i],topv);
                }
            }
            else{
                g[topv.vertex]=Integer.MAX_VALUE;
                int[] k=getPrev(topv.vertex);
                for (int i = 0; i < k.length; i++) {
                    update_raise(k[i]);
                }
            }
            topv=priorityqueue.peek();
        }
    }
    private void update_lower(int u, State sourcev){
        switch (tag[u]){
            case 0:
                rhs[u]=g[sourcev.vertex]+timeSeriesMapList.get(timecount)[u][sourcev.vertex];
                father[u]=sourcev.vertex;
                tag[u]=1;
                priorityqueue.add(CalculateKey(u));
                break;
            case 1:
                if(rhs[u]>g[sourcev.vertex]+timeSeriesMapList.get(timecount)[u][sourcev.vertex]) {
                    rhs[u] = g[sourcev.vertex] + timeSeriesMapList.get(timecount)[u][sourcev.vertex];
                    father[u] = sourcev.vertex;
                    priorityqueue.remove(new State(u, 0, 0));
                    priorityqueue.add(CalculateKey(u));
                }
                break;
            case 2:
                if(rhs[u]>g[sourcev.vertex]+timeSeriesMapList.get(timecount)[u][sourcev.vertex]
                        || father[u]==sourcev.vertex){
                    rhs[u]=g[sourcev.vertex]+timeSeriesMapList.get(timecount)[u][sourcev.vertex];
                    father[u]=sourcev.vertex;
                    tag[u]=1;
                    priorityqueue.add(CalculateKey(u));
                }
                break;
        }
    }
    private void update_raise(int u){
        if(u!=end){
            int []k=getSucc(u);
            for (int i = 0; i < k.length; i++) {
                if(tag[k[i]]==2&&rhs[u]>g[k[i]]+timeSeriesMapList.get(timecount)[u][k[i]]){
                    rhs[u]=g[k[i]]+timeSeriesMapList.get(timecount)[u][k[i]];
                    father[u]=k[i];
                }
            }
            if(rhs[u]!=g[u]&&tag[u]!=1){
                tag[u]=1;
                priorityqueue.add(CalculateKey(u));
            }
            if(rhs[u]==g[u]&&tag[u]==1){
                tag[u]=2;
                priorityqueue.remove(new State(u,0,0));
            }

        }
    }
    private void ComputeShortestPath(){
        //System.out.println("compute " + priorityqueue.peek().compareTo(CalculateKey(curposition)) + " " + (getRHS(curposition)!=getG(curposition)));
        //System.out.println(priorityqueue.peek().vertex + " " + priorityqueue.peek().first + " " + priorityqueue.peek().second
        //        + " " +getRHS(curposition) + " " + getG(curposition)
        //        + " " + CalculateKey(curposition).first + " "  + CalculateKey(curposition).second);
        while (!priorityqueue.isEmpty()
                &&(priorityqueue.peek().compareTo(CalculateKey(curposition))==-1
                ||getRHS(curposition)!=getG(curposition))){
            State kold=priorityqueue.peek();
            State u = priorityqueue.poll();
            //System.out.println("Remove2 " + u.vertex + " Remain " + priorityqueue.size());
            State knew=CalculateKey(u.vertex);
            //System.out.println(u.vertex + " " + "g "  +getG(u.vertex) + " rhs " +getRHS(u.vertex) + " " + kold.compareTo(knew));
            if(kold.compareTo(knew)==-1){
                //System.out.println("ADD " + knew.vertex + " Remain " + priorityqueue.size());
                priorityqueue.add(knew);
            }
            else if(getG(u.vertex)>getRHS(u.vertex)){
//                if (getG(u.vertex) == Integer.MAX_VALUE)
//                    System.out.print("K! " + u.vertex + " ");
                g[u.vertex]=rhs[u.vertex];
                //System.out.println(getG(u.vertex));
                int []s=getPrev(u.vertex);
                for (int i = 0; i < s.length; i++) {
                    //System.out.println("1!xxx " + s[i]);
                    UpdateVertex(s[i]);
                }
            }
            else {
                //System.out.println("Re " + u.vertex + " " + getG(u.vertex) + " " + getRHS(u.vertex));
                g[u.vertex]=Integer.MAX_VALUE;
                int []s=getPrev(u.vertex);
                for (int i = 0; i < s.length; i++) {
                    //System.out.println("1!111 " + s[i]);
                    UpdateVertex(s[i]);
                }
                //System.out.println("1!111 " + u.vertex);
                UpdateVertex(u.vertex);
                //System.out.println("KKKKKKKKK " + u.vertex +  " " + getRHS(u.vertex) + " " + getG(u.vertex));

            }
            //System.out.println("REMAIN " + priorityqueue.size());
//            if (!priorityqueue.isEmpty())
//            {
//                System.out.println("compute " + priorityqueue.peek().compareTo(CalculateKey(curposition)) + " " + (getRHS(curposition)!=getG(curposition)));
//                System.out.println(priorityqueue.peek().vertex + " " + priorityqueue.peek().first + " " + priorityqueue.peek().second
//                        + " " +getRHS(curposition) + " " + getG(curposition));
//            }
        }
    }
    public int doDStarLiteLabel(){
        int last=curposition;
        init();
        CoumpueShortestPathLabelVersion();
        pathcount.add(start);
        while (curposition != end){
            int []s=getSucc(curposition);
            int min=Integer.MAX_VALUE;
            //System.out.println("Pos before " + curposition);
            int temp = -1;
            for (int i = 0; i < s.length; i++) {
                //System.out.println("1--- " + temp + " " +  min + " " + s[i] + " " + timeSeriesMapList.get(timecount)[curposition][s[i]]+ " " + g[s[i]]);
                if (g[s[i]] != Integer.MAX_VALUE &&
                        min > timeSeriesMapList.get(timecount)[curposition][s[i]] + g[s[i]]){
                    min = (timeSeriesMapList.get(timecount)[curposition][s[i]] + g[s[i]]);
                    temp = s[i];
                    //System.out.println("1!! "+ temp + " " + min + " " + (timeSeriesMapList.get(timecount)[curposition][s[i]]+g[s[i]]));
                }
            }
            if (temp == -1)
                for (int i = 0; i < s.length; i++) {
                    //System.out.println("2--- " + temp + " " +  min + " " + s[i] + " " + timeSeriesMapList.get(timecount)[curposition][s[i]]);
                    if (min>timeSeriesMapList.get(timecount)[curposition][s[i]]){
                        min = (timeSeriesMapList.get(timecount)[curposition][s[i]]);
                        temp=s[i];
                        //System.out.println("1!! "+ temp + " " + min + " " + (timeSeriesMapList.get(timecount)[curposition][s[i]]));
                    }
                }
            int timeold = timecount;
            //System.out.println(timecount + " " + curposition + " " + temp);
            //System.out.println("cccc " + timeSeriesMapList.get(timecount)[curposition][temp] + " " + temp + " " + min);
            timecount += timeSeriesMapList.get(timecount)[curposition][temp];
            distancecount += timeSeriesMapList.get(0)[curposition][temp];
            //System.out.println("ADD " + temp + " Remain " + priorityqueue.size());
            pathcount.add(temp);
            curposition = temp;
            //System.out.println("Pos " + curposition);
            boolean flag=false;
            for (int i = 0; i < map.nodetotal; i++) {
                int []k=getSucc(i);
                for (int j = 0; j < k.length; j++) {
                    if(timeSeriesMapList.get(timecount)[i][k[j]]!=timeSeriesMapList.get(timeold)[i][k[j]]){
                        flag=true;
                        //System.out.println("2!222");
                        UpdateVertex(i);
                    }
                }
//                for (int j = 0; j < map.nodetotal; j++) {
//
//                    if(timeSeriesMapList.get(timecount)[i][j]==Integer.MAX_VALUE)
//                        continue;
//                    //System.out.println(i+" " + j + " "+timecount+" "+timeSeriesMapList.get(timecount)[i][j]);
//                    if(timeSeriesMapList.get(timecount)[i][j]!=timeSeriesMapList.get(timeold)[i][j]){
//                        flag=true;
//                        //System.out.println("2!222");
//                        UpdateVertex(i);
//                    }
//                }
            }
            if(priorityqueue.isEmpty())
            {
                //System.out.println("!REPORT!");
                //System.out.println("ADD " + curposition + " Remain " + priorityqueue.size());
                priorityqueue.add(CalculateKey(curposition));
            }
            if (flag){
                km += heuristic(last, curposition);
                last=curposition;
                CoumpueShortestPathLabelVersion();
            }
        }
        return 0;
    }
    public int doDStarLite(){
        int last=curposition;
        init();
        ComputeShortestPath();
        pathcount.add(start);
        while (curposition != end){
            int []s=getSucc(curposition);
            int min=Integer.MAX_VALUE;
            //System.out.println("Pos before " + curposition);
            int temp = -1;
            for (int i = 0; i < s.length; i++) {
                //System.out.println("1--- " + temp + " " +  min + " " + s[i] + " " + timeSeriesMapList.get(timecount)[curposition][s[i]]+ " " + g[s[i]]);
                if (g[s[i]] != Integer.MAX_VALUE &&
                        min > timeSeriesMapList.get(timecount)[curposition][s[i]] + g[s[i]]){
                    min = (timeSeriesMapList.get(timecount)[curposition][s[i]] + g[s[i]]);
                    temp=s[i];
                    //System.out.println("1!! "+ temp + " " + min + " " + (timeSeriesMapList.get(timecount)[curposition][s[i]]+g[s[i]]));
                }
            }
            if (temp == -1)
                for (int i = 0; i < s.length; i++) {
                    //System.out.println("2--- " + temp + " " +  min + " " + s[i] + " " + timeSeriesMapList.get(timecount)[curposition][s[i]]);
                    if (min>timeSeriesMapList.get(timecount)[curposition][s[i]]){
                        min = (timeSeriesMapList.get(timecount)[curposition][s[i]]);
                        temp=s[i];
                        //System.out.println("1!! "+ temp + " " + min + " " + (timeSeriesMapList.get(timecount)[curposition][s[i]]));
                    }
                }
            int timeold = timecount;
            //System.out.println(timecount + " " + curposition + " " + temp);
            //System.out.println("cccc " + timeSeriesMapList.get(timecount)[curposition][temp] + " " + temp + " " + min);
            timecount += timeSeriesMapList.get(timecount)[curposition][temp];
            distancecount += timeSeriesMapList.get(0)[curposition][temp];
            //System.out.println("ADD " + temp + " Remain " + priorityqueue.size());
            pathcount.add(temp);
            curposition = temp;
            //System.out.println("Pos " + curposition);
            boolean flag=false;
            for (int i = 0; i < map.nodetotal; i++) {
                int []k=getSucc(i);
                for (int j = 0; j < k.length; j++) {
                    if(timeSeriesMapList.get(timecount)[i][k[j]]!=timeSeriesMapList.get(timeold)[i][k[j]]){
                        flag=true;
                        //System.out.println("2!222");
                        UpdateVertex(i);
                    }
                }
//                for (int j = 0; j < map.nodetotal; j++) {
//
//                    if(timeSeriesMapList.get(timecount)[i][j]==Integer.MAX_VALUE)
//                        continue;
//                    //System.out.println(i+" " + j + " "+timecount+" "+timeSeriesMapList.get(timecount)[i][j]);
//                    if(timeSeriesMapList.get(timecount)[i][j]!=timeSeriesMapList.get(timeold)[i][j]){
//                        flag=true;
//                        //System.out.println("2!222");
//                        UpdateVertex(i);
//                    }
//                }
            }
            if(priorityqueue.isEmpty())
            {
                //System.out.println("!REPORT!");
                //System.out.println("ADD " + curposition + " Remain " + priorityqueue.size());
                priorityqueue.add(CalculateKey(curposition));
            }
            if (flag){
                km += heuristic(last, curposition);
                last=curposition;
                ComputeShortestPath();
            }
        }
        return 0;
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
    public int[] getSucc(int u){
        int[][] edge=map.graph.getEdgeMatrix();
        int vertexnumbber=edge.length;
        int vertexperline=(int)Math.sqrt(vertexnumbber);
        int row=u/vertexperline;
        int col=u%vertexperline;
        ArrayList<Integer> t = new ArrayList<Integer>();
        int cola=col-1;
        int colb=col+1;
        int rowa=row-1;
        int rowb=row+1;
        if(cola>-1&&edge[u][cola+row*vertexperline]!=Integer.MAX_VALUE){
            t.add(cola+row*vertexperline);
        }
        if(colb<vertexperline&&edge[u][colb+row*vertexperline]!=Integer.MAX_VALUE){
            t.add(colb+row*vertexperline);
        }
        if(rowa>-1&&edge[u][rowa*vertexperline+col]!=Integer.MAX_VALUE){
            t.add(rowa*vertexperline+col);
        }
        if(rowb<vertexperline&&edge[u][rowb*vertexperline+col]!=Integer.MAX_VALUE){
            t.add(rowb*vertexperline+col);
        }
        int []s=new int[t.size()];
        for (int i = 0; i < s.length; i++) {
            s[i]=t.get(i);
        }
        return s;
    }
    private int heuristic(int start,int end){
//        int result=0;
//        int []s=floyd.getpath(start,end);
//        for (int i = 0; i < s.length-1; i++) {
//            result+=timeSeriesMapList.get(timecount)[s[i]][s[i+1]];
//        }

        return floyd.getpathlength(start,end);
//        return result;
    }

}

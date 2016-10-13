import java.util.ArrayList;
import java.util.Currency;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.zip.Inflater;

/**
 * Created by terence on 10/7/16.
 */

/**
 * This is the algorithm for the DStarLite
 *
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
    int[][] edge;
    DStarLite(){

    }
    /**
     * Init
     * @param m the Map
     * @param fd the result of the Floyd algorithm
     * @param timeSeriesMapList the reuslt of the generated time-vary map
     * @param start start point
     * @param end end point
     */
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
        this.edge = m.graph.getEdgeMatrix();
    }

    /**
     * Calculate the value the node s's key value
     * @param s the id of the node s
     * @return a State contains the s's key value
     */
    private State CalculateKey(int s){

        int first=Math.min(getG(s),getRHS(s));
        if (first!= Integer.MAX_VALUE)
            first += heuristic(curposition,s) + km;
        int second=Math.min(getG(s),getRHS(s));
        return new State(s,first,second);
    }

    /**
     * init the algorithms
     */
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

    /**
     *
     * @param u
     * @return the value of G of the ndoe u
     */
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

            }
            rhs[u] = min;
        }
        State temp=new State(u,0,0);
        if(priorityqueue.contains(temp))
        {
            priorityqueue.remove(temp);
        }
        if(g[u]!=rhs[u])
        {
            priorityqueue.add(CalculateKey(u));
        }

    }

    /**
     *
     * Compute the shortest path from the current time node to the goal
     *
     */
    private void ComputeShortestPath(){
        while (!priorityqueue.isEmpty()
                &&(priorityqueue.peek().compareTo(CalculateKey(curposition))==-1
                ||getRHS(curposition)!=getG(curposition))){
            State kold=priorityqueue.peek();
            State u = priorityqueue.poll();
            State knew=CalculateKey(u.vertex);
            if(kold.compareTo(knew)==-1){
                priorityqueue.add(knew);
            }
            else if(getG(u.vertex) > getRHS(u.vertex)){

                g[u.vertex] = rhs[u.vertex];
                int []s=getPrev(u.vertex);
                for (int i = 0; i < s.length; i++) {
                    UpdateVertex(s[i]);
                }
            }
            else {
                g[u.vertex]=Integer.MAX_VALUE;
                int []s=getPrev(u.vertex);
                for (int i = 0; i < s.length; i++) {
                    UpdateVertex(s[i]);
                }
                UpdateVertex(u.vertex);

            }
        }
    }
    /**
     * Main function of the algorithm, the count the car's running time and the its running length
     * @return if return -1, means the car can not get to the goal within the length of the timeSeriesMapList
     */
    public int doDStarLite(){
        int last=curposition;
        init();
        ComputeShortestPath();
        pathcount.add(start);
        while (curposition != end){

            int []s=getSucc(curposition);
            int min=Integer.MAX_VALUE;
            int temp = -1;
            for (int i = 0; i < s.length; i++) {
                if (g[s[i]] != Integer.MAX_VALUE && timeSeriesMapList.get(timecount)[curposition][s[i]] != Integer.MAX_VALUE &&
                        min > timeSeriesMapList.get(timecount)[curposition][s[i]] + g[s[i]]){
                    min = (timeSeriesMapList.get(timecount)[curposition][s[i]] + g[s[i]]);
                    temp=s[i];
                }
            }
            if (temp == -1)
                for (int i = 0; i < s.length; i++) {
                    if (min>timeSeriesMapList.get(timecount)[curposition][s[i]]){
                        min = (timeSeriesMapList.get(timecount)[curposition][s[i]]);
                        temp=s[i];
                    }
                }
            int timeold = timecount;
            timecount += timeSeriesMapList.get(timecount)[curposition][temp];
            if(timecount>=timeSeriesMapList.size())
                return -1;
            distancecount += timeSeriesMapList.get(0)[curposition][temp];
            pathcount.add(temp);
            curposition = temp;
            boolean flag=false;
            for (int i = 0; i < map.nodetotal; i++) {
                int []k=getSucc(i);
                for (int j = 0; j < k.length; j++) {
                    if(timeSeriesMapList.get(timecount)[i][k[j]]!=timeSeriesMapList.get(timeold)[i][k[j]]){
                        flag=true;
                        UpdateVertex(i);
                    }
                }
            }
            if(priorityqueue.isEmpty())
            {
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
    /**
     * Get a array of the compute result of the car's route
     * @return the car's route
     */
    public int[] getpath(){
        int [] result=new int[pathcount.size()];
        for (int i = 0; i < result.length; i++) {
            result[i]=pathcount.get(i);
        }
        return result;
    }
    /**
     * Get the length of the car's route
     * @return Get the length of the car's route
     */
    public int getpathlength(){
        return distancecount;
    }
    /**
     * get the total time of the route
     * @return the total time of the route
     */
    public int getTimecount(){
        return timecount;
    }
    /**
     * Get the rhs value of node u
     * @param u the id of the node u
     * @return The value of the rhs value
     */
    private int getRHS(int u){
        return rhs[u];
    }

    /**
     * get the previous node of the node u
     * @param u the id of the node u
     * @return the list of the previous node of u
     */
    private int[] getPrev(int u){
        int vertexnumbber=timeSeriesMapList.get(0).length;
        int vertexperline=(int)Math.sqrt(vertexnumbber);
        int row=u/vertexperline;
        int col=u%vertexperline;
        ArrayList<Integer> t = new ArrayList<Integer>();
        int cola=col-1;
        int colb=col+1;
        int rowa=row-1;
        int rowb=row+1;
        if(cola>-1&&timeSeriesMapList.get(timecount)[cola+row*vertexperline][u]!=Integer.MAX_VALUE){
            t.add(cola+row*vertexperline);
        }
        if(colb<vertexperline&&timeSeriesMapList.get(timecount)[colb+row*vertexperline][u]!=Integer.MAX_VALUE){
            t.add(colb+row*vertexperline);
        }
        if(rowa>-1&&timeSeriesMapList.get(timecount)[rowa*vertexperline+col][u]!=Integer.MAX_VALUE){
            t.add(rowa*vertexperline+col);
        }
        if(rowb<vertexperline&&timeSeriesMapList.get(timecount)[rowb*vertexperline+col][u]!=Integer.MAX_VALUE){
            t.add(rowb*vertexperline+col);
        }
        int []s=new int[t.size()];
        for (int i = 0; i < s.length; i++) {
            s[i]=t.get(i);
        }
        return s;
    }
    /**
     * get the previous node of the node u
     * @param u u the id of the node u
     * @return the list of the previous node of u
     */
    public int[] getSucc(int u){
        int vertexnumbber=edge.length;
        int vertexperline=(int)Math.sqrt(vertexnumbber);
        int row=u/vertexperline;
        int col=u%vertexperline;
        ArrayList<Integer> t = new ArrayList<Integer>();
        int cola=col-1;
        int colb=col+1;
        int rowa=row-1;
        int rowb=row+1;
        if(cola>-1&&timeSeriesMapList.get(timecount)[u][cola+row*vertexperline]!=Integer.MAX_VALUE){
            t.add(cola+row*vertexperline);
        }
        if(colb<vertexperline && timeSeriesMapList.get(timecount)[u][colb+row*vertexperline]!=Integer.MAX_VALUE){
            t.add(colb+row*vertexperline);
        }
        if(rowa>-1&&timeSeriesMapList.get(timecount)[u][rowa*vertexperline+col]!=Integer.MAX_VALUE){
            t.add(rowa*vertexperline+col);
        }
        if(rowb<vertexperline&&timeSeriesMapList.get(timecount)[u][rowb*vertexperline+col]!=Integer.MAX_VALUE){
            t.add(rowb*vertexperline+col);
        }
        int []s=new int[t.size()];
        for (int i = 0; i < s.length; i++) {
            s[i]=t.get(i);
        }
        return s;
    }
    /**
     * get the heuristic value of a pair node, we have two version. 1, use the value of the current map;
     * 2. use the value of the time = 0 map, that is the value of the map without traffic time flow
     * @param start the start point
     * @param end the end point
     * @return the heuristic value of a pair node
     */
    private int heuristic(int start,int end){
//        int result=0;
//        int []s=floyd.getpath(start,end);
//        for (int i = 0; i < s.length-1; i++) {
//            result+=timeSeriesMapList.get(timecount)[s[i]][s[i+1]];
//        }
//
//        return result;
        return floyd.getpathlength(start,end);

    }

}

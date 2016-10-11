import javax.naming.ldap.StartTlsRequest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by terence on 10/11/16.
 */
public class RRTPlan {
    RRTPlan(){

    }
    private int timecount;
    private int pathcount;
    private Floyd floyd;
    private int start;
    private int end;
    private int now;
    private int time;
    private Map map;
    private int length;
    private int[][] edge;
    private ArrayList<int[][]> timeSeriesMapList;
    Stack<Integer> stack= new Stack<Integer>();
    ArrayList<Integer> path;
    boolean[] visited;
    RRTPlan(Map map,Floyd floyd, ArrayList<int[][]>timeSeriesMapList,int start, int end){
        this.timecount=0;
        this.pathcount=0;
        this.map=map;
        this.floyd=floyd;
        this.start= start;
        this.end=end;
        this.time = 0;
        this.length = 0;
        this.timeSeriesMapList=timeSeriesMapList;
        this.now=start;
        this.path=new ArrayList<Integer>();
        this.visited = new boolean[map.nodetotal];
        this.edge=map.graph.getEdgeMatrix();
        for(int i = 0; i < map.nodetotal; ++i)
            visited[i] = false;
    }

    private int therhold_target_goal(){
//        return (int)(0.3 * floyd.getpathlength(now, end));
        return (int) (0.9*heuristic(now,end));
    }
//    private int therhold_now_target(int a,int b){
////        return ((int)Math.max(timecount / (double)pathcount, 1.4) * floyd.getpathlength(a, b));
//        return (int)(1.5 * floyd.getpathlength(a, b));
//    }

    public int[] getpath(){
        int [] result=new int[path.size()];
        for (int i = 0; i < result.length; i++) {
            result[i]=path.get(i);
        }
        return result;
    }
    public int getpathlength(){
        return pathcount;
    }
    public int getTimecount(){
        return timecount;
    }
    public int repeatdorrtplan(){
        now=start;
        stack.push(now);
        path.add(start);
        visited[start] = true;
        while(now!=end){
            int next=dorrtplan();
            length = 0;
            time = 0;
            if(next<0)
            {
                System.out.println("WTF!");
                return -1;
            }
            timecount+=timeSeriesMapList.get(timecount)[now][next];
            pathcount+=timeSeriesMapList.get(0)[now][next];
            int temp=next;
            path.add(temp);

            System.out.println("ADD!!!!!" + next);
            now=next;
            System.out.println("Time " + timecount + " Length " + pathcount);
            if (timecount > 1000)
            {
                int[] st=getpath();
                for (int j = 0; j < st.length; j++) {
                    System.out.print(st[j]+" ");
                }
                System.out.println();
                return -1;
            }
            visited[now] = true;
            while(!stack.isEmpty())
                stack.pop();
            stack.push(now);
        }
        return 0;
    }
    private int heuristic(int start,int end){
        int result=0;
        int []s=floyd.getpath(start,end);
        for (int i = 0; i < s.length-1; i++) {
            result+=timeSeriesMapList.get(timecount)[s[i]][s[i+1]];
        }

//        return floyd.getpathlength(start,end);
        return result;
    }
    public int dorrtplan(){

//        if (stack.size() > 10)
//        {
//            System.out.println("2Deep 2Reach.");
//            return -1;
//        }
        int target = stack.peek();
//        System.out.println(target + " " + stack.size());
        int result = target;
//        if (floyd.getpathlength(target, end) < therhold_target_goal()) {
        if(heuristic(target,end) < therhold_target_goal()){
            if (time < (int)(2.5 * heuristic(now, target)) || target == end) {
                if (stack.size() < 2)
                    result = target;
                while(stack.size() >= 2)
                {
                    result = stack.pop();
                }
//                System.out.println("Ans " + result + " " + stack.size());
                return result;
            } else {
                System.out.println(target + " 2long 2reach " + time + " " + (2 * length));
                return -1;
            }
        }
        else {
            int[] s = getSucc(target);
//            System.out.println(target + " NumSucc " + s.length);
            for (int i = 0; i < s.length; i++) {
                System.out.println("Try " + target + " " + s[i]);
//                if (1.2 * floyd.getpathlength(target, end) <= floyd.getpathlength(s[i], end))
                if(1.3 * heuristic(target,end) < heuristic(s[i],end))
                {
                    System.out.println("cut");
                    continue;
                }
                if (visited[s[i]])
                {
                    System.out.println("Circle.");
                    continue;
                }
                stack.push(s[i]);
                time += timeSeriesMapList.get(timecount)[target][s[i]];
                length += timeSeriesMapList.get(0)[target][s[i]];
                visited[s[i]] = true;
                int resultGet = dorrtplan();
                if (resultGet > -1)
                {
                    visited[s[i]] = false;
                    return resultGet;
                }
                visited[s[i]] = false;
                time -= timeSeriesMapList.get(timecount)[target][s[i]];
                length -= timeSeriesMapList.get(0)[target][s[i]];
                System.out.println(time + " x " + length);
            }
        }
        System.out.println("Finish trying " + target);
        return -1;
    }
    private int[] getSucc(int u){
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
        if(colb<vertexperline&&timeSeriesMapList.get(timecount)[u][colb+row*vertexperline]!=Integer.MAX_VALUE){
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
        shuffleArray(s);
        return s;
    }
     void shuffleArray(int[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}

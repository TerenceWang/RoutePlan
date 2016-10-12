import java.util.ArrayList;

/**
 * Created by terence on 10/8/16.
 */
public class RepeatDijsktra {
    private ArrayList<int[][]> timeSeriesMapList;
    private int start;
    private int end;
    private int timecount;
    private int distancecount;
    private ArrayList<Integer> pathcount;
    RepeatDijsktra(ArrayList<int[][]> timeSeriesMapList,int start, int end){
        this.timeSeriesMapList=timeSeriesMapList;
        this.start=start;
        this.end=end;
        this.timecount=0;
        this.distancecount=0;
        this.pathcount=new ArrayList<Integer>();
    }
    public int doRepeatDijsktra(){
        Dijsktra dj=new Dijsktra();
        dj.dodijsktra(timeSeriesMapList.get(0),start);
        int now=dj.getpath(end)[1];
        timecount+=timeSeriesMapList.get(0)[start][now];
        distancecount+=timeSeriesMapList.get(0)[start][now];
//        System.out.println("lalal "+distancecount);
        pathcount.add(start);
        pathcount.add(now);
        while(now!=end){
            Dijsktra djj=new Dijsktra();
            int res=djj.dodijsktra(timeSeriesMapList.get(timecount),now);
            int temp;
            if(res<0){
                int []t=getSucc(now);
                int min=Integer.MAX_VALUE;
                for (int i = 0; i < t.length; i++) {
                    if(min<t[i])
                        min=t[i];
                }
                if(min==Integer.MAX_VALUE)
                {
                    System.out.println("ALONE.");
                    return -1;
                }
                temp=min;
            }else
                temp=djj.getpath(end)[1];
            timecount+=timeSeriesMapList.get(timecount)[now][temp];
            if(timecount>=timeSeriesMapList.size())
            {
                System.out.println("OverTime.");
                return -1;
            }
            distancecount+=timeSeriesMapList.get(0)[now][temp];
            pathcount.add(temp);
            now=temp;
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
    public int[] getSucc(int u){
        int vertexnumbber=timeSeriesMapList.get(timecount).length;
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
        return s;
    }
}

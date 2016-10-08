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
    public void doRepeatDijsktra(){
        Dijsktra dj=new Dijsktra();
        dj.dodijsktra(timeSeriesMapList.get(0),start);
        int now=dj.getpath(end)[1];
        timecount+=timeSeriesMapList.get(0)[start][now];
        distancecount+=timeSeriesMapList.get(0)[start][now];
        System.out.println("lalal "+distancecount);
        pathcount.add(start);
        pathcount.add(now);
        while(now!=end){
            Dijsktra djj=new Dijsktra();
            djj.dodijsktra(timeSeriesMapList.get(timecount),now);
            int temp=djj.getpath(end)[1];
            timecount+=timeSeriesMapList.get(timecount)[now][temp];
            distancecount+=timeSeriesMapList.get(0)[now][temp];
            pathcount.add(temp);
            now=temp;
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

}

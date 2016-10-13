import java.util.ArrayList;

/**
 * Created by terence on 10/10/16.
 * Apply A* algorithm in dynamic route planning, in every timestep 
 * calls the current map and calculate the current optimal route     
 */
public class RepeatAStar {
    private ArrayList<int[][]> timeSeriesMapList;
    private Floyd floyd;
    private int start,end;
    private int timecount;
    private ArrayList<Integer> pathcount;
    private int distancecount;
    RepeatAStar(ArrayList<int[][]> timeSeriesMapList,Floyd floyd,int start,int end){
        this.timeSeriesMapList=timeSeriesMapList;
        this.floyd=floyd;
        this.start=start;
        this.end=end;
        pathcount=new ArrayList<Integer>();
        timecount=0;
        distancecount=0;
    }
    /**
     * Main function of the algorithm, apply the A* algorithm in every 
     * @return if return -1, means the vehicle can not get to the goal within the length of the timeSeriesMapList
     * if return 0, means the vehicle can reach the objective point
     */
    
    public int dorepeatastar(){
        Astar astar=new Astar(floyd);
        astar.doastar(timeSeriesMapList.get(0),start,end);
        int now=astar.getpath(start,end)[1];
        timecount+=timeSeriesMapList.get(0)[start][now];
        distancecount+=timeSeriesMapList.get(0)[start][now];

        pathcount.add(start);
        pathcount.add(now);
        while(now!=end){
            Astar astar1=new Astar(floyd);
            astar1.doastar(timeSeriesMapList.get(timecount),now,end);
            int temp=astar1.getpath(now,end)[1];
            timecount+=timeSeriesMapList.get(timecount)[now][temp];
            if(timecount>=timeSeriesMapList.size())
                return -1;
            distancecount+=timeSeriesMapList.get(0)[now][temp];
            pathcount.add(temp);
            now=temp;
        }
        return 0;
    }
    /**
     * Get the path array of the vehicle's route in the dynamic network
     * @return Get the path array of the vehicle's route
     */
    public int[] getpath(){
        int [] result=new int[pathcount.size()];
        for (int i = 0; i < result.length; i++) {
            result[i]=pathcount.get(i);
        }
        return result;
    }
    /**
     * Get the length of the vehicle's route in the dynamic network
     * @return Get the length of the vehicle's route
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
}

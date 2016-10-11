import java.util.ArrayList;
import java.util.Random;

/**
 * Created by terence on 10/8/16.
 */
public class TrafficFlow {
    private Map m;
    private double[][] probMap;
    private int[][] edge;
    TrafficFlow(Map map){
        this.m=map;
        probMap = generateegedweigh();
        this.edge=map.graph.getEdgeMatrix();

    }
    public double[][] generateegedweigh(){
        int nodecount=m.graph.getEdgeMatrix().length;
        int k=5;
        double[][] result=new double[nodecount][nodecount];
        for (int i = 0; i < nodecount; i++) {
            for (int j = 0; j < nodecount; j++) {
                result[i][j]=-1;
            }
        }
        for (int i = 0; i < nodecount; i++) {
            int[] suc = getSucc(i);
            for (int j = 0; j < suc.length; j++) {
                double temp = peredgeweight(k);
                if (temp < 0)
                    temp = 0;
                result[i][suc[j]] = temp;
            }
            double[] tmp = Normalization(result[i]);
            result[i] = tmp;
        }
//            ArrayList<Double> countlist=new ArrayList<Double>();
//            int id=m.graph.getFirstNeighbor(i);
//            if(id!=-1){
//                double temp=peredgeweight(k);
//                if(temp<0)
//                    countlist.add(0.0);
//                else
//                    countlist.add(temp);
//            }
//
//            while(id!=-1){
//                id=m.graph.getNextNeighbor(i,id);
//                if(id!=-1){
//                    double temp=peredgeweight(k);
//                    if(temp<0)
//                        countlist.add(0.0);
//                    else
//                        countlist.add(temp);
//                }
//            }
//            Double []tmp=new Double[countlist.size()];
//            countlist.toArray(tmp);
//            double[] normalization = Normalization(tmp);
//            result.add(normalization);
//        }
        return result;
    }


    private double[] Normalization(double[] list){
        double sum=0;
        double []result=new double[list.length];
        for (int i = 0; i < list.length; i++) {
            if(list[i]==-1)
                continue;
            sum+=list[i];
        }
        for (int i = 0; i < list.length; i++) {
            if(list[i]==-1)
                continue;
            result[i]=list[i]/sum;
        }
        return result;
    }
    private double peredgeweight(int k){
        Random r=new Random();
        double result=0;
        double tmp[]=new double[k];
        double klist[]=new double[k];
        for (int i = 0; i < k; i++) {
            tmp[i]=(double)r.nextInt();
        }
        klist=Normalization(tmp);
        for (int i = 0; i < klist.length; i++) {
            result+=(r.nextGaussian()+7)*klist[i];
        }
        return result;
    }

    public ArrayList<int[][]> generateTrafficFlow()
    {
        ArrayList timeSeriesMapList = new ArrayList<int[][]>();
        int nodecount = m.graph.getNumOfVertex();
        int[][] mapDefault = m.graph.getEdgeMatrix();
        int[][] timeMap = new int[nodecount][nodecount];
        int[][] mapNow = new int[nodecount][nodecount];
        int[][] mapTemp = new int[nodecount][nodecount];
        int[][] timeStartMap = new int[nodecount][nodecount];
        int probNewFlow = 20; // Probability of setting up a new traffic flow(1/prob)
        int baseNewFlow = 300; // Basic size of new traffic flow
        int addSizeNewFlow = 300; // Random size add to the new traffic flow (1 - addSizeNewFlow)
        int countInitFlow = 5; // How many flows set up while init
        int baseInitFlow = 1600; // Basic size while init traffic flow
        int addSizeInitFlow = 400; // Random size add to the init traffic flows (1 - addSizeInitFlow)
        int timeFunctionConst = 200; // Const in time function
        int leastFlow = 50; // Ignore the flow under leastFlow.
        int countflow = 0;
//        System.out.println(nodecount);
        for(int i = 0; i < nodecount; ++i)
            for(int j = 0; j < nodecount; ++j)
            {
                timeMap[i][j] = 0;
                mapNow[i][j] = 0;
                timeStartMap[i][j] = 0;
                mapTemp[i][j] = 0;
//                if (mapDefault[i][j] == Integer.MAX_VALUE)
//                    mapDefault[i][j] = 0;
            }
        timeSeriesMapList.add(mapDefault);
        //System.out.println("Init OVER.");
        ;

        while(countflow < countInitFlow)
        {
            for (int i = 0; i < nodecount; ++i)
            {
                Random rand = new Random();
                int randTemp = rand.nextInt(100);
                if (randTemp > 95 && countflow < countInitFlow)
                {
                    int flowTemp = rand.nextInt(addSizeInitFlow);
                    flowTemp += baseInitFlow;
                    countflow++;
                    for(int k = 0; k < nodecount;++k) {
                        int temp = (int)(flowTemp * probMap[i][k]);
                        if (temp < 0)
                            continue;
                        mapNow[i][k] += temp;
                        timeStartMap[i][k] = 1;
//                        System.out.println(i + " " + k + " " + temp);
                    }
//                    System.out.println(i);
                }
                //System.out.println("INIT "+ countflow);
            }
        }
        {
            int[][] timeMapOut = new int[nodecount][nodecount];
            for(int i = 0; i < nodecount; ++i)
            {
                for(int j = 0; j < nodecount; ++j)
                {
                    timeMap[i][j] = mapNow[i][j] * mapDefault[i][j] / timeFunctionConst + mapDefault[i][j];
                    if(timeMap[i][j]==0)
                        timeMapOut[i][j]=Integer.MAX_VALUE;
                    else
                        timeMapOut[i][j] = timeMap[i][j];
//                    if (timeMap[i][j] > 100)
//                        System.out.println(1 + " " + i + " " + j + " " + timeMap[i][j]);
//                if (timeMap[i][j] == 0)
//                    timeMap[i][j] = Integer.MAX_VALUE;
                }
            }
            timeSeriesMapList.add(timeMapOut);
        }
        for(int i = 0; i < nodecount; ++i)
            for(int j = 0; j < nodecount; ++j)
            {
                mapTemp[i][j] = mapNow[i][j];
                if (mapTemp[i][j] < leastFlow)
                    mapTemp[i][j] = 0;
                mapNow[i][j] = 0;
            }
        //System.out.println("1 OVER.");
        for(int timeSeries = 2; timeSeries < 600; ++timeSeries)
        {
            for (int i = 0; i < nodecount; ++i)
            {
                Random rand = new Random();
                int randTemp = rand.nextInt(probNewFlow);
                if (randTemp == 1)
                {
                    int flowTemp = rand.nextInt(addSizeNewFlow);
                    flowTemp += baseNewFlow;
                    countflow++;
                    for(int k = 0; k < nodecount;++k) {
                        int temp = (int)(flowTemp * probMap[i][k]);
                        if (temp < 0)
                            continue;
                        mapNow[i][k] += temp;
                        timeStartMap[i][k] = timeSeries;
                    }
                }

                for(int j = 0; j < nodecount; ++j)
                {
                    if (timeStartMap[i][j] + timeMap[i][j] >= timeSeries)
                    {
                        timeStartMap[i][j] = 0;
                        for(int k = 0; k < nodecount;++k) {
                            int temp = (int)(mapTemp[i][j] * probMap[j][k]);
                            if(temp < 0)
                                continue;
                            mapNow[j][k] += temp;
                            if (timeStartMap[i][k] == 0)
                                timeStartMap[j][k] = timeSeries;
                        }
                    }
                    else
                        mapNow[i][j] += mapTemp[i][j];
                }

            }
            {
                int[][] timeMapOut = new int[nodecount][nodecount];
                for(int i = 0; i < nodecount; ++i)
                    for(int j = 0; j < nodecount; ++j)
                    {
                        timeMap[i][j] = mapNow[i][j] * mapDefault[i][j] / timeFunctionConst + mapDefault[i][j];
                        if(timeMap[i][j]==0)
                            timeMapOut[i][j]=Integer.MAX_VALUE;
                        else
                            timeMapOut[i][j] = timeMap[i][j];
//                    if (mapNow[i][j] > 0)
//                        System.out.println(timeSeries + " " + i + " " + j + " " + mapNow[i][j] * mapDefault[i][j] / 200);
//                if (timeMap[i][j] == 0)
//                    timeMap[i][j] = Integer.MAX_VALUE;
                    }
                timeSeriesMapList.add(timeMapOut);
            }

            for(int i = 0; i < nodecount; ++i)
                for(int j = 0; j < nodecount; ++j)
                {
                    mapTemp[i][j] = mapNow[i][j];
                    if (mapTemp[i][j] < leastFlow )
                        mapTemp[i][j] = 0;
                    mapNow[i][j] = 0;
                }
            //System.out.println(timeSeries + " OVER.");
        }


        return timeSeriesMapList;
    }

    private  double findweight( Map m,ArrayList<double[]> s,int start,int end){

        int id=m.graph.getFirstNeighbor(start);
        int count=0;
        if(id==end){
            return s.get(start)[count];
        }
        while(id!=-1){
            id = m.graph.getNextNeighbor(start,id);
            count++;
            if(id==end){
                return s.get(start)[count];
            }
        }
        return -1;
    }
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
}

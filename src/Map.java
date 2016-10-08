import java.io.*;
import java.util.*;

/**
 * Created by terence on 10/1/16.
 */
public class Map {

    public AMWGraph graph;
    public int nodeperline;
    public int nodetotal;
    public int edgetotal;
    Map(){

    }

    public void readmap(String filename){
        File file = new File(filename);
        BufferedReader reader=null;
        try{
            reader = new BufferedReader(new FileReader(file));
            String tmp=reader.readLine();
            String[] parameters=tmp.split(" ");
            nodeperline=Integer.parseInt(parameters[0]);
            nodetotal=Integer.parseInt(parameters[1]);
            edgetotal=Integer.parseInt(parameters[2]);
            graph= new AMWGraph(nodetotal);
            for (int i = 0; i < nodetotal; i++) {
                graph.insertVertex(i);
            }
            while((tmp = reader.readLine())!=null){
                parameters=tmp.split(" ");
                graph.insertEdge(Integer.parseInt(parameters[0]),Integer.parseInt(parameters[1])
                        ,Integer.parseInt(parameters[2]));
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            if(reader!=null){
                try {
                    reader.close();
                }catch (IOException e1){
                }
            }
        }
    }

    public static double findweight( Map m,ArrayList<double[]> s,int start,int end){
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

    public static ArrayList<int[][]> generateTrafficFlow(Map m)
    {
        ArrayList timeSeriesMapList = new ArrayList<int[][]>();
        int nodecount = m.graph.getNumOfVertex();
        int[][] mapDefault = new int[nodecount][nodecount];
        int[][] timeMap = new int[nodecount][nodecount];
        int[][] mapNow = new int[nodecount][nodecount];
        int[][] mapTemp = new int[nodecount][nodecount];
        int[][] timeStartMap = new int[nodecount][nodecount];
        for(int i = 0; i < nodecount; ++i)
            for(int j = 0; j < nodecount; ++j)
            {
                timeMap[i][j] = 0;
                mapNow[i][j] = 0;
                timeStartMap[i][j] = 0;
                mapTemp[i][j] = 0;
                mapDefault[i][j] = m.graph.getEdgeMatrix()[i][j];
//                if (mapDefault[i][j] == Integer.MAX_VALUE)
//                    mapDefault[i][j] = 0;
            }
        timeSeriesMapList.add(mapDefault);
        ArrayList<double[]> probMap = generateegedweigh(m);

        int countflow = 0;
        while(countflow < 2)
        {
            for (int i = 0; i < nodecount; ++i)
            {
                Random rand = new Random();
                int randTemp = rand.nextInt(100);
                if (randTemp > 95 && countflow < 2)
                {
                    int flowTemp = rand.nextInt(400);
                    flowTemp += 600;
                    countflow++;
                    for(int k = 0; k < nodecount;++k) {
                        int temp = (int)(flowTemp * findweight(m, probMap, i, k));
                        if (temp < 0)
                            continue;
                        mapNow[i][k] += temp;
                        timeStartMap[i][k] = 1;
                        System.out.println(i + " " + k + " " + temp);
                    }
                    System.out.println(i);
                }
            }
        }
        {
            int[][] timeMapOut = new int[nodecount][nodecount];
            for(int i = 0; i < nodecount; ++i)
            {
                for(int j = 0; j < nodecount; ++j)
                {
                    timeMap[i][j] = mapNow[i][j] * mapDefault[i][j] /150 + mapDefault[i][j];
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
                if (mapTemp[i][j] < 100)
                    mapTemp[i][j] = 0;
                mapNow[i][j] = 0;
            }

        for(int timeSeries = 2; timeSeries < 100; ++timeSeries)
        {
            for (int i = 0; i < nodecount; ++i)
            {
                Random rand = new Random();
                int randTemp = rand.nextInt(100);
                if (randTemp == 1)
                {
                    int flowTemp = rand.nextInt(300);
                    flowTemp += 100;
                    countflow++;
                    for(int k = 0; k < nodecount;++k) {
                        int temp = (int)(flowTemp * findweight(m, probMap, i, k));
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
                            int temp = (int)(mapTemp[i][j] * findweight(m, probMap, j, k));
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
                        timeMap[i][j] = mapNow[i][j] * mapDefault[i][j]/150 + mapDefault[i][j];
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
                    if (mapTemp[i][j] < 100 )
                        mapTemp[i][j] = 0;
                    mapNow[i][j] = 0;
                }
        }


        return timeSeriesMapList;
    }


    public static ArrayList<double[]> generateegedweigh(Map m){
        int nodecount=m.graph.getEdgeMatrix().length;
        int k=5;
        ArrayList<double[]> result=new ArrayList<>();
        for (int i = 0; i < nodecount; i++) {
            ArrayList<Double> countlist=new ArrayList<Double>();
            int id=m.graph.getFirstNeighbor(i);
            if(id!=-1){
                double temp=peredgeweight(k);
                if(temp<0)
                    countlist.add(0.0);
                else
                    countlist.add(temp);
            }

            while(id!=-1){
                id=m.graph.getNextNeighbor(i,id);
                if(id!=-1){
                    double temp=peredgeweight(k);
                    if(temp<0)
                        countlist.add(0.0);
                    else
                        countlist.add(temp);
                }
            }
            Double []tmp=new Double[countlist.size()];
            countlist.toArray(tmp);
            double[] normalization = Normalization(tmp);
            result.add(normalization);
        }
        return result;
    }


    public static double[] Normalization(Double[] list){
        double sum=0;
        double []result=new double[list.length];
        for (int i = 0; i < list.length; i++) {
            sum+=list[i];
        }
        for (int i = 0; i < list.length; i++) {
            result[i]=list[i]/sum;
        }
        return result;
    }
    static double peredgeweight(int k){
        Random r=new Random();
        double result=0;
        Double tmp[]=new Double[k];
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
    public static void main(String[] args) {
        Map m=new Map();
        m.readmap("data/nodenumber_10_edgenumber_327.map");
        int [][]s=m.graph.getEdgeMatrix();
        int start=5;
        int end=40;


        Dijsktra di=new Dijsktra();
        di.dodijsktra(s,start);
//
//
//        SPFA sp=new SPFA();
//        sp.dospfa(s,start);
//        Floyd fl=new Floyd();
//        fl.dofloyd(s);
//        int ss[]=fl.getpath(start,end);
//        for (int i = 0; i < ss.length; i++) {
//            System.out.println(ss[i]+" ");
//        }

        ArrayList<int[][]> tmp = new ArrayList<>();
        tmp=generateTrafficFlow(m);

        RepeatDijsktra repeatDijsktra=new RepeatDijsktra(tmp,start,end);

        repeatDijsktra.doRepeatDijsktra();
        for (int i = 0; i < repeatDijsktra.getpath().length; i++) {
            System.out.println(repeatDijsktra.getpath()[i]);
        }
        System.out.println("============");
        for (int i = 0; i < di.getpath(end).length; i++) {
            System.out.println(di.getpath(end)[i]);
        }
        System.out.println("===============");
        System.out.println(repeatDijsktra.getTimecount());
        System.out.println(di.getpathlength(end));
//        for (int i = 0; i < 2; i++) {
//            for(int j = 0; j < tmp.get(i).length; ++j) {
//                for(int k = 0; k < tmp.get(i).length; ++k)
//                    System.out.print(tmp.get(i)[j][k] + " ");
//                System.out.println();
//
//            }
//            System.out.println();
//        }

    }
}

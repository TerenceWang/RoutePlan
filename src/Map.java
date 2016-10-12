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



    public static void main(String[] args) {
        Map m=new Map();
        m.readmap("data/nodenumber_400_edgenumber_1376.map");
        int [][]s=m.graph.getEdgeMatrix();
        int start=5;
        int end=300;


        //Dijsktra dijsktra=new Dijsktra();
        //dijsktra.dodijsktra(s,start);

        Floyd floyd=new Floyd();
        floyd.dofloyd(s);
        System.out.println("Floyd OVER.");

//
//
//        SPFA sp=new SPFA();
//        sp.dospfa(s,start);

//        int ss[]=fl.getpath(start,end);
//        for (int i = 0; i < ss.length; i++) {
//            System.out.println(ss[i]+" ");
//        }


        int startimecount=0;
        int stardistance=0;
        int dijtime=0;
        int dijdistance=0;
        long dijruntime=0;
        long starruntime=0;
        long starlabelruntime=0;
        long starlabeldistance = 0;
        long starlabeltime = 0;
        long astartime=0;
        long astarruntime=0;
        long astardistance=0;
        int count=0;
        TrafficFlow trafficFlow=new TrafficFlow(m);

        BufferedWriter out= null;
        try {
            out = new BufferedWriter(new FileWriter("data/" + "5-result-400map.txt",true));
        for (int ii= 0; ii < 200; ++ii)
        {
            System.out.println("It " + ii + "Start.");
            ArrayList<int[][]> tmp = new ArrayList<>();
            tmp=trafficFlow.generateTrafficFlow();
//            tmp=trafficFlow.generateCongestion();
            System.out.println("TrafficFlow " + ii + " OVER.");

            DStarLite dStarLite=new DStarLite(m,floyd,tmp,start,end);
            long time3=System.currentTimeMillis();
            int res=dStarLite.doDStarLite();
            long time4=System.currentTimeMillis();
            if(res<0) {
                System.out.println("dStarLite Crash");
                continue;
            }
            System.out.println("1");

            DStarLite dStarLiteLabel=new DStarLite(m,floyd,tmp,start,end);
            long time5=System.currentTimeMillis();
            res=dStarLiteLabel.doDStarLiteLabel();
            long time6=System.currentTimeMillis();
            if(res<0) {
                System.out.println("dStarLiteLabel Crash");
                continue;
            }
            System.out.println("2");

            RepeatAStar repeatAStar = new RepeatAStar(tmp, floyd, start, end);
            long time7=System.currentTimeMillis();
            res=repeatAStar.dorepeatastar();
            long time8=System.currentTimeMillis();
            if(res<0){
                System.out.println("repeatAStar Crash");
                continue;
            }
            System.out.println("3");

            RepeatDijsktra repeatDijsktra=new RepeatDijsktra(tmp,start,end);
            long time1=System.currentTimeMillis();
            res=repeatDijsktra.doRepeatDijsktra();
            long time2=System.currentTimeMillis();
            if(res<0) {
                System.out.println("repeatDijsktra Crash");
                continue;
            }
            System.out.println("4");


            dijruntime+=(time2-time1);
            starruntime+=(time4-time3);
            starlabelruntime += (time6-time5);
            astarruntime += (time8-time7);
            count++;


//            for (int i = 0; i < repeatDijsktra.getpath().length; i++) {
//                System.out.println(repeatDijsktra.getpath()[i]);
//            }
//            System.out.println("============");
//            for (int i = 0; i < dijsktra.getpath(end).length; i++) {
//                System.out.println(dijsktra.getpath(end)[i]);
//            }
//            System.out.println("===============");
//            for (int i = 0; i < dStarLite.getpath().length; i++) {
//                System.out.println(dStarLite.getpath()[i]);
//            }
            System.out.println("===============");
            System.out.println("dStarLite: "+ dStarLite.getTimecount() + " " + dStarLite.getpathlength() );
            System.out.println("repeatDijsktra: "+repeatDijsktra.getTimecount() + " " + repeatDijsktra.getpathlength());
            System.out.println("dStarLiteLabel: "+dStarLiteLabel.getTimecount() + " " + dStarLiteLabel.getpathlength());
            System.out.println("repeatAStar: "+repeatAStar.getTimecount()+" "+repeatAStar.getpathlength());
            System.out.println("===============");

            startimecount+=dStarLite.getTimecount();
            stardistance+=dStarLite.getpathlength();
            starlabeltime+=dStarLiteLabel.getTimecount();
            starlabeldistance+=dStarLiteLabel.getpathlength();
            dijdistance+=repeatDijsktra.getpathlength();
            dijtime+=repeatDijsktra.getTimecount();
            astarruntime+=repeatAStar.getTimecount();
            astardistance+=repeatAStar.getpathlength();
            out.write(dStarLite.getTimecount()+" "+dStarLite.getpathlength()+" "
                    +dStarLiteLabel.getTimecount()+" "+dStarLiteLabel.getpathlength()+" "
                    +repeatDijsktra.getTimecount()+" "+repeatDijsktra.getpathlength()+" "
                    +repeatAStar.getTimecount()+" "+repeatAStar.getpathlength());
//            out.newLine();
//            System.out.println(dStarLite.getTimecount());
//            System.out.println(dStarLite.getpathlength());
//            System.out.println(repeatDijsktra.getTimecount());
//            System.out.println(repeatDijsktra.getpathlength());

        }
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("count: "+count);
        System.out.println("DIJ: "+dijdistance+" "+dijtime);
        System.out.println("DSTARLITE: "+stardistance+" "+startimecount);
        System.out.println("DSTARLITELABEL: "+starlabeldistance + " " + starlabeltime);
        System.out.println("ASTAR: "+astardistance + " " + astarruntime);
        System.out.println("RUNTIME: "+dijruntime+" "+starruntime + " " + starlabelruntime);
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

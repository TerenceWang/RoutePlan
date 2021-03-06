import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    /**
     * main function
     *
     */
    public static void main(String[] args) {
        Map m=new Map();
        m.readmap("data/nodenumber_900_edgenumber_3162.map");
        int [][]s=m.graph.getEdgeMatrix();
        int start=5;
        int end=300;


        Floyd floyd=new Floyd();
        floyd.dofloyd(s);
        System.out.println("Floyd OVER.");


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
            out = new BufferedWriter(new FileWriter("data/" + "1000-2-result-900map.txt",true));
            for (int ii= 0; ii < 1000; ++ii)
            {
                ArrayList<int[][]> tmp = new ArrayList<>();
                tmp=trafficFlow.generateTrafficFlow();
//            tmp=trafficFlow.generateCongestion();   /**It is a test method, we have not finish it yet!**/

                DStarLite dStarLite=new DStarLite(m,floyd,tmp,start,end);
                long time3=System.currentTimeMillis();
                int res=dStarLite.doDStarLite();
                long time4=System.currentTimeMillis();
                if(res<0) {
                    System.out.println("dStarLite Crash");
                    continue;
                }
//            System.out.println("1");

                DStarLiteLabel dStarLiteLabel=new DStarLiteLabel(m,floyd,tmp,start,end);
                long time5=System.currentTimeMillis();
                //res=dStarLiteLabel.doDStarLiteLabel();    /**In the real time test, the performance of DStarLite Label perform not so quite well**/
                long time6=System.currentTimeMillis();
                if(res<0) {
                    System.out.println("dStarLiteLabel Crash");
                    continue;
                }
//            System.out.println("2");

                RepeatAStar repeatAStar = new RepeatAStar(tmp, floyd, start, end);
                long time7=System.currentTimeMillis();
                res=repeatAStar.dorepeatastar();
                long time8=System.currentTimeMillis();
                if(res<0){
                    System.out.println("repeatAStar Crash");
                    continue;
                }
//            System.out.println("3");

                RepeatDijsktra repeatDijsktra=new RepeatDijsktra(tmp,start,end);
                long time1=System.currentTimeMillis();
                res=repeatDijsktra.doRepeatDijsktra();
                long time2=System.currentTimeMillis();
                if(res<0) {
                    System.out.println("repeatDijsktra Crash");
                    continue;
                }
//            System.out.println("4");


                dijruntime += (time2-time1);
                starruntime += (time4-time3);
                astarruntime += (time8-time7);
                count++;


                System.out.println("===============");
                System.out.println("dStarLite: "+ dStarLite.getTimecount() + " " + dStarLite.getpathlength() );
                System.out.println("repeatDijsktra: "+repeatDijsktra.getTimecount() + " " + repeatDijsktra.getpathlength());
                System.out.println("repeatAStar: "+repeatAStar.getTimecount()+" "+repeatAStar.getpathlength());
                System.out.println("===============");

                startimecount+=dStarLite.getTimecount();
                stardistance+=dStarLite.getpathlength();
                dijdistance+=repeatDijsktra.getpathlength();
                dijtime+=repeatDijsktra.getTimecount();
                astartime+=repeatAStar.getTimecount();
                astardistance+=repeatAStar.getpathlength();
                //write each steps result into file
                out.write(dStarLite.getTimecount()+" "+dStarLite.getpathlength()+" "
                        +repeatDijsktra.getTimecount()+" "+repeatDijsktra.getpathlength()+" "
                        +repeatAStar.getTimecount()+" "+repeatAStar.getpathlength());
                out.newLine();

            }
            System.out.println("count: "+count);
            System.out.println("DIJ: "+dijdistance+" "+dijtime);
            System.out.println("DSTARLITE: "+stardistance+" "+startimecount);
            System.out.println("ASTAR: "+astardistance + " " + astarruntime);
            System.out.println("RUNTIME: "+dijruntime+" "+starruntime + " " + starlabelruntime + " " +  astarruntime);
            //write the final count result
            out.write("count: "+count);
            out.newLine();
            out.write("DIJ: "+dijdistance+" "+dijtime);
            out.newLine();
            out.write("DSTARLITE: "+stardistance+" "+startimecount);
            out.newLine();
            out.write("DSTARLITELABEL: "+starlabeldistance + " " + starlabeltime);
            out.newLine();
            out.write("ASTAR: "+astardistance + " " + astarruntime);
            out.newLine();
            out.write("RUNTIME: "+dijruntime+" "+starruntime + " " + starlabelruntime + " " +  astarruntime);
            out.newLine();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        int start=9;
        int end=40;
        Map m=new Map();
        m.readmap("data/nodenumber_10_edgenumber_327.map");
        int [][]s=m.graph.getEdgeMatrix();
        Floyd fl=new Floyd();
        fl.dofloyd(s);
        TrafficFlow trafficFlow=new TrafficFlow(m);
        ArrayList<int[][]> tmp = new ArrayList<>();

        long a=0;
        long b=0;
        long c=0;
        int pathcount1=0;
        int pathcount2=0;
        int pathcount3=0;
        for (int i = 0; i < 30; i++) {

            tmp = trafficFlow.generateTrafficFlow();

            RepeatAStar repeatAStar = new RepeatAStar(tmp, fl, start, end);
            RepeatDijsktra repeatDijsktra = new RepeatDijsktra(tmp, start, end);
            DStarLite dStarLite=new DStarLite(m,fl,tmp,start,end);


            long time1=System.currentTimeMillis();
            repeatAStar.dorepeatastar();
            long time2=System.currentTimeMillis();

            pathcount1+=repeatAStar.getpathlength();
            a+=time2-time1;

            time1=System.currentTimeMillis();
            repeatDijsktra.doRepeatDijsktra();
            time2=System.currentTimeMillis();


            pathcount2+=repeatDijsktra.getpathlength();

            b+=time2-time1;

            time1=System.currentTimeMillis();
            dStarLite.doDStarLite();
            time2=System.currentTimeMillis();

            pathcount3+=dStarLite.getpathlength();

            c+=time2-time1;

        }
        System.out.println("astar: "+a+" "+pathcount1);
        System.out.println("dij: "+b+" "+pathcount2);
        System.out.println("dstar: "+c+" "+pathcount3);
//        Dijsktra di=new Dijsktra();
//        di.dodijsktra(s,start);
//        int []a = fl.getpath(start,end);
//        int []a2 = di.getpath(end);
//        Astar astar=new Astar(fl);
//        astar.doastar(s,start,end);
//        int result=0;
//        System.out.println(astar.getpathlength(end));
//        System.out.println(di.getpathlength(end));
//        System.out.println();

        System.out.println("Hello World!");
    }
}

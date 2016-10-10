import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        int start=9;
        int end=10;
        Map m=new Map();
        m.readmap("data/nodenumber_10_edgenumber_327.map");
        int [][]s=m.graph.getEdgeMatrix();
        Floyd fl=new Floyd();
        fl.dofloyd(s);
        TrafficFlow trafficFlow=new TrafficFlow(m);
        ArrayList<int[][]> tmp = new ArrayList<>();

        tmp=trafficFlow.generateTrafficFlow();

        RepeatAStar repeatAStar=new RepeatAStar(tmp,fl,start,end);
        RepeatDijsktra repeatDijsktra=new RepeatDijsktra(tmp,start,end);
        repeatAStar.dorepeatastar();
        System.out.println(repeatAStar.getpathlength());
        repeatDijsktra.doRepeatDijsktra();
        System.out.println(repeatDijsktra.getpathlength());
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

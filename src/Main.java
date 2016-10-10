public class Main {

    public static void main(String[] args) {
        int start=9;
        int end=30;
        Map m=new Map();
        m.readmap("data/nodenumber_2500_edgenumber_8907.map");
        int [][]s=m.graph.getEdgeMatrix();
//        Floyd fl=new Floyd();
//        fl.dofloyd(s);
        Dijsktra di=new Dijsktra();
        di.dodijsktra(s,start);
        int []a = fl.getpath(start,end);
        int []a2 = di.getpath(end);
        Astar astar=new Astar(fl, m);
        astar.doastar(s,start,end);
        int []ss = astar.getpath(start,end);
        int result=0;
//        for (int i = 0; i < ss.length; i++) {
//            System.out.print(ss[i]+" ");
//        }
        System.out.println();
        for (int i = 0; i < a2.length; i++) {
            System.out.print(a2[i]+" ");
        }
        System.out.println("Hello World!");
    }
}

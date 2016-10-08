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

        TrafficFlow trafficFlow=new TrafficFlow(m);
        ArrayList<int[][]> tmp = new ArrayList<>();
        tmp=trafficFlow.generateTrafficFlow();

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
        System.out.println(repeatDijsktra.getpathlength());
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

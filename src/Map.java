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
    public static ArrayList<double[]> generateegedweigh(Map m){
        int nodecount=m.graph.getEdgeMatrix().length;
        int k=5;
        ArrayList<double[]> result=new ArrayList<>();
        for (int i = 0; i < nodecount; i++) {
            ArrayList<Double> countlist=new ArrayList<Double>();
            int id=m.graph.getFirstNeighbor(i);
            if(id!=-1)
                countlist.add(peredgeweight(k));

            while(id!=-1){
                id=m.graph.getNextNeighbor(i,id);
                if(id!=-1)
                    countlist.add(peredgeweight(k));
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
        int end=96;


//        Dijsktra di=new Dijsktra();
//        di.dodijsktra(s,start);
//        di.getpath(end);
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

        ArrayList<double[]> tmp=new ArrayList<>();
        tmp=generateegedweigh(m);
        for (int i = 0; i < tmp.get(10).length; i++) {
            System.out.print(tmp.get(10)[i]+" ");
        }
        System.out.println();

    }
}

import java.io.*;

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
        int end=10;
        Dijsktra di=new Dijsktra();
        di.dodijsktra(s,start);
        System.out.println(di.getpath(end));
        System.out.println(di.getlength(end));

    }
}

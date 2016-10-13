
/**
 * Created by terence on 10/8/16.
 */
public class State implements Comparable, java.io.Serializable{
    State(int v,int first, int second){
        this.vertex=v;
        this.first=first;
        this.second=second;
    }
    public int vertex;
    public int first;
    public int second;

    public int compareTo(Object that)
    {
        //This is a modified version of the gt method
        State other = (State)that;
        if (first > other.first) return 1;
        else if (first < other.first) return -1;
        if (second > other.second) return 1;
        else if (second < other.second) return -1;
        return 0;
    }
    @Override
    public int hashCode()
    {
        return this.first + 34245*this.second;
    }

    @Override
    public boolean equals(Object aThat) {
        //check for self-comparison
        if ( this == aThat ) return true;

        if ( !(aThat instanceof State) ) return false;
        //Alternative to the above line :

        //cast to native object is now safe
        State that = (State)aThat;

        //now a proper field-by-field evaluation can be made
        if(this.vertex==that.vertex) return true;
        return false;

    }
}

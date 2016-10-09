
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

        //use instanceof instead of getClass here for two reasons
        //1. if need be, it can match any supertype, and not just one class;
        //2. it renders an explict check for "that == null" redundant, since
        //it does the check for null already - "null instanceof [type]" always
        //returns false. (See Effective Java by Joshua Bloch.)
        if ( !(aThat instanceof State) ) return false;
        //Alternative to the above line :
        //if ( aThat == null || aThat.getClass() != this.getClass() ) return false;

        //cast to native object is now safe
        State that = (State)aThat;

        //now a proper field-by-field evaluation can be made
//        if (this.first == that.first && this.second == that.second && this.vertex==that.vertex) return true;
        if(this.vertex==that.vertex) return true;
        return false;

    }
}

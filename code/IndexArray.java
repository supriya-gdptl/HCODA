public class IndexArray implements Comparable<IndexArray> {
    public double value;
    public String index;

    public IndexArray(){
    	
    }
    public int compareTo(IndexArray o) {
        IndexArray s = (IndexArray)o;
        if (this.value > s.value) {
            return 1;
        }
        if (this.value < s.value) {
            return -1;
        }
        return 0;
    }
}
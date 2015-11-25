import java.util.HashSet;

public class mostCV {
	int nodeIdx;
	int nodeDeg;
	HashSet<Integer> nodeNeighborExclude;
	
	public mostCV(){
		this.nodeIdx = -1;
		this.nodeDeg = Integer.MIN_VALUE;
		this.nodeNeighborExclude = new HashSet<Integer>();
	}
	
	public void updateMCV(int i, HashSet<Integer> j){
		this.nodeIdx = i;
		this.nodeNeighborExclude = j;
		this.nodeDeg = this.nodeNeighborExclude.size();
	}
}

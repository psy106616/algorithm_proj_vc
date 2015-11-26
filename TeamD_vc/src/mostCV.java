import java.util.HashSet;

public class mostCV {
	int nodeIdx;
	int nodeDeg;
	int oneDegNodeNum;
	HashSet<Integer> nodeNeighborExclude;
	
	public mostCV(){
		this.nodeIdx = -1;
		this.nodeDeg = Integer.MIN_VALUE;
		this.oneDegNodeNum = -1;
		this.nodeNeighborExclude = new HashSet<Integer>();
	}
	
	public void updateMCV(int i, HashSet<Integer> j){
		this.nodeIdx = i;
		this.nodeNeighborExclude = j;
		this.nodeDeg = this.nodeNeighborExclude.size();
	}
	
//	public void updateOneDegNum(int i){
//		if(i%2==1)
//			System.out.println("weird");
//		this.oneDegNodeNum = i/2;
//	}
}

import java.util.*;

public class mostConnectedVertex_temp {
	HashMap<Integer, Set<Integer>> mCV;
	int currentMV = -1;
	Set<Integer> currentMVEdges;
	int currentMVSize = Integer.MIN_VALUE;
	
	public mostConnectedVertex_temp(HashMap<Integer, Set<Integer>> graph_init){
		this.mCV = graph_init;
		for(Map.Entry<Integer, Set<Integer>> entry: this.mCV.entrySet()){
			if(entry.getValue().size() > this.currentMVSize){
				this.currentMV = entry.getKey();
				this.currentMVEdges = entry.getValue();
				this.currentMVSize = this.currentMVEdges.size();
			}
		}
	}
	
	/*
	public int getMV(){
		int res = this.currentMV;
		Set<Integer> resEdge = this.currentMVEdges;
		this.updateMV();
		return res;
	}
	*/
	
	public void updateMV(){
		Set<Integer> edgesToBeRemoved = this.currentMVEdges;
		edgesToBeRemoved.add(this.currentMV);
		
		this.currentMVSize = Integer.MIN_VALUE;
		for(Map.Entry<Integer, Set<Integer>> entry: this.mCV.entrySet()){
			if(entry.getKey()==currentMV){
				this.mCV.remove(entry.getKey());
			}
//			else{
//				Set<Integer> updateEdge = entry.getValue();
//				updateEdge.removeAll(edgesToBeRemoved);
//				if(!updateEdge.isEmpty()){
//					this.mCV.put(entry.getKey(), updateEdge);
//					if(updateEdge.size() > this.currentMVSize){
//						this.currentMV = entry.getKey();
//						this.currentMVEdges = entry.getValue();
//						this.currentMVSize = this.currentMVEdges.size();
//					}
//				}
//				else{
//					this.mCV.remove(entry.getKey());
//				}
//			}
		}
	}
}

import java.util.Set;


public class Approx {
	public Set<Integer> getVC_approx(Graph G){

		int nodeNum = G.numNodes;

		while(!isFinished(G)){
			int index = findMaxDegreeIndex(G);
			System.out.println(index);
			updateGraph(G, index);
		}
	}

	private boolean isFinished(Graph G){
		for(int i = 0; i < G.numNodes;i++){
			Set<Integer> temp = G.edgeMap.get(i+1);
			if(temp.size() > 0){
				return false;
			}
		}
		return true;
	}

	private int findMaxDegreeIndex(Graph G){
		int maxIndex = 1;
		for(int i = 1; i <= G.numNodes; i++){
			if(G.edgeMap.get(i).size()>G.edgeMap.get(maxIndex).size()){
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	private void updateGraph(Graph G, int index){
		Set<Integer> temp = G.edgeMap.get(index);
		temp.clear();
		
		for(int i = 1; i <= G.numNodes; i++){
			temp = G.edgeMap.get(i);
			if(temp.contains(index)){
				temp.remove(index);
			}
		}
		
	}
}

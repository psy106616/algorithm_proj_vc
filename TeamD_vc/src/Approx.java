import java.util.Set;


import java.util.*;

public class Approx {
	
	public Approx(){
		
	}
	
	public Set<Integer> getVC_approx(Graph G){

		Set<Integer> used = new HashSet<Integer>(G.usedVertex);

		while(!isFinished(G, used)){
			mostCV curMCV = G.getMCV(used);
			used.add(curMCV.nodeIdx);
			G.nodes.get(curMCV.nodeIdx).used = true;
			//System.out.println(index);
			//updateGraph(index, subGraph);
		}
		
		return used;
	}

	private boolean isFinished(Graph G, Set<Integer> used){
		for(Map.Entry<Integer, Set<Integer>> curEntry: G.edgeMap.entrySet()){
			if(used.contains(curEntry.getKey()))
				continue;
			HashSet<Integer> curEdge = new HashSet<Integer>(curEntry.getValue());
			curEdge.removeAll(used);
			if(!curEdge.isEmpty())
				return false;
		}
		return true;
	}

	private int findMaxDegreeIndex(HashMap<Integer, Set<Integer>> subGraph){
		int maxIndex = -1;
		int maxVal = Integer.MIN_VALUE;
		for(Map.Entry<Integer, Set<Integer>> curEntry: subGraph.entrySet()){
			if(curEntry.getValue().size()>maxVal){
				maxIndex = curEntry.getKey();
				maxVal = curEntry.getValue().size();
			}
		}
		return maxIndex;
	}

	private void updateGraph(int index, HashMap<Integer, Set<Integer>> subGraph){
		subGraph.get(index).clear();
		
		for(Map.Entry<Integer, Set<Integer>> curEntry: subGraph.entrySet()){
			curEntry.getValue().remove(index);
		}
		
	}
}

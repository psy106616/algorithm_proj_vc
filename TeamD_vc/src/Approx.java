import java.util.Set;


import java.util.*;

public class Approx {
	
	public Approx(){
		
	}
	
	public Set<Integer> getVC_approx(HashMap<Integer, Set<Integer>> subGraph){

		Set<Integer> used = new HashSet<Integer>();

		while(!isFinished(subGraph)){
			int index = findMaxDegreeIndex(subGraph);
			used.add(index);
			System.out.println(index);
			updateGraph(index, subGraph);
		}
		
		return used;
	}

	private boolean isFinished(HashMap<Integer, Set<Integer>> subGraph){
		for(Map.Entry<Integer, Set<Integer>> curEntry: subGraph.entrySet()){
			if(curEntry.getValue().size()>0)
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

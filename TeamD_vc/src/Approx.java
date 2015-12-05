import java.util.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Approx {
	
	String out1;
	String out2;
	PrintWriter o1;
	PrintWriter o2;
	
	public Approx(String outfile1, String outfile2) throws FileNotFoundException, UnsupportedEncodingException{
		this.out1 = outfile1;
		this.out2 = outfile2;
		this.o1 = new PrintWriter(this.out1, "UTF-8");
		this.o2 = new PrintWriter(this.out2, "UTF-8");
	}
	
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
	
	public Set<Integer> getVC_approx(Graph G, boolean isolated){

		Set<Integer> used = new HashSet<Integer>(G.usedVertex);
		long startT = System.currentTimeMillis();

		while(!isFinished(G, used)){
			mostCV curMCV = G.getMCV(used);
			used.add(curMCV.nodeIdx);
			G.nodes.get(curMCV.nodeIdx).used = true;
			//System.out.println(index);
			//updateGraph(index, subGraph);
		}
		
		long endT = System.currentTimeMillis();
		
		o2.printf("%.2f,%d\n", (endT-startT)/1000.0, used.size());
		
		o1.println(used.size());
		for(Integer i: used){
			o1.printf("%d,", i);
		}
		
		this.o1.close();
		this.o2.close();
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

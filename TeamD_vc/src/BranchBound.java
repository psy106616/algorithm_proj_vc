import java.util.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class BranchBound {
	
	int lowerBound;
	Set<Integer> bestVertexSet;
	boolean VCfound;
	long startTime;
	//String out1;
	String out2;
	//PrintWriter o1;
	PrintWriter o2;
	int cutoff;
	boolean timeUp;
	
	static int iterCnt = 0; 
	//mostConnectedVertex mCV;
	
	public BranchBound(Graph G, int t, PrintWriter outfile2) throws FileNotFoundException, UnsupportedEncodingException{
		this.startTime = System.currentTimeMillis();
		Approx init = new Approx();
		this.bestVertexSet = init.getVC_approx(G);
		this.lowerBound = this.bestVertexSet.size();
		VCfound = true;
		this.o2 = outfile2;
		timeUp = false;
		this.o2.printf("%.2f,%d\n", (System.currentTimeMillis()-startTime)/1000.0, this.bestVertexSet.size());
		this.cutoff = t;
	}
	
	public void getVC_BB(Graph G, int curMostConnected){
		
		if(((System.currentTimeMillis()-this.startTime)/1000.0)>=this.cutoff){
			//System.out.println((System.currentTimeMillis()-this.startTime)/1000.0);
			timeUp = true;
			return;
		}
		
		if(curMostConnected == -1 && G.visitedEdgesCnt == 2*G.numEdges){
			double solTime = (System.currentTimeMillis()-this.startTime)/1000.0;
			
			this.bestVertexSet = new HashSet<Integer>(G.usedVertex);
			this.lowerBound = this.bestVertexSet.size();
			this.VCfound = true;
			//System.out.println("New opt found with "+ this.lowerBound+" TimeLapse: "+solTime);
			this.o2.printf("%.2f,%d\n",solTime,this.bestVertexSet.size());
			
			return;
		}
		
		//two branch
		G.usedVertex.add(curMostConnected);
		G.unUsedVertex.remove(curMostConnected);

		Set<Integer> curNeighbor = new HashSet<Integer>();
		for(Integer idx: G.unCoveredEdge.get(curMostConnected)){
			curNeighbor.add(idx);
			G.unCoveredEdge.get(idx).remove(curMostConnected);
			if(G.unCoveredEdge.get(idx).size()==0)
				G.unCoveredEdge.remove(idx);
		}
		G.unCoveredEdge.remove(curMostConnected);
		
		G.visitedEdgesCnt += 2*curNeighbor.size();
		
		//check lower bound
		int nextMostConnected = G.getMCVidx();
		
		int currentBestLB1 = 0;
		
		if(nextMostConnected!=-1)
			currentBestLB1 = G.unCoveredEdge.get(nextMostConnected).size()>0?G.usedVertex.size() + (G.numEdges-G.visitedEdgesCnt/2+G.unCoveredEdge.get(nextMostConnected).size()-1)/G.unCoveredEdge.get(nextMostConnected).size():G.usedVertex.size();
		
		if(currentBestLB1 >= this.lowerBound){
			G.usedVertex.remove(curMostConnected);
			G.unUsedVertex.add(curMostConnected);
		}
		else{
			getVC_BB(G, nextMostConnected);
			
			if(timeUp==true)
				return;
			
			G.usedVertex.remove(curMostConnected);
			G.unUsedVertex.add(curMostConnected);
			
			if(curNeighbor.size()>2){
				G.usedVertex.addAll(curNeighbor);
				G.unUsedVertex.removeAll(curNeighbor);
				
				HashMap<Integer, Set<Integer>> extraEdgesCovered = new HashMap<Integer, Set<Integer>>();
				
				int otherEdgesCoveredNum = 0;
				
				for(Integer curNei: curNeighbor){
					if(G.unCoveredEdge.containsKey(curNei)){
						extraEdgesCovered.put(curNei, new HashSet<Integer>(G.unCoveredEdge.get(curNei)));
						otherEdgesCoveredNum += 2*G.unCoveredEdge.get(curNei).size();
					
						for(Integer neiNei: G.unCoveredEdge.get(curNei)){
							G.unCoveredEdge.get(neiNei).remove(curNei);
							if(G.unCoveredEdge.get(neiNei).size()==0)
								G.unCoveredEdge.remove(neiNei);
						}
						G.unCoveredEdge.remove(curNei);
					}
				}
				
				G.visitedEdgesCnt += otherEdgesCoveredNum;
				
				int nextMostConnected2 = G.getMCVidx();
				
				int currentBestLB2 = 0;
						
				if(nextMostConnected2!=-1)
					currentBestLB2 = G.unCoveredEdge.get(nextMostConnected2).size()>0?G.usedVertex.size() + (G.numEdges-G.visitedEdgesCnt/2+G.unCoveredEdge.get(nextMostConnected2).size()-1)/G.unCoveredEdge.get(nextMostConnected2).size():G.usedVertex.size();
				
				if(nextMostConnected2!=-1 && (nextMostConnected2 != nextMostConnected && currentBestLB2 < this.lowerBound)){
				//if(nextMostConnected2!=-1 && currentBestLB2 < this.lowerBound){
					getVC_BB(G, nextMostConnected2);
				}
				
				G.usedVertex.removeAll(curNeighbor);
				G.unUsedVertex.addAll(curNeighbor);
				
				//add back extra edges
				G.unCoveredEdge.putAll(extraEdgesCovered);
				
				for(Map.Entry<Integer, Set<Integer>> curEntry: extraEdgesCovered.entrySet()){
					for(Integer neiNei: curEntry.getValue()){
						if(!G.unCoveredEdge.containsKey(neiNei))
							G.unCoveredEdge.put(neiNei, new HashSet<Integer>());
						G.unCoveredEdge.get(neiNei).add(curEntry.getKey());
					}
				}
				
				G.visitedEdgesCnt -= otherEdgesCoveredNum;
				
			}

		}
		
		//add back between edges
		G.unCoveredEdge.put(curMostConnected, curNeighbor);
		
		for(Integer curN: curNeighbor){
			if(!G.unCoveredEdge.containsKey(curN))
				G.unCoveredEdge.put(curN, new HashSet<Integer>());
			G.unCoveredEdge.get(curN).add(curMostConnected);
		}
		
		G.visitedEdgesCnt -= 2*curNeighbor.size();
	}
}

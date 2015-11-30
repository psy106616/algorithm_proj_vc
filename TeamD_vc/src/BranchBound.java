import java.util.*;

public class BranchBound {
	
	int lowerBound;
	Set<Integer> bestVertexSet;
	boolean VCfound;
	//mostConnectedVertex mCV;
	
	public BranchBound(Graph G){
		this.lowerBound = 600;
		this.bestVertexSet = G.nodes;
		VCfound = false;
		//this.mCV = new mostConnectedVertex(G.edgeMap);
	}
	
	public void getVC_BB(Graph G){
		//System.out.println("Current # of vertex used: "+G.usedVertex.size()+". Covered edges: "+G.visitedEdges.size()/2+". VC found "+this.VCfound+". Cur opt: "+this.bestVertexSet.size());
		
		if(G.usedVertex.size() > this.lowerBound)
			return;

		if(G.visitedEdgesCnt==G.numEdges*2){
			this.bestVertexSet = new HashSet<Integer>(G.usedVertex);
			this.lowerBound = this.bestVertexSet.size();
			this.VCfound = true;
			System.out.println("New opt found with "+ this.lowerBound);
			return;
		}
		
		mostCV MCV = G.getMCV(G.usedVertex);
		if(MCV.nodeNeighborExclude.size()==0)
			return;
		
		//two branch
		G.usedVertex.add(MCV.nodeIdx);
		G.unUsedVertex.remove(MCV.nodeIdx);
		G.visitedEdgesCnt += 2*MCV.nodeNeighborExclude.size();
		
		
//		Set<Edge> edgeBetween = new HashSet<Edge>();
//		//for(Integer otherEnd: G.edgeMap.get(currentMCV)){
//		for(Integer otherEnd: MCV.nodeNeighborExclude){
//			edgeBetween.add(new Edge(MCV.nodeIdx, otherEnd));
//			edgeBetween.add(new Edge(otherEnd, MCV.nodeIdx));
//		}
//		G.visitedEdges.addAll(edgeBetween);
		
		//check lower bound
		mostCV nextBestCV1 = G.getMCV(G.usedVertex);
		int currentBestLB1 = nextBestCV1.nodeDeg>0?G.usedVertex.size() + (G.numEdges-G.visitedEdgesCnt/2+nextBestCV1.nodeDeg-1)/nextBestCV1.nodeDeg:G.usedVertex.size();
		if(currentBestLB1 >= this.lowerBound){
			G.usedVertex.remove(MCV.nodeIdx);
			G.unUsedVertex.add(MCV.nodeIdx);
		}
		else{
			getVC_BB(G);
			G.usedVertex.remove(MCV.nodeIdx);
			G.unUsedVertex.add(MCV.nodeIdx);
			//G.usedVertex.addAll(MCV.nodeNeighborExclude);
			//G.unUsedVertex.removeAll(MCV.nodeNeighborExclude);
			
			//Set<Edge> otherEdgesCovered = new HashSet<Edge>();
			int otherEdgesCoveredNum = 0;
//			for(Integer neighbor: MCV.nodeNeighborExclude){
//				Set<Integer> otherEdgeEnds = new HashSet<Integer>(G.edgeMap.get(neighbor));
//				otherEdgeEnds.remove(MCV.nodeIdx);
//				otherEdgeEnds.removeAll(G.usedVertex);
////				for(Integer neighborEnd: otherEdgeEnds){
////					otherEdgesCovered.add(new Edge(neighbor, neighborEnd));
////					otherEdgesCovered.add(new Edge(neighborEnd, neighbor));
////					
////				}
//				otherEdgesCoveredNum += otherEdgeEnds.size();
//				G.visitedEdgesCnt += 2*otherEdgeEnds.size();
//			}
			
			for(Integer neighbor: MCV.nodeNeighborExclude){
				G.usedVertex.add(neighbor);
				G.unUsedVertex.remove(neighbor);
				Set<Integer> curNodeNeighbor = new HashSet<Integer>(G.edgeMap.get(neighbor));
				curNodeNeighbor.remove(MCV.nodeIdx);
				curNodeNeighbor.removeAll(G.usedVertex);
				G.visitedEdgesCnt += 2*curNodeNeighbor.size();
				otherEdgesCoveredNum += curNodeNeighbor.size();
			}
			
			mostCV nextBestCV2 = G.getMCV(G.usedVertex);
			int currentBestLB2 = nextBestCV2.nodeDeg>0?G.usedVertex.size() + (G.numEdges-G.visitedEdgesCnt/2+nextBestCV2.nodeDeg-1)/nextBestCV2.nodeDeg:G.usedVertex.size();
			
			if(nextBestCV2.nodeIdx != MCV.nodeIdx && currentBestLB2 < this.lowerBound){
				//G.visitedEdges.addAll(otherEdgesCovered);
				getVC_BB(G);
				//G.visitedEdges.removeAll(otherEdgesCovered);
			}
			
			G.visitedEdgesCnt -= 2*otherEdgesCoveredNum;
			G.usedVertex.removeAll(MCV.nodeNeighborExclude);
			G.unUsedVertex.addAll(MCV.nodeNeighborExclude);
		}
		//G.visitedEdges.removeAll(edgeBetween);
		G.visitedEdgesCnt -= 2*MCV.nodeNeighborExclude.size();
	}
}

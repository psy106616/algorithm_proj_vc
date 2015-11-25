import java.util.*;

public class BranchBound {
	
	int lowerBound;
	Set<Integer> bestVertexSet;
	boolean VCfound;
	//mostConnectedVertex mCV;
	
	public BranchBound(Graph G){
		this.lowerBound = G.numNodes;
		this.bestVertexSet = G.nodes;
		VCfound = false;
		//this.mCV = new mostConnectedVertex(G.edgeMap);
	}
	
	public void getVC_BB(Graph G){
		//System.out.println("Current # of vertex used: "+G.usedVertex.size()+". Covered edges: "+G.visitedEdges.size()/2+". VC found "+this.VCfound+". Cur opt: "+this.bestVertexSet.size());
		
		if(this.lowerBound <= G.usedVertex.size())
			return;
		if(G.visitedEdges.size()==G.edges.size()){
			this.bestVertexSet = new HashSet<Integer>(G.usedVertex);
			this.lowerBound = this.bestVertexSet.size();
			this.VCfound = true;
			System.out.println("New opt found with "+ this.lowerBound);
			return;
		}
		
		//int currentMCV = G.getMCV(G.usedVertex);
		//Set<Integer> MCVNeighbor = new HashSet<Integer>(G.edgeMap.get(currentMCV));
		mostCV MCV = G.getMCV(G.usedVertex);
		if(MCV.nodeNeighborExclude.size()==0)
			return;
		//MCV.nodeNeighborExclude.removeAll(G.usedVertex);
		
		//two branch
		G.usedVertex.add(MCV.nodeIdx);
		Set<Edge> edgeBetween = new HashSet<Edge>();
		//for(Integer otherEnd: G.edgeMap.get(currentMCV)){
		for(Integer otherEnd: MCV.nodeNeighborExclude){
			edgeBetween.add(new Edge(MCV.nodeIdx, otherEnd));
			edgeBetween.add(new Edge(otherEnd, MCV.nodeIdx));
		}
		G.visitedEdges.addAll(edgeBetween);
		getVC_BB(G);
		
		G.usedVertex.remove(MCV.nodeIdx);
		//G.visitedEdges.removeAll(edgeBetween);
		if(MCV.nodeNeighborExclude.size()>1){
			G.usedVertex.addAll(MCV.nodeNeighborExclude);
			Set<Edge> otherEdgesCovered = new HashSet<Edge>();
			for(Integer neighbor: MCV.nodeNeighborExclude){
				Set<Integer> otherEdgeEnds = new HashSet<Integer>(G.edgeMap.get(neighbor));
				otherEdgeEnds.remove(MCV.nodeIdx);
				otherEdgeEnds.removeAll(G.usedVertex);
				for(Integer neighborEnd: otherEdgeEnds){
					otherEdgesCovered.add(new Edge(neighbor, neighborEnd));
					otherEdgesCovered.add(new Edge(neighborEnd, neighbor));
				}
			}
			G.visitedEdges.addAll(otherEdgesCovered);
			//G.usedVertex.addAll(G.edgeMap.get(currentMCV));	//select all connected nodes rather than itself 
			getVC_BB(G);
			
			G.usedVertex.removeAll(MCV.nodeNeighborExclude);
			G.visitedEdges.removeAll(otherEdgesCovered);
		}
		
		G.visitedEdges.removeAll(edgeBetween);
	}
}

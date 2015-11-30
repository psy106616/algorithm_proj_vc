import java.util.*;

public class Graph {
	int numNodes = 0;
	int numEdges = 0;
	int visitedEdgesCnt = 0;
	Set<Integer> nodes = new HashSet<Integer>();
	Set<Edge> edges = new HashSet<Edge>();
	Set<Edge> visitedEdges = new HashSet<Edge>();
	Set<Integer> usedVertex = new HashSet<Integer>();
	Set<Integer> unUsedVertex = new HashSet<Integer>();
	
	Set<Integer> solution = new HashSet<Integer>();
	
	int[] nodeDegree;
	
	//HashMap<Integer, Set<Integer>> nodeAdj = new HashMap<Integer, Set<Integer>>();
	HashMap<Integer, Set<Integer>> edgeMap = new HashMap<Integer, Set<Integer>>();
	HashMap<Integer, ArrayList<Integer>> edgeMap2 = new HashMap<Integer, ArrayList<Integer>>();
	
	PriorityQueue<int[]> mostConnectedNodes = new PriorityQueue<int[]>(10, new myComp());
	
	public Graph(){
		
	}
	
	public Graph(int n, int e){
		this.numNodes = n;
		this.numEdges = e;
		this.nodeDegree = new int[n+1];
		
		for(int i=1; i<=n; i++){
			//this.nodeAdj.put(i, new HashSet<Integer>());
			this.edgeMap.put(i, new HashSet<Integer>());
			this.edgeMap2.put(i, new ArrayList<Integer>());
			this.nodes.add(i);
		}
		
		int[] s = {41, 30, 56, 11, 80, 28, 53, 55, 45, 104, 74, 47, 101, 10, 12, 68, 52, 109, 88, 71, 17, 29, 54, 77, 110, 93, 39, 7, 16, 81, 9, 40, 27, 31, 91, 57, 100, 44, 70, 108, 8, 66, 94, 59, 38, 25, 5, 3, 113, 42, 19, 48, 99, 75, 22, 24, 78, 64, 46, 26, 34, 32, 63, 95, 73, 111, 90, 87, 18, 35, 115, 4, 84, 82, 13, 72, 14, 2, 21, 114, 15, 20, 60, 1, 69, 79, 65, 36, 107, 49, 58, 6, 50, 67};
		for(Integer i: s){
			this.solution.add(i);
		}
		
		this.unUsedVertex = this.nodes;
	}
	
	public int addEdge(int from, int to){
		// add in edges, only store upper triangular of the adjacency matrix
		if(from > this.numNodes || to > this.numNodes)
			return 0;
		
		this.edgeMap.get(from).add(to);
		this.edgeMap.get(to).add(from);
		this.edges.add(new Edge(from, to));
		this.edges.add(new Edge(to, from));
		this.edgeMap2.get(from).add(to);
		//this.edgeMap2.get(to).add(from);
		
//		if(from > to){
//			int temp = from;
//			from = to;
//			to = temp;
//		}
		
		//if(this.nodeAdj.get(from).contains(to)){
		//	return 0;
		//}
		//else{
		//	this.nodeAdj.get(from).add(to);
			this.nodeDegree[from]+=1;
			//this.nodeDegree[to]+=1;
			return 1;
		//}
	}
	
//	public boolean checkVC(Set<Integer> nodeSet){
//		int numEdgesVisited = 0;
//		
//		for(int i=1; i<=this.numNodes; i++){
//			if(this.nodeAdj.get(i).size()==0)
//				continue;
//			else if(nodeSet.contains(i)){
//				numEdgesVisited += this.nodeAdj.get(i).size();
//			}
//			else{
//				for(Integer n: this.nodeAdj.get(i)){
//					if(nodeSet.contains(n))
//						numEdgesVisited++;
//				}
//			}
//		}
//		
//		return numEdgesVisited==this.numEdges;
//	}
	
//	public boolean checkEdgeVisited(){
//		return true;
//	}
//	
//	public void updateMostConnectedNodes(){
//		if(this.numNodes==0)
//			return;
//		for(int i=1; i<=this.nodeDegree.length; i++){
//			int[] temp = new int[2];
//			temp[0] = i;
//			temp[1] = this.nodeDegree[i-1];
//			this.mostConnectedNodes.add(temp);
//		}
//	}
	
	public mostCV getMCV(Set<Integer> excludeNodes){
		mostCV MCV = new mostCV();
		for(Map.Entry<Integer, Set<Integer>> entry: this.edgeMap.entrySet()){
			if(!excludeNodes.contains(entry.getKey())){
				HashSet<Integer> currentEdges = new HashSet<Integer>(entry.getValue());
				currentEdges.removeAll(excludeNodes);
				int curSize = currentEdges.size();
				if(curSize > MCV.nodeDeg){
					//MCVSize = currentEdges.size();
					//MCV = entry.getKey();
					MCV.updateMCV(entry.getKey(), currentEdges);
				}
			}
		}
		//MCV.updateOneDegNum(oneCnt);
		return MCV;
	}
	
	public void getIsland(){
		for(int i=1; i<this.nodeDegree.length; i++){
			if(this.nodeDegree[i]==0){
				this.unUsedVertex.remove(i);
			}
			else if(!this.unUsedVertex.contains(i))
				continue;
			else if(this.nodeDegree[i]==1 && this.edgeMap2.get(this.edgeMap2.get(i).get(0)).size() == 1){
				this.usedVertex.add(i);
				this.unUsedVertex.remove(i);
				this.unUsedVertex.remove(this.edgeMap2.get(i).get(0));
				this.visitedEdgesCnt += 2;
			}
		}
	}
	
	public int getCurrentDeg(int node, Set<Integer> excludeNodes){
		HashSet<Integer> nodeEdges = new HashSet<Integer>(this.edgeMap.get(node));
		nodeEdges.removeAll(excludeNodes);
		return nodeEdges.size();
	}
}

import java.util.*;

public class Graph {
	int numNodes = 0;
	int numEdges = 0;
	int visitedEdgesCnt = 0;
	Set<Integer> nodes = new HashSet<Integer>();
	Set<Edge> edges = new HashSet<Edge>();
	Set<Edge> visitedEdges = new HashSet<Edge>();
	Set<Integer> usedVertex = new HashSet<Integer>();
	
	Set<Integer> solution = new HashSet<Integer>();
	
	int[] nodeDegree;
	
	HashMap<Integer, Set<Integer>> nodeAdj = new HashMap<Integer, Set<Integer>>();
	HashMap<Integer, Set<Integer>> edgeMap = new HashMap<Integer, Set<Integer>>();
	
	PriorityQueue<int[]> mostConnectedNodes = new PriorityQueue<int[]>(10, new myComp());
	
	public Graph(){
		
	}
	
	public Graph(int n, int e){
		this.numNodes = n;
		this.numEdges = e;
		this.nodeDegree = new int[n];
		
		for(int i=1; i<=n; i++){
			this.nodeAdj.put(i, new HashSet<Integer>());
			this.edgeMap.put(i, new HashSet<Integer>());
			this.nodes.add(i);
		}
		
		int[] s = {1,4,5,7,8,9,10,11,12,13,14,15,16,19,24,25,26,28,29,31,32,33,34,35,38,39,40,42,43,44,45,49,50,51,53,54,55,56,57,58,59,60,61,62,63,64,65,67,69,70,71,72,73,75,76,77,78,80,81,83,86,87,88,90,91,92,93,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,114,115,116,118,119,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,146,147,149,150,152,153,154,155,156,157,158,159,161,162,163,164,166,167,168,169,170,171,172,174,175,176,177,178,179,180,182,183,184,185,187,189,190,191,192,193,194,195,196,197};
		for(Integer i: s){
			this.solution.add(i);
		}
	}
	
	public int addEdge(int from, int to){
		// add in edges, only store upper triangular of the adjacency matrix
		if(from > this.numNodes || to > this.numNodes)
			return 0;
		
		this.edgeMap.get(from).add(to);
		this.edgeMap.get(to).add(from);
		this.edges.add(new Edge(from, to));
		this.edges.add(new Edge(to, from));
		
		if(from > to){
			int temp = from;
			from = to;
			to = temp;
		}
		
		if(this.nodeAdj.get(from).contains(to)){
			return 0;
		}
		else{
			this.nodeAdj.get(from).add(to);
			this.nodeDegree[from-1]+=1;
			this.nodeDegree[to-1]+=1;
			return 1;
		}
	}
	
	public boolean checkVC(Set<Integer> nodeSet){
		int numEdgesVisited = 0;
		
		for(int i=1; i<=this.numNodes; i++){
			if(this.nodeAdj.get(i).size()==0)
				continue;
			else if(nodeSet.contains(i)){
				numEdgesVisited += this.nodeAdj.get(i).size();
			}
			else{
				for(Integer n: this.nodeAdj.get(i)){
					if(nodeSet.contains(n))
						numEdgesVisited++;
				}
			}
		}
		
		return numEdgesVisited==this.numEdges;
	}
	
	public boolean checkEdgeVisited(){
		return true;
	}
	
	public void updateMostConnectedNodes(){
		if(this.numNodes==0)
			return;
		for(int i=1; i<=this.nodeDegree.length; i++){
			int[] temp = new int[2];
			temp[0] = i;
			temp[1] = this.nodeDegree[i-1];
			this.mostConnectedNodes.add(temp);
		}
	}
	
	public mostCV getMCV(Set<Integer> excludeNodes){
		//int MCV = -1;
		//int MCVSize = Integer.MIN_VALUE;
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
	
	public int getCurrentDeg(int node, Set<Integer> excludeNodes){
		HashSet<Integer> nodeEdges = new HashSet<Integer>(this.edgeMap.get(node));
		nodeEdges.removeAll(excludeNodes);
		return nodeEdges.size();
	}
}

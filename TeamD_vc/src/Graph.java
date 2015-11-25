import java.util.*;

public class Graph {
	int numNodes = 0;
	int numEdges = 0;
	Set<Integer> nodes = new HashSet<Integer>();
	Set<Edge> edges = new HashSet<Edge>();
	Set<Edge> visitedEdges = new HashSet<Edge>();
	Set<Integer> usedVertex = new HashSet<Integer>();
	
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
				if(currentEdges.size()>MCV.nodeDeg){
					//MCVSize = currentEdges.size();
					//MCV = entry.getKey();
					MCV.updateMCV(entry.getKey(), currentEdges);
				}
			}
		}
		return MCV;
	}
}

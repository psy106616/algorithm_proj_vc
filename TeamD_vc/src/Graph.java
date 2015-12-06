import java.util.*;

public class Graph {
	int numNodes = 0;
	int numEdges = 0;
	int visitedEdgesCnt = 0;
	int mostConnected;
	
	ArrayList<Node> nodes = new ArrayList<Node>();
	Set<Edge> edges = new HashSet<Edge>();
	Set<Integer> usedVertex = new HashSet<Integer>();
	Set<Integer> unUsedVertex = new HashSet<Integer>();
	
//	Set<Integer> solution = new HashSet<Integer>();
	
	HashMap<Integer, Set<Integer>> edgeMap = new HashMap<Integer, Set<Integer>>();
	HashMap<Integer, Set<Integer>> unCoveredEdge = new HashMap<Integer, Set<Integer>>();
	
	public Graph(){
		
	}
	
	public Graph(int n, int e){
		this.numNodes = n;
		this.numEdges = e;
		
		this.nodes.add(new Node());
		
		for(int i=1; i<=n; i++){
			this.edgeMap.put(i, new HashSet<Integer>());
			this.unCoveredEdge.put(i, new HashSet<Integer>());
			this.nodes.add(new Node(i));
			this.unUsedVertex.add(i);
		}
		
//		int[] s = {41, 30, 56, 11, 80, 28, 53, 55, 45, 104, 74, 47, 101, 10, 12, 68, 52, 109, 88, 71, 17, 29, 54, 77, 110, 93, 39, 7, 16, 81, 9, 40, 27, 31, 91, 57, 100, 44, 70, 108, 8, 66, 94, 59, 38, 25, 5, 3, 113, 42, 19, 48, 99, 75, 22, 24, 78, 64, 46, 26, 34, 32, 63, 95, 73, 111, 90, 87, 18, 35, 115, 4, 84, 82, 13, 72, 14, 2, 21, 114, 15, 20, 60, 1, 69, 79, 65, 36, 107, 49, 58, 6, 50, 67};
//		for(Integer i: s){
//			this.solution.add(i);
//		}
		
	}
	
	public int addEdge(int from, int to){
		// add in edges, only store upper triangular of the adjacency matrix
		if(from > this.numNodes || to > this.numNodes)
			return 0;
		
		this.edgeMap.get(from).add(to);
		this.edgeMap.get(to).add(from);
		
		this.unCoveredEdge.get(from).add(to);
		this.unCoveredEdge.get(to).add(from);

		this.nodes.get(from).deg++;
		return 1;
	}
	
	public mostCV getMCV(Set<Integer> excludeNodes){
		mostCV MCV = new mostCV();
		for(Map.Entry<Integer, Set<Integer>> entry: this.edgeMap.entrySet()){
			if(!excludeNodes.contains(entry.getKey())){
				HashSet<Integer> currentEdges = new HashSet<Integer>(entry.getValue());
				currentEdges.removeAll(excludeNodes);
				int curSize = currentEdges.size();
				if(curSize > MCV.nodeDeg){
					MCV.updateMCV(entry.getKey(), currentEdges);
				}
			}
		}
		return MCV;
	}
	
	public boolean isVC(Set<Integer> s){
		for(Map.Entry<Integer, Set<Integer>> curEntry: this.edgeMap.entrySet()){
			if(s.contains(curEntry.getKey())){
				continue;
			}
			else{
				for(Integer n: curEntry.getValue()){
					if(!s.contains(n))
						return false;
				}
			}
		}
		return true;
	}
	
	public boolean isVC(Set<Integer> s, int node){
		boolean res;
		
		if(this.nodes.get(node).used==true){
			s.remove(node);
			res = isVC(s);
			s.add(node);
		}
		else{
			s.add(node);
			res = isVC(s);
			s.remove(node);
		}
		
		return res;
	}
	
	public int getMCVidx(){
		int res = -1;
		int cnt = 0;
		for(Map.Entry<Integer, Set<Integer>> curEntry: this.unCoveredEdge.entrySet()){
			if(curEntry.getValue().size()>cnt){
				res = curEntry.getKey();
				cnt = curEntry.getValue().size();
			}
		}
		return res;
	}
	
	public void updateMostConnected(){
		this.mostConnected = this.getMCVidx();
	}
}

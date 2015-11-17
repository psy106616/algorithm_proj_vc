import java.util.*;

public class Graph {
	int numNodes = 0;
	int numEdges = 0;
	Set<Integer> nodes = new HashSet<Integer>();
	int[] nodeDegree;
	HashMap<Integer, Set<Integer>> nodeAdj = new HashMap<Integer, Set<Integer>>();
	PriorityQueue<int[]> mostConnectedNodes = new PriorityQueue<int[]>(){
		public int compare(int[] N1, int[] N2){
			return N2[1]-N1[1];
		}
	};
	
	public Graph(){
		
	}
	
	public Graph(int n, int e){
		this.numNodes = n;
		this.numEdges = e;
		this.nodeDegree = new int[n];
		
		for(int i=1; i<=n; i++){
			this.nodeAdj.put(i, new HashSet<Integer>());
			this.nodes.add(i);
		}
	}
	
	public int addEdge(int from, int to){
		// add in edges, only store upper triangular of the adjacency matrix
		if(from > this.numNodes || to > this.numNodes)
			return 0;
		
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
}

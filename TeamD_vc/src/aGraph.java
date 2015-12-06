import java.util.*;

public class aGraph {
	int numNodes = 0;
	int numEdges = 0;
	HashMap<Integer, Set<Integer>> Nodes = new HashMap<Integer, Set<Integer>>();
	PriorityQueue<int[]> mostConnectedNodes = new PriorityQueue<int[]>(){
		public int compare(int[] N1, int[] N2){
			return N2[1]-N1[1];
		}
	};
	
	public aGraph(){
		
	}
	
	public aGraph(int n, int e){
		this.numNodes = n;
		this.numEdges = e;
		
		for(int i=1; i<=n; i++){
			Nodes.put(i, new HashSet<Integer>());
		}
	}
	
	public void addEdge(int from, int to){
		// add in edges, only store upper triangular of the adjacency matrix
		if(from > this.numNodes || to > this.numNodes)
			return;
		
		if(from > to){
			int temp = from;
			from = to;
			to = temp;
		}
		
		if(this.Nodes.get(from).contains(to)){
			// return 0;
		}
		else{
			this.Nodes.get(from).add(to);
			// return 1;
		}
	}
	
	public boolean checkVC(Set<Integer> nodeSet){
		int numEdgesVisited = 0;
		
		for(int i=1; i<=this.numNodes; i++){
			if(this.Nodes.get(i).size()==0)
				continue;
			else if(nodeSet.contains(i)){
				numEdgesVisited += this.Nodes.get(i).size();
			}
			else{
				for(Integer n: this.Nodes.get(i)){
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
		for(Integer key: this.Nodes.keySet()){
			int[] temp = new int[2];
			temp[0] = key;
			temp[1] = Nodes.get(key).size();
			this.mostConnectedNodes.add(temp);
		}
	}
}

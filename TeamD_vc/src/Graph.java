import java.util.*;

public class Graph {
	int numNodes = 0;
	int numEdges = 0;
	HashMap<Integer, Set<Integer>> Nodes = new HashMap<Integer, Set<Integer>>();
	
	public Graph(){
		
	}
	
	public Graph(int n, int e){
		this.numNodes = n;
		this.numEdges = e;
		
		for(int i=1; i<=n; i++){
			Nodes.put(i, new HashSet<Integer>());
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
		
		if(this.Nodes.get(from).contains(to)){
			return 0;
		}
		else{
			this.Nodes.get(from).add(to);
			return 1;
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
}

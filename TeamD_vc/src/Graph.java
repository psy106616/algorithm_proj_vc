import java.util.*;

public class Graph {
	int numNodes = 0;
	int numEdges = 0;
	HashMap<Integer, ArrayList<Integer>> Nodes = new HashMap<Integer, ArrayList<Integer>>();
	
	public Graph(){
		
	}
	
	public Graph(int n, int e){
		this.numNodes = n;
		this.numEdges = e;
	}
	
	public void addEdge(int from, int to){
		
	}
	
	public boolean checkVC(ArrayList<Integer> set){
		return true;
	}
	
	public boolean checkEdgeVisited(){
		return true;
	}
}

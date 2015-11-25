import java.util.*;
import java.io.*;

public class Approximation{
	public static void main(String [] args){
		try{
			String inFileName = "football.graph";
			//input file parse
			Graph G = parseInput(inFileName);

			int nodeNum = G.nodeAdj.size();
			

			while(!isFinished(G)){
				int index = findMaxDegreeIndex(G);
				System.out.println(index);
				updateGraph(G, index);
			}

		}
		catch(IOException e){
			System.err.println("Read File Error");
		}
		

	}

	public static Graph parseInput(String inFileName) throws IOException {
		BufferedReader fcont = new BufferedReader(new FileReader(inFileName));
		
		String[] firstLine = fcont.readLine().split(" ");
		int nodeCnt = Integer.parseInt(firstLine[0]);
		Graph G = new Graph(nodeCnt, Integer.parseInt(firstLine[1]));
		
		int lineCnt = 0;
		String line;
		while((line = fcont.readLine())!=null){
			
			lineCnt++;
			String[] curLine = line.split(" ");
			
			//add edges
			for(int i=0; i<curLine.length; i++){
				G.addEdge(lineCnt, Integer.parseInt(curLine[i]));
			}
		}
		fcont.close();
		return G;
	}

	private static boolean isFinished(Graph G){
		for(int i = 0; i < G.nodeAdj.size();i++){
			Set temp = G.nodeAdj.get(i+1);
			if(temp.size() > 0){
				return false;
			}
		}
		return true;
	}

	private static int findMaxDegreeIndex(Graph G){
		int maxIndex = 1;
		for(int i = 1; i <= G.nodeAdj.size(); i++){
			if(G.nodeAdj.get(i).size()>G.nodeAdj.get(maxIndex).size()){
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	private static void updateGraph(Graph G, int index){
		Set temp = G.nodeAdj.get(index);
		temp.clear();
		
		for(int i = 1; i <= G.nodeAdj.size(); i++){
			temp = G.nodeAdj.get(i);
			if(temp.contains(index)){
				temp.remove(index);
			}
		}
		
	}

	
}
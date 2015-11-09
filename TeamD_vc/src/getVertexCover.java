import java.util.*;
import java.io.*;

public class getVertexCover {
	static String USAGE = "Usage:";

	public static void main(String[] args) throws IOException {
		
		//check input correct
//		if(args.length != 5){
//			System.out.println("Input Error");
//			System.out.println(USAGE);
//			System.exit(1);
//		}
//		String inFileName = args[0];
//		String outFileName = args[1];
//		String algPick = args[2];
//		String allowedTime = args[3];
		
		//test use
		String inFileName = "as-22july06.graph";
		String outFileName = "testout.txt";
		String algPick = "bb";
		
		if(!algPick.equalsIgnoreCase("bb") && !algPick.equalsIgnoreCase("local") && !algPick.equalsIgnoreCase("approx")){
			System.out.println("Algorithm not recognized");
			System.out.println(USAGE);
			System.exit(1);
		}
		
		PrintWriter output = new PrintWriter(outFileName, "UTF-8");
		
		//input file parse
		Graph G = parseInput(inFileName);
		
		Set<Integer> VCset = new HashSet<Integer>();
		
		if(algPick.equalsIgnoreCase("bb")){
			//using branch and bound
			BranchBound bb = new BranchBound();
			VCset = bb.getVC_BB(G);
		}
		else if(algPick.equalsIgnoreCase("approx")){
			//using approximation
			
		}
		else{
			//using local search
		}
		
		output.close();
	}
	
	public static Graph parseInput(String inFileName) throws IOException {
		BufferedReader fcont = new BufferedReader(new FileReader(inFileName));
		
		String[] firstLine = fcont.readLine().split(" ");
		int nodeCnt = Integer.parseInt(firstLine[0]);
		Graph G = new Graph(nodeCnt, Integer.parseInt(firstLine[1]));
		
		int edgeCnt = 0;
		int lineCnt = 0;
		String line;
		while((line = fcont.readLine())!=null){
			lineCnt++;
			String[] curLine = line.split(" ");
			
			//add edges
			for(int i=0; i<curLine.length; i++){
				edgeCnt += G.addEdge(lineCnt, Integer.parseInt(curLine[i]));
			}
		}
		
		if(edgeCnt != G.numEdges){
			System.out.println("Only "+edgeCnt+" edges detected! Updating");
			G.numEdges = edgeCnt;
		}
		
		fcont.close();
		return G;
	}

}

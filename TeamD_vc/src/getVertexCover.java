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
		String inFileName = "jazz.graph";
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
		
		Set<Integer> VCset;
		
		long startTime = System.currentTimeMillis();

		if(algPick.equalsIgnoreCase("bb")){
			//using branch and bound
			BranchBound bb = new BranchBound(G);
			bb.getVC_BB(G);
			VCset = bb.bestVertexSet;
		}
		else if(algPick.equalsIgnoreCase("approx")){
			//using approximation
			VCset = new HashSet<Integer>();
		}
		else{
			//using local search
			VCset = new HashSet<Integer>();
		}
		
		long endTime = System.currentTimeMillis();
		
		output.close();
		System.out.println("Best set found with "+VCset.size()+" of vertices: ");
		for(Integer v: VCset){
			System.out.printf("%d,",v);
		}
		System.out.println();
		System.out.println("Run time in seconds: " + (endTime-startTime)/1e3);
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
			if(line.equals(""))
				break;
			lineCnt++;
			String[] curLine = line.trim().split(" ");
			
			//add edges
			for(int i=0; i<curLine.length; i++){
				if(lineCnt==Integer.parseInt(curLine[i]))
					System.out.println("Self edge detected");
				edgeCnt += G.addEdge(lineCnt, Integer.parseInt(curLine[i]));
			}
		}
		
		if(edgeCnt != G.numEdges){
			System.out.println("Only "+edgeCnt+" edges detected! Updating");
			G.numEdges = edgeCnt;
		}
		
		G.updateMostConnectedNodes();
		fcont.close();
		return G;
	}

}

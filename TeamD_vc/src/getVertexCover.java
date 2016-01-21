import java.util.*;
import java.io.*;

public class getVertexCover {
	static String USAGE = "Usage: java getVertexCover <inputFileName> <method: bb, sa, fast, approx> <cut off time in seconds> [<randSeed>]";

	public static void main(String[] args) throws IOException {
		
		//check input correct
		if(args.length >= 4 && args.length <=2){
			System.out.println("Input Error");
			System.out.println(USAGE);
			System.exit(1);
		}
		String inFileName = args[0];
		String outFileName = inFileName+".sol";
		String algPick = args[1];
		int allowedTime = Integer.valueOf(args[2]);
		int randSeed;
		if(args.length==4){
			randSeed = Integer.valueOf(args[3]);	//only simulated annealing will use random seed
		}
		else{
			randSeed = 100;
		}
		String outFileName1; 
		String outFileName2;
		
		//test use
//		String inFileName = "power.graph";
//		String outFileName1;
//		String outFileName2;
//		String algPick = "sa";
//		int randSeed = 100;
//		int allowedTime = 60;
		
				
		if(!algPick.equalsIgnoreCase("bb") && !algPick.equalsIgnoreCase("sa") && !algPick.equalsIgnoreCase("fast") && !algPick.equalsIgnoreCase("approx")){
			System.out.println("Algorithm not recognized");
			System.out.println(USAGE);
			System.exit(1);
		}
		
		//PrintWriter output = new PrintWriter(outFileName, "UTF-8");
		
		//input file parse
		Graph G = parseInput(inFileName);
		
		Set<Integer> VCset;
		
		long startTime = System.currentTimeMillis();

		if(algPick.equalsIgnoreCase("bb")){
			//using branch and bound
			outFileName1 = inFileName+"_BnB_"+allowedTime+".sol";
			outFileName2 = inFileName+"_BnB_"+allowedTime+".trace";
			PrintWriter o1 = new PrintWriter(outFileName1, "UTF-8");
			PrintWriter o2 = new PrintWriter(outFileName2, "UTF-8");
			BranchBound bb = new BranchBound(G, allowedTime, o2);
			bb.getVC_BB(G, G.mostConnected);
			o1.println(bb.bestVertexSet.size());
			for(Integer i: bb.bestVertexSet){
				o1.printf("%d,", i);
			}
			o1.close();
			o2.close();
			VCset = bb.bestVertexSet;
		}
		else if(algPick.equalsIgnoreCase("approx")){
			//using approximation
			outFileName1 = inFileName+"_Approx_"+allowedTime+".sol";
			outFileName2 = inFileName+"_Approx_"+allowedTime+".trace";
			Approx app = new Approx(outFileName1, outFileName2);
			VCset = app.getVC_approx(G, true);
		}
		else if(algPick.equalsIgnoreCase("fast")){
			outFileName1 = inFileName+"_Fast_"+allowedTime+"_"+String.valueOf(randSeed)+".sol";
			outFileName2 = inFileName+"_Fast_"+allowedTime+"_"+String.valueOf(randSeed)+".trace";
			fastVC fast = new fastVC(randSeed, allowedTime, outFileName1, outFileName2);
			VCset = fast.getVC_fast(inFileName);
		}
		else{
			//using simulated annealing
			outFileName1 = inFileName+"_SA_"+allowedTime+"_"+String.valueOf(randSeed)+".sol";
			outFileName2 = inFileName+"_SA_"+allowedTime+"_"+String.valueOf(randSeed)+".trace";
			simulatedAnnealing sa = new simulatedAnnealing(randSeed, allowedTime, outFileName1, outFileName2);
			VCset = sa.getVC_sa(G);
		}
		
		long endTime = System.currentTimeMillis();
		
//		output.close();
		System.out.println("Best set found with "+VCset.size()+" of vertices: ");
//		for(Integer v: VCset){
//			System.out.printf("%d,",v);
//		}
		System.out.println();
		System.out.println("Run time in seconds: " + (endTime-startTime)/1e3);
		System.out.println("Finished");
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
			if(lineCnt < G.numNodes){
				lineCnt++;
				line = line.trim();
				if(!line.equals("")){
					String[] curLine = line.split(" ");
				
				//add edges
					for(int i=0; i<curLine.length; i++){
						if(lineCnt==Integer.parseInt(curLine[i])){
							System.out.println("Self edge detected");
							continue;
						}
						edgeCnt += G.addEdge(lineCnt, Integer.parseInt(curLine[i]));
					}
				}
			}
			else
				break;
		}
		
		if(edgeCnt != G.numEdges*2){
			System.out.println("Only "+edgeCnt+" edges detected! Updating");
			G.numEdges = edgeCnt;
		}
		
		//G.updateMostConnectedNodes();
		//G.getIsland();
		G.updateMostConnected();
		fcont.close();
		return G;
	}

}

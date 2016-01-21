import java.util.*;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class simulatedAnnealing {
	int loopCnt = 10;
	int A = 1;
	int B = 1;
	int randSeed;
	int cutoff;
	String out1;
	String out2;
	PrintWriter o1;
	PrintWriter o2;
	
	public simulatedAnnealing(int i, int j, String outfile1, String outfile2) throws FileNotFoundException, UnsupportedEncodingException{
		this.randSeed = i;
		this.cutoff = j;
		this.out1 = outfile1;
		this.out2 = outfile2;
		this.o1 = new PrintWriter(this.out1, "UTF-8");
		this.o2 = new PrintWriter(this.out2, "UTF-8");
	}
	
	public Set<Integer> getVC_sa(Graph G){
		int iter = 0;
		
		double iterCutOff = 1.0*this.cutoff/this.loopCnt;
		
		int deno = Math.min(G.numNodes, 10000);
		
		//double decreaseRatio = Math.pow(10, -2.0/deno);
		double decreaseRatio = 0.9999;
		
		Random gen = new Random(this.randSeed);
		
		Set<Integer> res = new HashSet<Integer>(G.unUsedVertex);
		
		int innerLoopCnt = G.numNodes;
		
		long startTime = System.currentTimeMillis();
		
		int cnt = 0;
		while(iter < this.loopCnt && (System.currentTimeMillis()-startTime)/1000.0 <= this.cutoff){
		//while((System.currentTimeMillis()-startTime)/1000.0 <= this.cutoff){
//						
//			if(cnt%20000==0){
//				System.out.println();
//				System.out.println("iter " + iter);
//			}
			
			initSetUp(G);
			int iterCnt = 0;
			double Temp = 100;
			int f_prev = G.numNodes;
			int f_local = G.numNodes;
			Set<Integer> sol_local = new HashSet<Integer>(G.unUsedVertex);
			Set<Integer> sureSet = new HashSet<Integer>(G.unUsedVertex);
			boolean timeUp = false;
			double passTime = 0;
			long iterStartTime = System.currentTimeMillis();
			
			//while(Temp > 0.01 && iterCnt < innerLoopCnt){
			while(passTime <= iterCutOff){
				
				//System.out.println("Sup "+ iter +" Iter "+iterCnt + " Temp "+Temp);
				
				int curIdx = gen.nextInt(G.numNodes)+1;
				int f_cur;
				
				Set<Integer> searched = new HashSet<Integer>();
				int findCnt = 0;
				boolean found = false;
				
				while(findCnt < 2*G.numNodes && !found){
				//while(!found){
					curIdx = gen.nextInt(G.numNodes)+1;
					found = G.isVC(sol_local, curIdx);
					//System.out.println(curIdx);
					findCnt++;
				}
				
				if(!found){
					passTime = (System.currentTimeMillis() - iterStartTime)/1000.0;
					continue;
				}
								
				if(G.nodes.get(curIdx).used==true){
					G.nodes.get(curIdx).used=false;
					f_cur = calculateF(G);
					
					if(f_cur<f_prev || Math.exp(-(f_cur-f_prev)*(1-G.nodes.get(curIdx).ratio)/Temp) > gen.nextDouble()){
						if(f_cur<f_local){
							f_local = f_cur;
							sol_local.remove(curIdx);
							long curTime = System.currentTimeMillis();
							if(sol_local.size()<sureSet.size() && G.isVC(sol_local)){
								sureSet = new HashSet<Integer>(sol_local);
								cnt++;
								o2.printf("%.2f,%d\n", (curTime-startTime)/1000.0,sol_local.size());
							}
						}
						f_prev = f_cur;
					}
					else{
						G.nodes.get(curIdx).used = true;
					}
				}
				else{
					G.nodes.get(curIdx).used=true;
					f_cur = calculateF(G);
					
					if(f_cur<f_prev || Math.exp(-(f_cur-f_prev)*(1-G.nodes.get(curIdx).ratio)/Temp) < gen.nextDouble()){
						if(f_cur<f_local){
							f_local = f_cur;
							sol_local.add(curIdx);
							long curTime = System.currentTimeMillis();
							if(sol_local.size()<sureSet.size() && G.isVC(sol_local)){
								sureSet = new HashSet<Integer>(sol_local);
								cnt++;
								o2.printf("%.2f,%d\n", (curTime-startTime)/1000.0,sol_local.size());
							}
						}
						f_prev = f_cur;
					}
					else{
						G.nodes.get(curIdx).used = false;
					}
				}
				
				Temp *= decreaseRatio;
				iterCnt++;
				
				long curTime = System.currentTimeMillis();
				passTime = (curTime - iterStartTime)/1000.0;
			}
			
			
			//System.out.println("iter "+iter+" Time: "+(System.currentTimeMillis()-startTime)/1000.0);
			
			//update
			if(res.size()>=sureSet.size()){
				res = new HashSet<Integer>(sureSet);
			}
			iter++;
			
			
		}
		
		double t = (System.currentTimeMillis() - startTime)/1000.0;
		
		
		
		o1.println(res.size());
		for(Integer i: res){
			o1.printf("%d,", i);
		}
		this.o1.close();
		this.o2.close();
		return res;
	}
	
	public int calculateF(Graph G){
		int res = 0;
		int unCoveredEdgeCnt = 0;
		
		for(int i=1; i<G.nodes.size(); i++){
			if(G.nodes.get(i).used==true){
				res++;
			}
			else{
				for(Integer unCovered: G.edgeMap.get(i)){
					if(G.nodes.get(unCovered).used==false)
						unCoveredEdgeCnt += 2;
				}
			}
		}
		
		res = res + unCoveredEdgeCnt/2;
		
		return res;
	}
	
	public void initSetUp(Graph G){
		for(int i=1; i<=G.numNodes; i++){
			//set all nodes being selected
			G.nodes.get(i).used = true;
			G.nodes.get(i).ratio = ((double) G.nodes.get(i).deg)/G.numEdges;
		}
	}
	
}

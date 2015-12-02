import java.util.*;
import java.lang.Math;

public class simulatedAnnealing {
	int loopCnt = 20;
	int A = 1;
	int B = 1;
	
	public Set<Integer> getVC_sa(Graph G){
		int iter = 1;
		
		double decreaseRatio = Math.pow(10, -2.0/G.numNodes);
		
		Random gen = new Random();
		
		Set<Integer> res = new HashSet<Integer>(G.unUsedVertex);
		
		int innerLoopCnt = G.numNodes;
		
		while(iter < this.loopCnt){
			initSetUp(G);
			int iterCnt = 0;
			double Temp = 100;
			int f_prev = G.numNodes;
			int f_local = G.numNodes;
			Set<Integer> sol_local = new HashSet<Integer>(G.unUsedVertex);
			Set<Integer> sureSet = new HashSet<Integer>(G.unUsedVertex);
			while(Temp > 0.01 && iterCnt < innerLoopCnt){
				
				System.out.println("Sup "+ iter +" Iter "+iterCnt + " Temp "+Temp);
				
				int curIdx = gen.nextInt(G.numNodes)+1;
				int f_cur;
				
				Set<Integer> searched = new HashSet<Integer>();
				int findCnt = 0;
				boolean found = false;
				
				while(findCnt < 2*G.numNodes && !found){
					curIdx = gen.nextInt(G.numNodes)+1;
					found = G.isVC(sol_local, curIdx);
					//System.out.println(curIdx);
					findCnt++;
//					if(searched.contains(curIdx)){
//						findCnt--;
//					}
//					else{
//						searched.add(curIdx);
//					}
				}
				
				if(!found){
					break;
				}
//				else if(G.nodes.get(curIdx).used==true){
//					System.out.println("-");
//				}
//				else{
//					System.out.println("+");
//				}
								
				if(G.nodes.get(curIdx).used==true){
					G.nodes.get(curIdx).used=false;
					f_cur = calculateF(G);
					
					if(f_cur<f_prev || Math.exp(-(f_cur-f_prev)*(1-G.nodes.get(curIdx).ratio)/Temp) > gen.nextDouble()){
						if(f_cur<f_local){
							f_local = f_cur;
							sol_local.remove(curIdx);
							if(sol_local.size()<sureSet.size() && G.isVC(sol_local)){
								sureSet = new HashSet<Integer>(sol_local);
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
							if(sol_local.size()<sureSet.size() && G.isVC(sol_local)){
								sureSet = new HashSet<Integer>(sol_local);
							}
						}
						f_prev = f_cur;
					}
					else{
						G.nodes.get(curIdx).used = false;
					}
				}
				
				//if(G.numNodes*0.15<iterCnt){
					Temp *= decreaseRatio;
				//}
				iterCnt++;
			}
			
			
			//update
			if(res.size()>=sureSet.size()){
				res = new HashSet<Integer>(sureSet);
			}
			iter++;
		}
		
		
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

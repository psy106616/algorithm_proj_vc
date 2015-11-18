#include "fastvc.h"

int try_step=100;


int edge_cand;

void cover_LS()
{
	int		remove_v, add_v;
	int		remove_dscore, add_dscore;
	int		e,v1,v2;
	int		i;

	step = 1;

	while(1)
	{
		if (uncov_stack_fill_pointer == 0)//update best solution if needed
		{
			update_best_sol();
			
			//if (c_size==optimal_size) return;
				
			update_target_size();
			
			continue;
		}
		
		if(step%try_step==0) //check cutoff
		{
			times(&finish);
			double elap_time = (finish.tms_utime + finish.tms_stime - start_time)/sysconf(_SC_CLK_TCK);
			if(elap_time >= cutoff_time)return;
		}
		
		remove_v = choose_remove_v();
		
		remove(remove_v);

		
		e = uncov_stack[rand()%uncov_stack_fill_pointer];
		v1 = edge[e].v1;
		v2 = edge[e].v2;

		if(dscore[v1]>dscore[v2] || (dscore[v1]==dscore[v2] && time_stamp[v1]<time_stamp[v2]) )
			add_v=v1;
		else add_v=v2;

		add(add_v);
		
		int index = index_in_remove_cand[remove_v];
		index_in_remove_cand[remove_v] = 0;
		
		remove_cand[index] = add_v;
		index_in_remove_cand[add_v] = index;
		
		time_stamp[add_v]=time_stamp[remove_v]=step;

		step++;
	}

}


int main(int argc, char* argv[])
{
	int seed,i;

	//cout<<"c This is FastMVC, a local search solver for the Minimum Vertex Cover problem."<<endl;
	
	if(build_instance(argv[1])!=1){
		cout<<"can't open instance file"<<endl;
		return -1;
	}
	
	//optimal_size=0;
	i=2;
		
	sscanf(argv[i++],"%d",&seed);
	sscanf(argv[i++],"%d",&cutoff_time);

	
	srand(seed);
	//cout<<seed<<' ';
	cout<<"c This is FastVC, solving instnce "<<argv[1]<<endl;
		
	times(&start);
	start_time = start.tms_utime + start.tms_stime;

   	init_sol();
   
	//if(c_size + uncov_stack_fill_pointer > optimal_size ) 
	//{
		//cout<<"c Start local search..."<<endl;
		cover_LS();
	//}
		
	//check solution
	if(check_solution()==1)
	{
		print_solution();
		cout<<"c Best found vertex cover size = "<<best_c_size<<endl;
		cout<<"c searchSteps = "<<best_step<<endl;
		cout<<"c solveTime = "<<best_comp_time<<endl;
		
		//cout<<best_c_size<<' '<<best_comp_time<<endl;
	}
	
	free_memory();

	return 0;
}

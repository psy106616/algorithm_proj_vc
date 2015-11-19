import java.util.*;
import java.io.*;

public class fastVC {

	public static final int MAXV = 120;
	public static final int MAXE = 620;

	public static int c_size;
	public static int v_num;
	public static int e_num;

	public static int[] dscore = new int[MAXV];

	public static int[] v_degree = new int[MAXV];

	public static int[][] v_edges = new int[MAXV][MAXE];
	public static int[][] v_adj = new int[MAXV][MAXE];

	public static int try_step = 10;

	public static long step;
	public static long best_step;
	public static double best_comp_time;

	public static int[] remove_cand = new int[MAXV];

	public static int[] index_in_uncov_stack = new int[MAXE];

	public static ArrayList uncov_stack = new ArrayList();

	public static int uncov_stack_fill_pointer;

	public static boolean[] v_in_c = new boolean[MAXV];

	public static long[] time_stamp = new long[MAXV];

	public static Edge[] edge = new Edge[MAXE];

	public static boolean[] best_v_in_c = new boolean[MAXV];
	public static int best_c_size;

	public static long startTime = 0;

	public static int remove_cand_size;
	public static int[] index_in_remove_cand = new int[MAXV];

	public static int cand_count = 50;

	public static int cutoff_time;

	public static int optimal_size;

	public static int[] v_degree_tmp = new int[MAXV];

	public static Graph parseGraph(String s) {
		File file = new File(s);
		BufferedReader reader = null;
		Graph G = null;

		try{
			reader = new BufferedReader(new FileReader(file));
			String text = reader.readLine();
			String[] split = text.split(" ");
			v_num = Integer.parseInt(split[0]);
			e_num = Integer.parseInt(split[1]);
			G = new Graph(v_num, e_num);


			int lineCnt = 0;
			while ((text = reader.readLine()) != null) {
				lineCnt++;
				String[] numbers = text.split(" ");

				for (int i = 0; i < numbers.length; i++) {
					int tmp = Integer.parseInt(numbers[i]);
					G.addEdge(lineCnt, tmp);
				}
				v_degree[lineCnt] = numbers.length;
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (reader != null) {
		            reader.close();
		        }
		    } catch (IOException e) {
		    }
		}

		int index = 0;

		for (Integer key : G.Nodes.keySet()) {
			int idx = 0;
			for (Integer i : G.Nodes.get(key)) {
				edge[index] = new Edge(key, i);
				index++;
				// v_edges[key][idx] = index;
				// idx++;
 			}
		}

		// for (Integer key : G.Nodes.keySet()) {
		// 	// v_degree[key] = G.Nodes.get(key).size(); 
		// 	System.out.println(v_degree[key]);
		// 	// for (int i = 0; i < G.Nodes.get(key).size(); i++) {
		// 	// 	List list = new ArrayList();
		// 	// 	list.addAll(G.Nodes.get(key));
		// 	// 	v_adj[key][i] = (Integer)list.get(i); 
		// 	// }
		// }

		int v1, v2;

		for (int e = 0; e < e_num; e++) {
			v1 = edge[e].v1;
			v2 = edge[e].v2;

			// System.out.println(v1+ " " + v2);

			v_edges[v1][v_degree_tmp[v1]] = e;
			v_edges[v2][v_degree_tmp[v2]] = e;

			v_adj[v1][v_degree_tmp[v1]] = v2;
			v_adj[v2][v_degree_tmp[v2]] = v1;

			v_degree_tmp[v1]++;
			v_degree_tmp[v2]++;
		}


		// System.out.println(index);

		return G;
	}

	public static void cover(int e) {
		int index, last_uncov_edge;

		last_uncov_edge = (Integer)uncov_stack.remove(uncov_stack.size() - 1);
		index = index_in_uncov_stack[e];
		System.out.println(index);
		System.out.println(last_uncov_edge);
		if (index >= uncov_stack.size())
		{
			uncov_stack.add(last_uncov_edge);
		}
		else {
			uncov_stack.set(index, last_uncov_edge);
		}
		index_in_uncov_stack[last_uncov_edge] = index;
	}

	public static void uncover(int e) {
		index_in_uncov_stack[e] = uncov_stack_fill_pointer;
		uncov_stack.add(e);
	}

	public static void add(int v) {
		v_in_c[v] = true;
		dscore[v] = -dscore[v];

		int i,e,n;

		int edge_count = v_degree[v];

		for (i = 0; i < edge_count; i++) {
			e = v_edges[v][i];
			n = v_adj[v][i];

			if (v_in_c[n] == false) {
				dscore[n]--;

				cover(e);
			}
			else {
				dscore[n]++;
			}
		}
	}

	public static void remove(int v) {
		v_in_c[v] = false;
		dscore[v] = -dscore[v];

		int i,e,n;
		int edge_count = v_degree[v];
		for (i = 0; i < edge_count; i++) {
			e = v_edges[v][i];
			n = v_adj[v][i];

			if (v_in_c[n] == false) {
				dscore[n]++;

				uncover(e);
			}
			else {
				dscore[n]--;
			}
		}
	}

	public static void reset_remove_cand() {
		int j = 0;
		for (int v = 1; v <= v_num; v++) {
			if (v_in_c[v] == true) {
				remove_cand[j] = v;
				index_in_remove_cand[v] = j;
				j++;
			}
			else index_in_remove_cand[v] = 0;
		}

		remove_cand_size = j;
	}

	public static void init_sol() {
		int v1, v2;

		for (int v = 1; v <= v_num; v++) {
			v_in_c[v] = false;
			dscore[v] = 0;
			time_stamp[v] = 0;
		}

		c_size = 0;
		for (int e = 0; e < e_num; e++) {
			v1 = edge[e].v1;
			v2 = edge[e].v2;

			if (v_in_c[v1] == false && v_in_c[v2] == false) {
				if (v_degree[v1] > v_degree[v2]) {
					v_in_c[v1] = true;
				}
				else {
					v_in_c[v2] = true;
				}
				c_size++;
			}
		}

		System.out.println("After first initialization: The vertex cover is ");

		for (int v = 1; v <= v_num; v++) {
			System.out.print(v + ": " + v_in_c[v] + " ");
		}

		System.out.println();

		uncov_stack_fill_pointer = 0;

		for (int e = 0; e < e_num; e++) {
			v1 = edge[e].v1;
			v2 = edge[e].v2;

			if (v_in_c[v1] == true && v_in_c[v2] == false) dscore[v1]--;
			else if (v_in_c[v2] == true && v_in_c[v1] == false) dscore[v2]--;
		}

		for (int v = 1; v <= v_num; v++) {
			if (v_in_c[v] == true && dscore[v] == 0) {
				remove(v);
				c_size--;
			}
		}

		System.out.println("After second initialization: The vertex cover is ");

		for (int v = 1; v <= v_num; v++) {
			System.out.print(v + ": " + v_in_c[v] + " ");
		}

		System.out.println();

		update_best_sol();

		reset_remove_cand();
	}

	public static void update_best_sol() {
		// System.out.println("here");
		for (int i = 1; i <= v_num; i++)
		{
			// System.out.println(v_in_c[i]);
			best_v_in_c[i] = v_in_c[i];

		}

		System.out.println("After update: The vertex cover is ");

		for (int v = 1; v <= v_num; v++) {
			System.out.print(v + ": " + v_in_c[v] + " ");
		}

		System.out.println();

			
		best_c_size = c_size;
		long finishTime = System.nanoTime();
		best_comp_time = (double) (finishTime - startTime);
		// best_comp_time = round(best_comp_time * 100)/100.0;
		best_step = step;
	}

	public static void update_target_size() {
		c_size--;

		int v,i;
		int best_dscore;
		int best_remove_v;

		best_remove_v = remove_cand[0];
		best_dscore = dscore[best_remove_v];

		if (dscore[best_remove_v] != 0) {
			for (i = 1; i < remove_cand_size; i++) {
				v = remove_cand[i];

				if (dscore[v] == 0) break;

				if (dscore[v] > dscore[best_remove_v])
					best_remove_v = v;
			}
		}

		// System.out.println(remove_cand_size);

		remove(best_remove_v);

		
		int last_remove_cand_v = remove_cand[--remove_cand_size];
		int index = index_in_remove_cand[best_remove_v];
		remove_cand[index] = last_remove_cand_v;
		index_in_remove_cand[last_remove_cand_v] = index;
		
	}

	public static int choose_remove_v() {
		int v;

		Random rand = new Random();

		int best_v = remove_cand[rand.nextInt(remove_cand_size)];

		for (int i = 1; i < cand_count; i++) {
			v = remove_cand[rand.nextInt(remove_cand_size)];

			if (dscore[v] < dscore[best_v])
				continue;
			else if (dscore[v] > dscore[best_v])
				best_v = v;
			else if (time_stamp[v] < time_stamp[best_v])
				best_v = v;
		}

		return best_v;
	}

	public static void cover_LS() {
		int remove_v, add_v;
		int remove_dscore, add_dscore;
		int e,v1,v2;
		int i;

		Random rand = new Random();

		step = 1;

		while(true) {
			// System.out.println(step);
			// if (uncov_stack_fill_pointer == 0) {
			// 	update_best_sol();

			// 	// if (c_size==optimal_size) return;

			// 	update_target_size();

			// 	continue;
			// }

			// System.out.println(step);
			if (step % try_step == 0) {
				// times(&finish);
				long finishTime = System.nanoTime();
				// double elap_time = (finish.tms_utime + finish.tms_stime - start_time)
				double elap_time = (double)(finishTime - startTime);
				System.out.println(elap_time);
				if (elap_time >= cutoff_time) return;
			}

			remove_v = choose_remove_v();

			remove(remove_v);

			System.out.println(uncov_stack);
			// e = (Integer)uncov_stack.get(rand.nextInt(uncov_stack_fill_pointer));
			e = (Integer)uncov_stack.get(0);

			System.out.println(e);
			v1 = edge[e].v1;
			v2 = edge[e].v2;

			if (dscore[v1] > dscore[v2] || (dscore[v1]==dscore[v2] && time_stamp[v1] < time_stamp[v2]))
				add_v = v1;
			else add_v = v2;

			System.out.println(add_v);

			add(add_v);

			int index = index_in_remove_cand[remove_v];
			index_in_remove_cand[remove_v] = 0;

			remove_cand[index] = add_v;
			index_in_remove_cand[add_v] = index;

			time_stamp[add_v] = time_stamp[remove_v] = step;

			step++;
		}

	}

	public static int check_solution() {
		for (int e = 0; e < e_num; e++) {
			// System.out.println(best_v_in_c[e]);
			if (best_v_in_c[edge[e].v1] != true && best_v_in_c[edge[e].v2] != true) {
				System.out.println("c error: uncovered edge "+ e);
				return 0;
			}
		}

		int verified_vc_size = 0;

		for (int i = 1; i <= v_num; i++) {
			if (best_v_in_c[i] == true)
				verified_vc_size++;
		}

		if (best_c_size == verified_vc_size) return 1;

		else {
			System.out.println("c error: claimed best found vc size != verified vc size");
			System.out.println("c claimed best found vc size = " + best_c_size);
			System.out.println("c verified vc size = " + verified_vc_size);
			return 0;
		}
	}

	public static void print_solution() {
		for (int i = 1; i < v_num; i++) {
			if (best_v_in_c[i] == true)
				System.out.println(i);
		}
	}

	public static void main(String args[]) {
		if (args.length < 2) {
			System.err.println("Unexpected number of command line arguments");
			System.exit(1);
		}

		String data_file = args[0];
		int cutoff_time = Integer.parseInt(args[1]);

		// optimal_size = 0;
		Graph G = parseGraph(data_file);

		// System.out.println(G.Nodes);

		startTime = System.nanoTime();

		init_sol();

		// if (c_size + uncov_stack_fill_pointer > optimal_size) {
			cover_LS();
		// }
		

		if (check_solution() == 1) {
			print_solution();
			System.out.println("c Best vertex cover size = " + best_c_size);
			System.out.println("c searchSteps = " + best_step);
			System.out.println("c solveTime = " + best_comp_time);
		}

				
	}
}
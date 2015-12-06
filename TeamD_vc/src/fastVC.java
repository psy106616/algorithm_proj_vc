// This is the fastvc algorithm

import java.util.*;
import java.io.*;

public class fastVC {

	public static final int MAXV = 1000000;
	public static final int MAXE = 6000000;

	public static int c_size;
	public static int v_num;
	public static int e_num;

	public static int[] dscore = new int[MAXV];

	public static int[] v_degree = new int[MAXV];

	public static int[][] v_edges;
	public static int[][] v_adj;

	public static int try_step = 10;

	public static long step;
	public static long best_step;
	public static double best_comp_time;

	public static int[] remove_vertex = new int[MAXV];

	public static int[] index_in_uncover_stack = new int[MAXE];

	public static ArrayList uncover_stack = new ArrayList();

	public static int stack_pointer;

	public static boolean[] v_in_c = new boolean[MAXV];

	public static long[] time_stamp = new long[MAXV];

	public static Edge[] edge = new Edge[MAXE];

	public static boolean[] best_v_in_c = new boolean[MAXV];
	public static int best_c_size;

	public static long startTime = 0;

	public static int remove_size;
	public static int[] index_remove = new int[MAXV];

	public static int cand_count = 50;

	public static float cutoff_time;

	public static int optimal_size;

	public static int[] v_degree_tmp = new int[MAXV];

	public static PrintWriter o1;
	public static PrintWriter o2;

	int randSeed;
	int cutoff;
	String out1;
	String out2;

	public fastVC(int i, int j, String outfile1, String outfile2) throws FileNotFoundException, UnsupportedEncodingException {
		this.randSeed = i;
		cutoff_time = j;
		this.out1 = outfile1;
		this.out2 = outfile2;
		o1 = new PrintWriter(this.out1, "UTF-8");
		o2 = new PrintWriter(this.out2, "UTF-8");
	}

	public Set<Integer> getVC_fast(String s) {
		Set<Integer> VCset = new HashSet<Integer>();
		aGraph G = parseGraph(s);
		startTime = System.nanoTime();
		init_sol();
		cover_vertex();

		if (check_solution() == 1) {
			o1.println(best_c_size);
			for (int i = 1; i < v_num; i++) {
				if (best_v_in_c[i] == true)
				{
					o1.printf("%d,", i);
					VCset.add(i);
				}
			}
		}
		o1.close();
		return VCset;
	}

	public static void print_solution() {
		for (int i = 1; i < v_num; i++) {
			if (best_v_in_c[i] == true)
				System.out.println(i);
		}
	}

	public static aGraph parseGraph(String s) {
		File file = new File(s);
		BufferedReader reader = null;
		aGraph G = null;

		try{
			reader = new BufferedReader(new FileReader(file));
			String text = reader.readLine();
			String[] split = text.split(" ");
			v_num = Integer.parseInt(split[0]);
			e_num = Integer.parseInt(split[1]);
			G = new aGraph(v_num, e_num);


			int lineCnt = 0;
			while ((text = reader.readLine()) != null) {
				lineCnt++;
				if (text.length() > 0)
				{
					String[] numbers = text.split(" ");

					for (int i = 0; i < numbers.length; i++) {
						int tmp = Integer.parseInt(numbers[i]);
						G.addEdge(lineCnt, tmp);
					}
					v_degree[lineCnt] = numbers.length;
				}
				else
					v_degree[lineCnt] = 0;	
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
 			}
		}

		int v1, v2;

		v_edges = new int[v_num+1][];
		v_adj = new int[v_num+1][];

		for (int v = 1; v <= v_num; v++) {
			v_edges[v] = new int[v_degree[v]];
			v_adj[v] = new int[v_degree[v]];
		}

		for (int e = 0; e < e_num; e++) {
			v1 = edge[e].end1;
			v2 = edge[e].end2;

			v_edges[v1][v_degree_tmp[v1]] = e;
			v_edges[v2][v_degree_tmp[v2]] = e;

			v_adj[v1][v_degree_tmp[v1]] = v2;
			v_adj[v2][v_degree_tmp[v2]] = v1;

			v_degree_tmp[v1]++;
			v_degree_tmp[v2]++;
		}

		return G;
	}

	public static void cover(int e) {
		int index, last_uncov_edge;

		last_uncov_edge = (Integer)uncover_stack.remove(uncover_stack.size() - 1);
		index = index_in_uncover_stack[e];
		if (index >= uncover_stack.size())
		{
			uncover_stack.add(last_uncov_edge);
		}
		else {
			uncover_stack.set(index, last_uncov_edge);
		}
		index_in_uncover_stack[last_uncov_edge] = index;
	}

	public static void uncover(int e) {
		index_in_uncover_stack[e] = stack_pointer;
		uncover_stack.add(e);
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

	public static void reset_remove() {
		int j = 0;
		for (int v = 1; v <= v_num; v++) {
			if (v_in_c[v] == true) {
				remove_vertex[j] = v;
				index_remove[v] = j;
				j++;
			}
			else index_remove[v] = 0;
		}

		remove_size = j;
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
			v1 = edge[e].end1;
			v2 = edge[e].end2;

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

		stack_pointer = 0;

		for (int e = 0; e < e_num; e++) {
			v1 = edge[e].end1;
			v2 = edge[e].end2;

			if (v_in_c[v1] == true && v_in_c[v2] == false) dscore[v1]--;
			else if (v_in_c[v2] == true && v_in_c[v1] == false) dscore[v2]--;
		}

		for (int v = 1; v <= v_num; v++) {
			long finishTime = System.nanoTime();
			double elap_time = (double)(finishTime - startTime)/10000000;
			if (v_in_c[v] == true && dscore[v] == 0) {
				remove(v);
				c_size--;
				if (v % 10 == 0) {
					o2.printf("%.2f,%d\n" , elap_time , c_size);
				}	
			}
		}

		update_best_sol();

		reset_remove();

		o2.close();
	}

	public static void update_best_sol() {
		for (int i = 1; i <= v_num; i++)
		{
			best_v_in_c[i] = v_in_c[i];

		}
	
		best_c_size = c_size;
		long finishTime = System.nanoTime();
		best_comp_time = (double) (finishTime - startTime)/10000000;
		o2.printf("%.2f,%d\n", best_comp_time, best_c_size);
		best_step = step;
		o2.close();
	}

	public static void update_target_size() {
		c_size--;

		int v,i;
		int best_dscore;
		int best_remove_v;

		best_remove_v = remove_vertex[0];
		best_dscore = dscore[best_remove_v];

		if (dscore[best_remove_v] != 0) {
			for (i = 1; i < remove_size; i++) {
				v = remove_vertex[i];

				if (dscore[v] == 0) break;

				if (dscore[v] > dscore[best_remove_v])
					best_remove_v = v;
			}
		}

		remove(best_remove_v);
		
	}

	public static int choose_remove_v() {
		int v;

		Random rand = new Random();

		if (remove_size == 0)
			return 0;

		int best_v = remove_vertex[rand.nextInt(remove_size)];

		for (int i = 1; i < cand_count; i++) {
			v = remove_vertex[rand.nextInt(remove_size)];

			if (dscore[v] < dscore[best_v])
				continue;
			else if (dscore[v] > dscore[best_v])
				best_v = v;
			else if (time_stamp[v] < time_stamp[best_v])
				best_v = v;
		}

		return best_v;
	}

	public static void cover_vertex() {
		int remove_v, add_v;
		int remove_dscore, add_dscore;
		int e,v1,v2;
		int i;

		Random rand = new Random();

		step = 1;

		long finishTime = System.nanoTime();
		double elap_time = (double)(finishTime - startTime)/10000000;

		while(elap_time < cutoff_time) {
			if (check_solution() == 0)
			{
				update_best_sol();
				update_target_size();
			}

			finishTime = System.nanoTime();
			elap_time = (double)(finishTime - startTime)/10000000;

			remove_v = choose_remove_v();

			remove(remove_v);

			e = (Integer)uncover_stack.get(0);

			v1 = edge[e].end1;
			v2 = edge[e].end2;

			if (dscore[v1] > dscore[v2] || (dscore[v1]==dscore[v2] && time_stamp[v1] < time_stamp[v2]))
				add_v = v1;
			else add_v = v2;

			add(add_v);

			int index = index_remove[remove_v];
			index_remove[remove_v] = 0;

			remove_vertex[index] = add_v;
			index_remove[add_v] = index;

			time_stamp[add_v] = time_stamp[remove_v] = step;

			step++;
		}

	}

	public static int check_solution() {
		for (int e = 0; e < e_num; e++) {
			if (best_v_in_c[edge[e].end1] != true && best_v_in_c[edge[e].end2] != true) {
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
}
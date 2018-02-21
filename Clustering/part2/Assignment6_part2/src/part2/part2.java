package part2;

import java.io.*;
import java.util.*;

public class part2 {
	public static ArrayList<String> list1 = new ArrayList<String>();
	public static ArrayList<String> list2 = new ArrayList<String>();
	public static int length = 0;

	public static String removeCharacters(String actual_string, String modified_string) {
		char[] original_array = actual_string.toCharArray();
		char[] remove_characters = modified_string.toCharArray();
		int initial, last = 0;
		boolean[] bool_array = new boolean[128];
		for (initial = 0; initial < remove_characters.length; ++initial) {
			bool_array[remove_characters[initial]] = true;
		}
		for (initial = 0; initial < original_array.length; ++initial) {
			if (!bool_array[original_array[initial]]) {
				original_array[last++] = original_array[initial];
			}
		}
		return new String(original_array, 0, last);
	}

	public static void calculateSSE(cluster[] cluster) {
		double[] jaccard_distance = new double[25];
		double sse = 0;
		for (int a = 0; a < length; a++) {
			for (int b = 0; b < 251; b++) {
				if (cluster[b].getInstanceNumber().equals(list1.get(a))) {
					list2.add(cluster[b].getInstance());
				}
			}
		}
		for (int c = 0; c < length; c++) {
			for (int d = 0; d < 251; d++) {
				if (cluster[d].getClusterNumber() == c) {
					jaccard_distance[c] = jaccard_distance[c]
							+ Math.pow(computeJaccardDistance(cluster[d].getInstance(), list2.get(c)), 2);
				}
			}
		}
		for (int e = 0; e < length; e++) {
			sse += jaccard_distance[e];
		}
		System.out.println();
		System.out.println();
		System.out.println("####################################");
		System.out.println("THE SSE VALUE FOR K-MEANS IS:" + sse);
		System.out.println("####################################");
	}

	public static double computeJaccardDistance(String s1, String s2) {
		String[] first_string = s1.split("  ");
		String[] second_string = s2.split("  ");
		ArrayList<String> ch = new ArrayList<String>();
		int count = 0;
		for (int i = 0; i < first_string.length; i++) {
			l: for (int j = 0; j < second_string.length; j++) {

				if (first_string[i].equals(second_string[j])) {
					ch.add(first_string[i]);
					count++;
					break l;
				}

			}
		}
		double c1 = (1 - (0.01 * (count * 100 / (first_string.length + second_string.length - count))));
		return c1;
	}

	public static void update_is(cluster[] cluster) {
		int clusterNumber = 0;
		for (int i = 0; i < length; i++) {
			ArrayList<String> ch1 = new ArrayList<String>();
			ArrayList<String> ch2 = new ArrayList<String>();
			for (int j = 0; j < 251; j++) {
				int cno = cluster[j].getClusterNumber();
				if (cno == i) {
					ch1.add(cluster[j].getInstance());
					ch2.add(cluster[j].getInstanceNumber());
				}
			}
			double[] dist = new double[ch1.size()];
			for (int k = 0; k < ch1.size(); k++) {
				dist[k] = 0;
				for (int l = 0; l < ch1.size(); l++) {
					dist[k] = dist[k] + computeJaccardDistance(ch1.get(k), ch1.get(l));
				}
			}
			if (dist.length != 0) {
				double min_dist = dist[0];
				clusterNumber = 0;
				for (int m = 0; m < dist.length; m++) {
					if (dist[m] <= min_dist) {
						min_dist = dist[m];
						clusterNumber = m;
					}
				}
			}
			if (ch2.size() != 0) {
				list1.set(i, ch2.get(clusterNumber));
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		File file = new File(args[3]);
		System.out.println(file);
		FileOutputStream fout = new FileOutputStream(file);
		PrintStream print = new PrintStream(fout);
		System.setOut(print);
		String rchars = ",.-|!'@#";
		length = Integer.parseInt(args[0]);

		cluster[] cluster = new cluster[251];
		for (int d = 0; d < 251; d++) {
			cluster[d] = new cluster();
		}
		Scanner sc2 = null;
		Scanner sc3 = null;
		try {
			sc2 = new Scanner(new File(args[2]));
			sc3 = new Scanner(new File(args[1]));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int k1 = 0;
		while (sc2.hasNextLine()) {
			int count = 0;
			String l1 = new String();
			String l3 = "";
			l1 = sc2.nextLine();
			String[] st2 = l1.split(" ");
			l: for (int i = 1; i < st2.length; i++) {
				if (st2[i].equals("null,") || st2[i].startsWith("http") || st2[i].startsWith("\"profile_image_url\":"))
					break l;
				count++;
			}
			for (int m = 0; m < st2.length; m++) {
				if (st2[m].equals("\"id\":")) {
					l3 = st2[++m];
				}
			}
			String l2[] = new String[count];

			int k = 1;
			for (int j = 0; j < count; j++) {
				String l4 = new String();
				l4 = removeCharacters(st2[k++], rchars);
				l2[j] = l4;
			}
			cluster[k1].setInstance(l3, l2);
			k1++;
		}
		while (sc3.hasNextLine()) {
			String s = new String();
			s = sc3.nextLine();
			if (("325946633986641920").equals(s))
				s = "325946633986641920,";
			list1.add(s);
		}

		f: for (int it = 0; it < 25; it++) {
			System.out.println();
			System.out.println();
			System.out.println("ITERATION #" + (it + 1));
			System.out.println("-------------------------------");
			for (int i1 = 0; i1 < 251; i1++) {
				double dist[] = new double[length];
				String s3 = new String();
				String s4 = cluster[i1].getInstance();
				for (int k = 0; k < length; k++) {
					for (int l = 0; l < 251; l++) {
						String s5 = list1.get(k);
						String s6 = cluster[l].getInstanceNumber();
						if (s5.equals(s6)) {
							s3 = cluster[l].getInstance();
						}
					}
					dist[k] = computeJaccardDistance(s3, s4);
				}
				double min_dist = dist[0];
				int clusterNumber = 0;
				for (int l = 0; l < dist.length; l++) {
					if (dist[l] < min_dist) {
						min_dist = dist[l];
						clusterNumber = l;
					}
				}

				cluster[i1].setClusterNumber(clusterNumber);
			}
			int flag = 0;
			for (int d = 0; d < length; d++) {
				System.out.println("CLUSTER #" + (d + 1));
				System.out.println(
						"********************************************************************************************");
				System.out.print("<");
				for (int x = 0; x < 251; x++) {
					if (cluster[x].getClusterNumber() == d) {
						flag = 0;
						System.out.print(cluster[x].instanceNumber);
					}
				}
				if (flag == 1) {
					System.out.print("There are no points in this cluster");
				}
				System.out.print(">");
				System.out.println();
				System.out.println();
			}
			ArrayList<String> is3 = new ArrayList<String>(list1);
			int cc = 0;
			update_is(cluster);
			for (int g = 0; g < length; g++) {
				if (list1.get(g).equals(is3.get(g)))
					cc++;
			}
			if (cc == length) {
				System.out.println();
				System.out.println();
				System.out.println("Iterations ended because Centroids remain the same!!");
				break f;
			}
		}
		calculateSSE(cluster);
	}
}

class cluster {

	public int clusterNumber;
	public String instanceNumber;
	public String tweet;
	public int tweet_length;

	public void setClusterNumber(int cn) {
		clusterNumber = cn;
	}

	public int getClusterNumber() {
		return clusterNumber;
	}

	public int tweetSize() {
		return tweet_length;
	}

	public void setInstance(String no, String[] st) {
		instanceNumber = no;
		tweet = Arrays.toString(st).replace(',', ' ').replace('[', ' ').replace(']', ' ');
		tweet_length = st.length;
	}

	public String getInstance() {
		return tweet;
	}

	public String getInstanceNumber() {
		return instanceNumber;
	}
}
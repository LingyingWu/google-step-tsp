import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TravellingSalesmanProblem {

	static ArrayList<City<Double, Double>> position = new ArrayList<City<Double, Double>>();
	static ArrayList<Integer> solution = new ArrayList<Integer>();
	static ArrayList<ArrayList<Integer>> permutation = new ArrayList<ArrayList<Integer>>();
	static double[][] distance;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		double totalDistance;
		if (args.length != 2) {
			System.out.println("Usage: java TravellingSalsemanProblem inputFile solutionFile");
			System.exit(1);
		}
		readInput(args[0]);
		getAllDistance(position.size());

		totalDistance = solverBrute();
		// totalDistance = solverGreedy();
		// totalDistance = solverGreedyShortestFirst();
		// totalDistance = solverTwoOpt();
		// totalDistance = solverThreeOpt();

		printSolution(args[1]);
		System.out.println("total distance: " + totalDistance);
	}

	public static double solverThreeOpt() {
		solverGreedy();

		int visited = 0;
		double bestDistance = calTotalDistance(solution);
		ArrayList<Integer> tour = new ArrayList<Integer>(solution);

		while (visited < tour.size()) {
			for (int i = 0; i < tour.size() - 2; i++) {
				for (int j = i + 1; j < tour.size() - 1; j++) {
					for (int k = +1; k < tour.size(); k++) {
						ArrayList<Integer> betterTour = new ArrayList<Integer>(twoOptSwap(tour, i, k));
						betterTour = twoOptSwap(betterTour, j, k);
						double newDistance = calTotalDistance(betterTour);

						if (newDistance < bestDistance) {
							visited = 0;
							bestDistance = newDistance;
							tour = betterTour;
						}
					}
				}
			}
			visited++;
		}
		solution = tour;
		return calTotalDistance(solution);
	}

	public static double solverTwoOpt() {
		solverGreedy();
		solution = twoOpt(solution);

		return calTotalDistance(solution);
	}

	public static ArrayList<Integer> twoOpt(ArrayList<Integer> tour) {

		// repeat until no improvement is made
		int visited = 0;
		double bestDistance = calTotalDistance(tour);
		while (visited < tour.size()) {
			for (int i = 0; i < tour.size() - 1; i++) {
				for (int k = i + 1; k < tour.size(); k++) {
					ArrayList<Integer> betterTour = new ArrayList<Integer>(twoOptSwap(tour, i, k));
					double newDistance = calTotalDistance(betterTour);
					if (newDistance < bestDistance) {
						visited = 0;
						bestDistance = newDistance;
						tour = betterTour;
					}
				}
			}
			visited++;
		}
		return tour;
	}

	public static ArrayList<Integer> twoOptSwap(ArrayList<Integer> tour, int i, int k) {
		ArrayList<Integer> newTour = new ArrayList<Integer>(tour);

		// 1. take route[0] to route[i-1] and add them in order to new_route
		for (int j = 0; j <= i - 1; j++) {
			newTour.set(j, tour.get(j));
		}

		// 2. take route[i] to route[k] and add them in reverse order
		int dec = 0;
		for (int j = i; j <= k; j++) {
			newTour.set(j, tour.get(k - dec));
			dec++;
		}

		// 3. take route[k+1] to end and add them in order to new_route
		for (int j = k + 1; j < tour.size(); j++) {
			newTour.set(j, tour.get(j));
		}

		return newTour;
	}

	public static double solverBrute() {
		ArrayList<Integer> cityList = new ArrayList<Integer>();
		for (int i = 1; i < position.size(); i++) // start from city 0
			cityList.add(i);
		permute(cityList, 0);

		double totalDistance;
		double min = Double.MAX_VALUE;
		ArrayList<Integer> best = new ArrayList<Integer>();

		for (ArrayList<Integer> path : permutation) {
			path.add(0, 0);
			totalDistance = calTotalDistance(path);
			if (totalDistance < min) {
				min = totalDistance;
				best = path;
			}
		}
		totalDistance = min;

		for (int i = 0; i < best.size(); i++)
			solution.add(best.get(i));
		return totalDistance;
	}

	public static void permute(ArrayList<Integer> list, int index) {
		if (index >= list.size() - 1) { // nothing left
			permutation.add(new ArrayList<Integer>(list));
		} else { // put each element from index+1 in location "index" in turn
			permute(list, index + 1); // special case: no swap

			int temp = list.get(index);
			for (int i = index + 1; i < list.size(); i++) {
				list.set(index, list.get(i)); // swap
				list.set(i, temp);

				// find all permutations of the elements after index
				permute(list, index + 1);

				list.set(i, list.get(index)); // put back
				list.set(index, temp);
			}
		}
	}

	public static double solverGreedy() {

		// start from city 0
		int current = 0;
		solution.add(current);

		ArrayList<Integer> unvisited = new ArrayList<Integer>();
		for (int i = 1; i < position.size(); i++) {
			unvisited.add(i);
		}

		// choose the nearest neighbor as next city
		int next = 0;
		while ((unvisited.size()) != 0) {
			double min = Double.MAX_VALUE;
			for (int i = 0; i < unvisited.size(); i++) {
				if (distance[current][unvisited.get(i)] < min) {
					next = i;
					min = distance[current][unvisited.get(i)];
				}
			}
			next = unvisited.remove(next);
			solution.add(next);
			current = next;
		}
		return calTotalDistance(solution);
	}

	public static double solverGreedyShortestFirst() {

		ArrayList<Integer> unvisited = new ArrayList<Integer>();
		for (int i = 0; i < position.size(); i++) {
			unvisited.add(i);
		}

		// start from the path with the shortest distance
		int origin = 0, current = 0, next = 0;
		double min = Double.MAX_VALUE;
		for (int i = 1; i < position.size(); i++) {
			for (int j = 0; j < i; j++) {
				if (distance[i][j] < min) {
					min = distance[i][j];
					origin = i;
					next = j;
				}
			}
		}

		solution.add(origin);
		solution.add(next);
		unvisited.remove(origin);
		current = unvisited.remove(next);

		// choose the city with the shortest distance from the current city
		while ((unvisited.size()) != 0) {
			min = Double.MAX_VALUE;
			for (int i = 0; i < unvisited.size(); i++) {
				if (distance[current][unvisited.get(i)] < min) {
					next = i;
					min = distance[current][unvisited.get(i)];
				}
			}
			next = unvisited.remove(next);
			solution.add(next);
			current = next;
		}
		return calTotalDistance(solution);
	}

	public static void readInput(String file) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine(); // ignore the first line
		String[] p = new String[2];

		while ((line = br.readLine()) != null) {
			p = line.split(",");
			position.add(new City<Double, Double>(Double.parseDouble(p[0]), Double.parseDouble(p[1])));
		}
		br.close();
	}

	public static void printSolution(String file) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write("index\n");
		int previous = solution.get(solution.size() - 1);
		for (int item : solution) {
			bw.write(Integer.toString(item) + " -> " + Double.toString(distance[previous][item]));
			bw.write("\n");
			previous = item;
		}
		bw.flush();
		bw.close();
	}

	public static void getAllDistance(int n) {
		distance = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				distance[i][j] = distance[j][i] = calDistance(i, j);
			}
		}
	}

	public static double calTotalDistance(ArrayList<Integer> path) {
		double total = 0.0;
		for (int i = 0; i < path.size() - 1; i++) {
			total += distance[path.get(i)][path.get(i + 1)];
		}
		total += distance[path.get(path.size() - 1)][path.get(0)];
		return total;
	}

	public static double calDistance(int cityA, int cityB) {
		return Math.sqrt(Math.pow(position.get(cityA).x - position.get(cityB).x, 2)
				+ Math.pow(position.get(cityA).y - position.get(cityB).y, 2));
	}

}
/**
 * Homework # 3 - 
 * Concepts of AI - Tinkham
 * Jun He
 * Sean Fast
 */

import java.util.List;

/**
 * This class defines the Frontier object which represents the frontier of the
 * A* search
 */
public class Frontier implements Comparable<Frontier> {
	Frontier preFrontier; // previous frontier in path
	List<Character> list; // collection of characters in iteration
	int cost; // current cost to get to the path

	/**
	 * Constructor for Frontier object
	 */
	public Frontier(Frontier preFrontier, List<Character> list, int cost) {
		this.preFrontier = preFrontier;
		this.list = list;
		this.cost = cost;
	}

	/**
	 * @return the list
	 */
	public List<Character> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<Character> list) {
		this.list = list;
	}

	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

	/**
	 * @return weight 
	 * 			the current cost + the heuristic value heuristic value is
	 *         the number of B's in first four spaces
	 */
	public int getWeight() {
		int n = 0;
		for (int i = 0; i < 4; i++)
			// if(Character.toLowerCase(list.get(i))=='b') //TODO:
			if (list.get(i) == 'B') {
				n++;
			}
		return n + cost;
	}

	/**
	 * override compareTo function to allow for the collection to be sorted
	 */
	public int compareTo(Frontier arg0) {
		return getWeight() - arg0.getWeight();
	}

	/**
	 * override equals function to see if the frontier is in the closed or
	 * existing frontier list
	 */
	public boolean equals(Frontier arg0) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != arg0.getList().get(i))
				return false;
		}
		return true;
	}

	/**
	 * @return preFrontier object
	 */
	public Frontier getPreFrontier() {
		return preFrontier;
	}

	/**
	 * @param preFrontier
	 */
	public void setPreFrontier(Frontier preFrontier) {
		this.preFrontier = preFrontier;
	}
}

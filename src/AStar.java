/**
 * Homework # 3 - 
 * Concepts of AI - Tinkham
 * @author Jun He
 * @author Sean Fast
 * 
 * Assignment: Implementing A*
 * Write a program in a language of your choice to solve the following 
 * sliding block puzzle, using the A* algorithm:
 * The puzzle contains 3 black tiles and 3 white tiles 
 * which can slide or hop within a row of 7 spaces. 
 * The initial puzzle configuration is:
 * B	B	B	   	W	W	W
 * The goal (or "solved") configuration is to have all 
 * of the white tiles to the left of all the black tiles. 
 * Note that there are several possible puzzle configurations 
 * that qualify as "solved"; for example, both of these are 
 * "solved" configurations:
 * W	W	   	W	B	B	B
 * W	W	W	B	B	B
 * There are two possible moves:
 * -Slide a tile into an adjacent empty square. This move has cost 1.
 * -Hop a tile over 1 or 2 adjacent tiles, landing in an empty square. 
 * The cost of this move is the number of tiles hopped.
 * You may use any heuristic function that you choose. One easy heuristic
 * function is the number of black tiles to the left of the leftmost 
 * white tile. For example, h(['b', 'b', 'w', 'b', '  ', 'w', 'w']) 
 * would return 2. Feel free to use this one, or any other heuristic 
 * function that successfully guides your search toward a goal.
 * Your program should report the solution by printing the sequence 
 * of states that the puzzle goes through from the start to the goal.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class receives user input from console in the form of a string
 * and converts it to uppercase, then a character array. Then it uses the A*
 * algorithm to compute the solution path
 */
public class AStar {
	private static List<Frontier> frontiers = new ArrayList<Frontier>(); //list of frontier paths
	private static List<Frontier> closed = new ArrayList<Frontier>(); //list of closed paths
	private Frontier goal; //final state of character array (all W's to left of leftmost B)

	public static void main(String[] arg) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        char[] input = null; //user input
        do{
        	try {
        		System.out.print("Enter String:");
				input = br.readLine().toUpperCase().toCharArray(); //convert user input to uppercase & character array
			} catch (IOException e) {
				e.printStackTrace();
			}
        }while(!validInput(input)); //perform input validation
        
        //convert to arraylist for swapping of chars
        List<Character> list = new ArrayList<Character>(); 
        for(char c : input)
        	list.add(c);
        
        AStar hw = new AStar(list); //perform A* algorithm on arraylist
		hw.run();
		
		System.out.println("Solution:");
		System.out.println(hw.getSolution());
		System.out.println("\nCost:");
		System.out.println(hw.getGoal().getWeight());
		System.out.println("\nFrontier List:");
		System.out.println(hw.getFrontierList());
		System.out.println("\nClosed List:");
		System.out.println(hw.getClosedList());
	}

	/**
	 * This method adds a new frontier to the list
	 */
	public AStar(List<Character> list) {
		frontiers.add(new Frontier(null, list, 0));
	}

	/**
	 * This method performs the A* algorithm on the list
	 */
	public void run() {
		Frontier target = frontiers.remove(0); //pop off initial state
		while (!goal(target)) { //check if open state is goal state
			next(target); //if not, expand target's children states
			Collections.sort(frontiers); //sort frontier list in ascending cost order
			closed.add(target); //add popped off state to closed list
			target = frontiers.remove(0); //repeat process with new target
		}
		goal = target; //invariant: target is your goal state
	}

	/**
	 * This method expands the target's children state
	 */
	private void next(Frontier frontier) {
		List<Character> list = frontier.getList(); //current frontier list
		int spaceIndex = list.indexOf(' '); //get index of space char
		for (int i = -3; i < 4; i++) { //find available indexes for space
			int index = i + spaceIndex;
			if (index >= 0 && index != spaceIndex && index < 7) { //find valid spaces
				
				List<Character> listCopy = new ArrayList<Character>(7); 
				for (int j = 0; j < 7; j++) {
					listCopy.add(' '); //copy space into every element of copy list
				}
				Collections.copy(listCopy, frontier.getList()); //copy open state list to new frontier list
				
				Collections.swap(listCopy, spaceIndex, index); //swap space char
				
				int additionalCost = Math.abs(i) < 3 ? 1 : 2; //compute cost of swap
				int cost = frontier.getCost() + additionalCost; //compute current cost for frontier
				Frontier newFrontier = new Frontier(frontier, listCopy, cost); //create new frontier
				if (!(isContains(closed, newFrontier)||isContains(frontiers, newFrontier))) 
					//check if new frontier is in closed or open list
					frontiers.add(newFrontier); //add if not in either
			}
		}
	}

	/**
	 * This method checks if given frontier is goal state
	 */
	private boolean goal(Frontier frontier) {
		int n = 0;
		for (int i = 0; i < 4; i++)
			if (frontier.getList().get(i) != 'B')
				n++;
		return n == 4;
	}
	
	/**
	 * This method checks the user input string for validity
	 */
	public static boolean validInput(char[] list){
		int nWhite=3; //should only be 3 white chars
		int nBlack=3; //should only be 3 black chars
		int nSpace=1; //should only be 1 space char
		for(char c: list){
			switch(c){
			case 'W':
				nWhite--; //decrement white char count
				break;
			case 'B':
				nBlack--; //decrement black char count
				break;
			case ' ':
				nSpace--; //decrement space char count
				break;	
			default:
				return false; //invalid char 
			}
		}
		//if quantities of each character type are correct, return true
		return (nWhite == 0) && (nBlack == 0) && (nSpace==0);
	}
	
	/**
	 * This method checks if list contains given frontier
	 */
	private boolean isContains(List<Frontier>list,Frontier frontier){
		for (Frontier f : list) {
			if (f.equals(frontier)){
				if (frontier.getWeight() < f.getWeight()) //true could be only occurred when list is a reference of frontiers
					list.set(list.indexOf(f), frontier);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return solution 
	 */
	public String getSolution(){
		Frontier preFrontier = goal;
		String output = "";
		while (preFrontier != null) { //run till first prefrontier
			output = preFrontier.getList() + "\n" + output; //build output string of prefontiers
			preFrontier = preFrontier.getPreFrontier(); 
		}
		return output;
	}
	
	/**
	 * @return frontier list 
	 */
	public String getFrontierList(){
		String output = "";
		for(int i = 0; i < frontiers.size(); i++)
			output += i + ". " + frontiers.get(i).getList() + "\n";//build output string of frontier 
		return output;
	}
	
	/**
	 * @return closed list 
	 */
	public String getClosedList(){
		String output = "";
		for(int i = 0; i < closed.size(); i++)
			output += i + ". " + closed.get(i).getList() + "\n"; //build output string of frontier
		return output;
	}

	/**
	 * @return goal 
	 */
	public Frontier getGoal() {
		return goal;
	}

}

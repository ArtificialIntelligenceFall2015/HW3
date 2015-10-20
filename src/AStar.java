import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AStar {
	private static List<Frontier> frontiers = new ArrayList<Frontier>();
	private static List<Frontier> closer = new ArrayList<Frontier>();
	private Frontier goal;

	public static void main(String[] arg) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        char[] input=null;
        do{
        	try {
        		System.out.print("Enter String:");
				input = br.readLine().toCharArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }while(!validInput(input));
        List<Character> list=new ArrayList<Character>();
        for(char c:input)
        	list.add(c);
        AStar hw = new AStar(list);
		hw.run();
		System.out.println("Solution:");
		System.out.println(hw.getSolution());
		System.out.println("\nCost:");
		System.out.println(hw.getGoal().getWeight());
		System.out.println("\nFrontier List:");
		System.out.println(hw.getFrontierList());
		System.out.println("\nCloser List:");
		System.out.println(hw.getCloserList());
	}

	public AStar(List<Character> list) {
		frontiers.add(new Frontier(null, list, 0));
	}

	public void run() {
		Frontier target = frontiers.remove(0);
		while (!goal(target)) {
			next(target);
			Collections.sort(frontiers);
			closer.add(target);
			target = frontiers.remove(0);
		}
		goal=target;
	}

	private void next(Frontier frontier) {
		List<Character> list = frontier.getList();
		int spaceIndex = list.indexOf(' ');
		for (int i = -2; i < 3; i++) {
			int index = i + spaceIndex;
			if (index >= 0 && index != spaceIndex && index < 7) {
				List<Character> listCopy = new ArrayList<Character>(7);
				for (int j = 0; j < 7; j++)
					listCopy.add(' ');
				Collections.copy(listCopy, frontier.getList());
				Collections.swap(listCopy, spaceIndex, index);
				int cost = frontier.getCost() + Math.abs(i);
				Frontier frontierCopy = new Frontier(frontier, listCopy, cost);
				if (!(isContains(closer, frontierCopy)||isContains(frontiers, frontierCopy)))
					frontiers.add(frontierCopy);
			}
		}
	}

	private boolean goal(Frontier frontier) {
		int n = 0;
		for (int i = 0; i < 4; i++)
			if (Character.toLowerCase(frontier.getList().get(i)) != 'b')
				n++;
		return n == 4;
	}
	
	public static boolean validInput(char[] list){
		int nWhite=3;
		int nBlack=3;
		int nSpace=1;
		for(char c: list){
			switch(Character.toLowerCase(c)){
			case 'w':
				nWhite--;
				break;
			case 'b':
				nBlack--;
				break;
			case ' ':
				nSpace--;
				break;	
			default:
				return false;
			}
		}
		return nWhite==0&&nBlack==0&&nSpace==0;
	}
	
	private boolean isContains(List<Frontier>list,Frontier frontier){
		for (Frontier f : list) {
			if (f.equals(frontier)){
				if (frontier.getWeight() < f.getWeight())//true could be only occurred when list is a reference of frontiers
					list.set(list.indexOf(f), frontier);
				return true;
			}
		}
		return false;
	}
	
	public String getSolution(){
		Frontier preFrontier = goal;
		String output="";
		while (preFrontier != null) {
			output=preFrontier.getList()+"\n"+output;
			preFrontier = preFrontier.getPreFrontier();
		}
		return output;
	}
	
	public String getFrontierList(){
		String output="";
		for(int i=0; i<frontiers.size(); i++)
			output+=i+". "+frontiers.get(i).getList()+"\n";
		return output;
	}
	
	public String getCloserList(){
		String output="";
		for(int i=0; i<closer.size(); i++)
			output+=i+". "+closer.get(i).getList()+"\n";
		return output;
	}

	public Frontier getGoal() {
		return goal;
	}

	public void setGoal(Frontier goal) {
		this.goal = goal;
	}
}

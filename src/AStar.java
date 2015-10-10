import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AStar {
	List<Frontier> frontiers = new ArrayList<Frontier>();
	List<Frontier> closer = new ArrayList<Frontier>();

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
        }while(!AStar.validInput(input));
        List<Character> list=new ArrayList<Character>();
        for(char c:input)
        	list.add(c);
        AStar hw = new AStar(list);
		Frontier frontier = hw.run();
		Frontier preFrontier = frontier;
		String output="";
		while (preFrontier != null) {
			output=preFrontier.getList()+"\n"+output;
			preFrontier = preFrontier.getPreFrontier();
		}
		System.out.println(output);
	}

	public AStar(List<Character> list) {
		frontiers.add(new Frontier(null, list, 0));
	}

	public Frontier run() {
		Frontier target = frontiers.remove(0);
		while (!goal(target)) {
			next(target);
			Collections.sort(frontiers);
			closer.add(target);
			target = frontiers.remove(0);
		}
		return target;
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
				boolean match = false;
				for (Frontier f : closer) {
					if (f.equals(frontierCopy))
						match = true;
				}
				if (!match) {
					for (Frontier f : frontiers) {
						if (f.equals(frontierCopy)){
							match = true;
							if (frontierCopy.getWeight() < f.getWeight())
								frontiers.set(frontiers.indexOf(f), frontierCopy);
						}
					}
					if (!match)
						frontiers.add(frontierCopy);
				}
			}
		}
	}

	private boolean goal(Frontier frontier) {
		int n = 0;
		for (int i = 0; i < 4; i++)
			if (frontier.getList().get(i) != 'b')
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
}

import java.util.ArrayList;
import java.util.List;

public class Frontier implements Comparable<Frontier>{
	Frontier preFrontier;
	List<Character> list;
	int cost;
	public List<Character> getList() {
		return list;
	}
	public void setList(List<Character> list) {
		this.list = list;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public int getWeight(){
		int n=0;
		for(int i=0; i<4; i++)
			if(Character.toLowerCase(list.get(i))=='b')
				n++;
		return n+cost;
	}
	public Frontier(Frontier preFrontier, List<Character> list, int cost) {
		super();
		this.preFrontier = preFrontier;
		this.list = list;
		this.cost = cost;
	}
	@Override
	public int compareTo(Frontier arg0) {
		// TODO Auto-generated method stub
		return getWeight()-arg0.getWeight();
	}
	
	public boolean equals(Frontier arg0){
		for(int i=0; i<list.size(); i++){
			if(list.get(i)!=arg0.getList().get(i))
				return false;
		}
		return true;
	}
	public Frontier getPreFrontier() {
		return preFrontier;
	}
	public void setPreFrontier(Frontier preFrontier) {
		this.preFrontier = preFrontier;
	}
}

package game_mechanics;

public class Auction {
	
	private Property property;
	
	private int money;
	private int hit;
	private int player;
	private boolean playersIn[];
	
	public Auction(Property property, int startPrice, int numberOfPlayers) {
		// TODO Auto-generated constructor stub
		this.property = property;
		this.money = startPrice;
		this.playersIn = new boolean[numberOfPlayers];
		for (int i = 0; i < playersIn.length; i++) {
			playersIn[i] = true;
		}
		
		
	}

	public boolean bid(int player, int value){
		
		this.player = player;
		this.money+= value;
		
		return true;
	}
	public void hit(){
		hit++;
	}
	
	public int getPlayer() {
		return player;
	}
	public int getMoney() {
		return money;
	}
	public Property getProperty() {
		return property;
	}
	public int getHit() {
		return hit;
	}
	public boolean playerLeft(int player){
		playersIn[player] = false;
		int a = 0;
		for(int i = 0; i<playersIn.length; i++){
			if(playersIn[i]){
				a++;
			}
			if(a>1){
				return  true;
			}
		}
		return false;
	}
	
	
}

package game_mechanics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Player {
	
	private int playerId;
	private String name;
	private int figurka;
	
	private int position;
	
	
	private boolean isInPrison;
	private int numberOfRound;
	
	private PlayerInteraction playerInteraction;
	private Wallet wallet;
	private List<Property> properties = new ArrayList<>();
	private boolean isActive = true;
	private List<Property>[] groups = new List[10];
	
	
	public Player(int playerId, String name, int figurka, int position,int money, PlayerInteraction playerInteraction) {
		this.wallet = new Wallet(money, playerInteraction, this);
		this.playerId = playerId;
		this.name = name;
		this.figurka = figurka;
		this.position = position;
		this.playerInteraction = playerInteraction;
		for(int i = 0; i<groups.length; i++){
			groups[i] = new  ArrayList<Property>();
		}
	}
	public Player(int playerId, String name, int figurka, int money, PlayerInteraction playerInteraction) {
		this.wallet = new Wallet(money, playerInteraction, this);
		this.playerInteraction = playerInteraction;
		this.playerId = playerId;
		this.name = name;
		this.figurka = figurka;
		for(int i = 0; i<groups.length; i++){
			groups[i] = new  ArrayList<Property>();
		}
	}
	public String getPlayerType(){
		return playerInteraction.getPlayersType(this);
	}
	
	/*public PlayerInteraction getPlayerInteraction() {
		return playerInteraction;
	}*/
	
	public boolean hasRealty(int id){
		for(Property property: properties){
			if(property.getType()==Property.REALTY && property.getRealty().getRealtyID()==id){
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}
	protected void addProperty(Property property){
		property.setOwner(playerId);
		if(property.getRealty()!=null){
			groups[property.getRealty().getGroup()-1].add(property);
			groups[property.getRealty().getGroup()-1].sort(new Comparator<Property>() {

				@Override
				public int compare(Property o1, Property o2) {
					// TODO Auto-generated method stub
					return Integer.compare(o1.getRealty().getRealtyID(),o2.getRealty().getRealtyID());
				}
			});
		}
		properties.add(property);
	}
	protected void removeProperty(Property property){
		properties.remove(property);
		if(property.getRealty()!=null){
			groups[property.getRealty().getGroup()-1].remove(property);
		}
	}
	public List<Property> getGroupProperties(int group){
		return new ArrayList<Property>(groups[group-1]);
	}
	
	protected Wallet getWallet() {
		return wallet;
	}
	
	protected List<Property> getProperties() {
		return properties;
	}
	public int getPlayerId() {
		return playerId;
	}
	public int getFigurka() {
		return figurka;
	}

	public boolean isInPrison() {
		return isInPrison;
	}
	protected void goToPrison() {
		this.isInPrison = true;
		
		numberOfRound = 0;
	}
	protected void roundInPrison(){
		numberOfRound ++;
	}
	public int getNumberOfRound() {
		return numberOfRound;
	}
	
	public int getPosition() {
		return position;
	}
	protected void setPosition(int position) {
		this.position = position;
	}
	public int getMoney(){
		return wallet.getMoney();
	}
	public int getHouses(){
		int h = 0;
		for(Property property: properties){
			if(property.getType()==Property.REALTY){
				int n= property.getNumberOfHouse();
				if(n==5){
					n=4;
				}
				h+= n;
			}
		}
		return h;
	}
	public int getHotels(){
		int h = 0;
		for(Property property: properties){
			if(property.getType()==Property.REALTY){
				int n= property.getNumberOfHouse();
				if(n==5){
					h++;
				}
			}
		}
		return h;
	}
	protected void releaseFromPrison() {
		// TODO Auto-generated method stub
		isInPrison = false;
		numberOfRound = 0;
	}
	public boolean hasCardPrison() {
		for(Property property: properties){
			if(property.getType()== Property.CARD){
				return true;
			}
		}
		return false;
	}
	protected Card takeCard(){
		Card card = null;
		int a = -1;
		for(int i = 0; i<properties.size(); i++){
			if(properties.get(i).getType()==Property.CARD){
				a = i;
				break;
			}
		}
		if(a!=-1){
			card = properties.remove(a).getCard();
		}
		return card;
	}
	/**
	 * Take a property
	 * @param id
	 * @return
	 */
	protected Property getProperty(int id){
		Property prop = null;
		int a = -1;
		for(int i = 0; i<properties.size(); i++){
			if(properties.get(i).getType()==Property.REALTY&& properties.get(i).getRealty().getRealtyID()==id){
				a = i;
				break;
			}
		}
		if(a!=-1){
			prop = properties.remove(a);
			if(prop.getRealty()!=null){
				groups[prop.getRealty().getGroup()-1].remove(prop);
			}
			
		}
		return prop;
	}
	public int getNumberOfHousesOwned(){
		int a = 0;
		for(Property property: properties){
			if(property.getRealty()!=null){
				a ++;
			}
		}
		return a;
	}
	public int getNumberOfCardsOwn(){
		int a = 0;
		for(Property property: properties){
			if(property.getCard()!=null){
				a ++;
			}
		}
		return a;
	}
	public boolean isActive() {
		return isActive;
	}
	public List<Property> getPropertiesPublic(){
		return new ArrayList<>(properties);
	}
	
	protected void inactive(){
		isActive = false;
	}
	public void noPropertyies() {
		// TODO Auto-generated method stub
		properties.clear();
		for(int i = 0; i<groups.length; i++){
			groups[i].clear();
		}
	}
	
	public int countValueOfProperty(){
		int money=  getMoney();
		for(int i = 0; i<properties.size(); i++){
			Property p = properties.get(i);
			if(p.getRealty()!=null && !p.isMortage()){
				money+= p.getRealty().getPrice();
				money+=p.getNumberOfHouse()*p.getRealty().getHouseCost()/2;
			}
		}
		return money;
	}
	
}


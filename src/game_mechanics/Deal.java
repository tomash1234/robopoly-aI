package game_mechanics;

import java.util.ArrayList;
import java.util.List;

public class Deal {
	
	private List<Property> offer = new ArrayList<>();
	private List<Property> demand = new ArrayList<>();
	
	private int moneyOffer;
	private int moneyDemand;
	
	private int rent;
	
	private int playerWhoOffer;
	private int dealTo;
	
	private boolean confirmO;
	private boolean confirmD;
	
	private int rentValue = 0;
	
	
	
	public Deal(int playerWhoOffer, int dealTo) {
		this.playerWhoOffer = playerWhoOffer;
		this.dealTo = dealTo;
	}
	
	public int getPlayerWhoOffer() {
		return playerWhoOffer;
	}
	public int getDealTo() {
		return dealTo;
	}
	public void addOffer(Property propterty){
		offer.add(propterty);
	}
	public void addDemand(Property propterty){
		demand.add(propterty);
	}
	
	public Property getOffer(int index){
		return offer.get(index);
	}

	public Property getDemand(int index){
		return demand.get(index);
	}
	
	public int getOfferLength(){
		return offer.size();
	}
	public int getDemandLength(){
		return demand.size();
	}
	
	public int getMoneyDemand() {
		return moneyDemand;
	}
	public int getMoneyOffer() {
		return moneyOffer;
	}
	public void setMoneyDemand(int moneyDemand) {
		this.moneyDemand = moneyDemand;
	}
	public void setMoneyOffer(int moneyOffer) {
		this.moneyOffer = moneyOffer;
	}
	
	protected void confirmD() {
		this.confirmD = true;
	}
	protected void confirmO() {
		this.confirmO = true;
	}
	
	public void setRentOmittedFromOffer(){
		rent = -1;
	}

	public void setRentOmittedFromDemand(){
		rent = 1;
	}
	protected void setRentValue(int rentValue) {
		this.rentValue = rentValue;
	}
	public int getRentValue() {
		return rentValue;
	}
	
	public int getRent() {
		return rent;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Player " +  moneyOffer + " offers " + offer + ", to player" + dealTo + " for " + demand;
	}
}

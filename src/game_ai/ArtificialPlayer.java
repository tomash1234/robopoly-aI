package game_ai;

import java.util.List;

import game_mechanics.Deal;
import game_mechanics.GameReferee;
import game_mechanics.Player;
import game_mechanics.PlayerInteraction;
import game_mechanics.Property;
import game_mechanics.Realty;

/**
 * 
 * @author Tomáš Hromada
 * Třída reprezentuje statického hráče, umělá inteligence není docílena neuronovými sítěmi.
 *
 */
public class ArtificialPlayer implements PlayerInteraction {
	
	private Player player;
	private String code;
	private GameReferee gameReferee;
	private int playerId = 0;

	@Override
	public void onStartGame(Player player, String code) {
		// TODO Auto-generated method stub
		this.player = player;
		this.code = code;
	}

	@Override
	public boolean onTurnStart(Player player) {
		// TODO Auto-generated method stub
		gameReferee.pleasePayMe(code, playerId);
		
		if(player.isInPrison() && player.hasCardPrison()){
			gameReferee.getOutOfJail(code, true);
		}
		if(player.getMoney()>4000){
			findPropertyToBuyHouse();
		}
		return true;
	}
	

	private void findPropertyToBuyHouse(){
		Property pro  =null;
		for(int i = 10; i>=1; i--){
			if(i==2|| i==5){
				continue;
			}
			List<Property> group = player.getGroupProperties(i);
			if(group.size()==3 || (group.size()==2&&(i==1||i==10))){
				int min = 4;
				for(Property pr: group){
					if(pr.getNumberOfHouse()<=min){
						min = pr.getNumberOfHouse();
						pro = pr;
					}
				}
				if(pro!=null&&player.getMoney()-pro.getRealty().getHouseCost()>5000){
					gameReferee.buyHouse(code, player, pro.getRealty().getRealtyID());
					pro=null;
					if(player.getMoney()<6000){
						return;
					}
				}
			}
			
		}
	}
	
	
	private boolean havingAll(Player player, Property property){
		int a = 0;
		boolean balance = true;
		for(Property property2: player.getPropertiesPublic()){
			if(property2.getRealty()!=null&&property2.getRealty().getGroup()==property.getRealty().getGroup()){
				a++;
				if(property.getNumberOfHouse()>property2.getNumberOfHouse()){
					balance= false;
				}
			}
		}
		if(a==3|| a==2&&( property.getRealty().getGroup()==1|| property.getRealty().getGroup()==10)){
			// nechce se mi to negovat
		}else{
			return false;
		}
		return true;
	}

	@Override
	public void setGameReferee(GameReferee gameReferee, int playerID) {
		// TODO Auto-generated method stub
		this.gameReferee = gameReferee;
		this.playerId = playerID;
	}

	@Override
	public boolean doYouWantBuyIt(Player player, Realty property) {
		// TODO Auto-generated method stub
		if(player.getMoney()-property.getPrice()>2000){
			return true;
		}
		return false;
	}

	

	@Override
	public int onAuctionStarts(int player, Property property, int price, boolean start, int lastPlayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean haveYouReadTheCard(Player player, String text) {
		// TODO Auto-generated method stub
		
		gameReferee.iHaveReadTheCard(code);
		return true;
	}

	@Override
	public void youHaveToPayToBank(Player player, int amount) {
		// TODO Auto-generated method stub
		/*if(!gameReferee.payMoneyToBank(code)){
		
			gameReferee.quit(code, playerId);
		}*/
		//Musí mít dostatek penez
		if(amount>player.getMoney()){
			sellHouses(amount-1-player.getMoney());
		}
		
	}

	@Override
	public void walletUsed(Player player, int money, int lastTranslaction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean youHavetToPayTo(Player payer, Player player, int money) {
		// TODO Auto-generated method stub
		/*if(!gameReferee.payRent(code, payer, true)){
			gameReferee.quit(code, playerId);
		}*/
		Deal d = new Deal(payer.getPlayerId(), player.getPlayerId());
		d.setRentOmittedFromDemand();
		//gameReferee.createDeal(code, playerId, d);
		
		if(money>player.getMoney()){
			sellHouses(money-1-player.getMoney());
		}
		return true;
	}
	
	private void sellHouses(int money){
		int sold = 0;
		List<Property>[] groups = new List[10];
		for (int i = 0; i < groups.length; i++) {
			groups[i] = player.getGroupProperties(i + 1);
		}
		for (int i = 0; i < groups.length && sold < money; i++) {
			if (i == 1 || i == 4) {
				continue;
			}
			int pocet = groups[i].size();
			if ((i == 0 || i == 9) && pocet == 2 || pocet == 3) {
				while (sold < money) {
					Property pro = null;
					int max = 0;
					for (Property p : groups[i]) {
						if (p.getNumberOfHouse() > max) {
							pro = p;
							max = p.getNumberOfHouse();
						}
					}
					if (pro != null) {
						sold += pro.getRealty().getHouseCost() / 2;
						gameReferee.sellToBank(code, player, pro, 1);
					} else {
						break;
					}
				}
			}
		}
	}

	@Override
	public boolean dealWasOffered(Player player, Deal deal) {
		// TODO Auto-generated method stub
		//gameReferee.privateDealAnswe(code, playerId, true);
		boolean de = analyzeDeal(deal);
		return de;
	}

	@Override
	public void dealAnswer(Player offer, boolean answer, Deal deal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void isSomeOneTurn(int player, int playerToGo) {
		// TODO Auto-generated method stub
		gameReferee.pleasePayMe(code, playerId);
		gameReferee.noChecking(code, playerId);
		
	}
	@Override
	public boolean needOwnThread(int player) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean analyzeDeal(Deal deal){
		int income = deal.getMoneyOffer();
		int outcome = deal.getMoneyDemand();
		List<Property> playerImagineryProperty = player.getPropertiesPublic();
		int oldPrice = countPrice(playerImagineryProperty);
		
		for(int i = 0; i<deal.getDemandLength(); i++){
			playerImagineryProperty.remove(deal.getDemand(i));
		}
		for(int i = 0; i<deal.getOfferLength(); i++){
			playerImagineryProperty.add(deal.getOffer(i));
		}
		int newPrice = countPrice(playerImagineryProperty);
		int bilance = newPrice-oldPrice-outcome+income;
		if(deal.getRent()==1){
			bilance-=deal.getRentValue();
		}else if(deal.getRent()==-1){
			bilance+=deal.getRentValue();
		}
		return bilance>0;
	}
	
	private int countPrice(List<Property> properties){
		int[] groups = new int[11];
		int[] groupsSize = new int[11];
		for(Property pro:properties){
			if(pro.getRealty()!=null){
				int pVal = pro.getRealty().getPrice();
				if(pro.isMortage()){
					pVal/=2;
				}
				groups[pro.getRealty().getGroup()]+=pVal;
				groupsSize[pro.getRealty().getGroup()]++;
			}
		}
		
		int price = 0; 
		for(int i = 1; i<=10; i++){
			if((i==1||i==10||i==5)){
				if(groupsSize[i]==2){
					price+=groups[i]*3;
				}else if (groupsSize[i]==1){
					price+=groups[i];
				}
			}else if(i==2){
				switch(groupsSize[i]){
					case 1:
						price+=groups[i];
						break;
					case 2:
						price+=(groups[i]*1.5);
						break;
					case 3:
						price+=groups[i]*2;
						break;
					case 4:
						price+=groups[i]*3;
						break;
				}
			}else{
				if(groupsSize[i]==2){
					price+=groups[i]*1.5;
				}else if(groupsSize[i]==3){
					price+=groups[i]*3;
				}else{
					price+=groups[i];
				}
			}
		}
		return price;
	}

	@Override
	public String getPlayersType(Player player) {
		// TODO Auto-generated method stub
		return "Static AI";
	}
}

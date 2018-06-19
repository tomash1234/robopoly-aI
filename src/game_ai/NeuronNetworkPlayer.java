package game_ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import game_mechanics.Deal;
import game_mechanics.GameReferee;
import game_mechanics.Player;
import game_mechanics.PlayerInteraction;
import game_mechanics.Property;
import game_mechanics.Realty;
import nn4ai_tools.Creature;
import nn4ai_tools.NeuralNetwork;
import nn4ai_tools.NeuralNetworkAttr;

public class NeuronNetworkPlayer implements PlayerInteraction {
	public static final int SCHEMAS[][] = new int[][]{{5, 3,1}, {3, 2,1}, {4, 3,1}};
	//public static final int SCHEMAS[][] = new int[][]{{5, 3,1}, {3, 2,1}, {6, 4,2,1}};
	public static final float MIN_WEIGHT = -100;
	public static final float MAX_WEIGHT = 100;
	
	
	
	private Player player;
	private String code;
	private GameReferee gameReferee;
	private int playerId = 0;
	private NeuralNetwork neuralNetworkPropertyWanted;
	private NeuralNetwork neuralNetworkPropertyValueForOwner;
	private NeuralNetwork neuralNetworkHousesBuying;
	

	public NeuronNetworkPlayer(Creature creature) {
		// TODO Auto-generated constructor stub
		NeuralNetworkAttr networkAttrHousesBuying = creature.getNeuralNetworkAttr(1);
		neuralNetworkHousesBuying = new NeuralNetwork(networkAttrHousesBuying.getSchema(), networkAttrHousesBuying.getWeights(), networkAttrHousesBuying.getMin(), networkAttrHousesBuying.getMax());
		
		NeuralNetworkAttr networkAttrValueForOwner = creature.getNeuralNetworkAttr(2);
		neuralNetworkPropertyValueForOwner = new NeuralNetwork(networkAttrValueForOwner.getSchema(), networkAttrValueForOwner.getWeights(), networkAttrValueForOwner.getMin(), networkAttrValueForOwner.getMax());
		
		
		NeuralNetworkAttr networkAttr = creature.getNeuralNetworkAttr(0);
		neuralNetworkPropertyWanted = new NeuralNetwork(networkAttr.getSchema(), networkAttr.getWeights(), networkAttr.getMin(), networkAttr.getMax());
	}

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
		gameReferee.tossTheDice(code, player);
		
		findPropertyToBuyHouse();
		
		trading();
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
		float a = resolveNeuronNetworkPropertyWanted(gameReferee.getGame().getProperty(property.getRealtyID()), false);
		
		return a>=0.5f;
	}

	

	@Override
	public int onAuctionStarts(int player, Property property, int price, boolean start, int lastPlayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean haveYouReadTheCard(Player player, String text) {
		// TODO Auto-generated method stub

		return true;
	}

	@Override
	public void youHaveToPayToBank(Player player, int amount) {
		// TODO Auto-generated method stub
		//Musí mít dostatek penez
		if(player.getMoney()<amount){
			earnMoney(amount-player.getMoney(), -1);
		}
		
	}
	
	private void findPropertyToBuyHouse(){
		if(player.getMoney()<=1000){
			return;
		}
		Property pro  =null;
		List<PropertyAndValue> candidates = new ArrayList<>();
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
				if (pro != null && pro.getRealty().getHouseCost() < player.getMoney()) {
					
					
					candidates.add(new PropertyAndValue(pro, chanceToBuyAHouse(min, i, pro.getRealty().getHouseCost())));
					pro = null;
				}
			}
			
		}
		Collections.sort(candidates, new Comparator<PropertyAndValue>() {

			@Override
			public int compare(PropertyAndValue o1, PropertyAndValue o2) {
				// TODO Auto-generated method stub
				return Float.compare(o1.getF(), o2.getF());
			}
		});
		for(int i = candidates.size()-1; i>=0; i--){
			if(player.getMoney()>candidates.get(i).getPro().getRealty().getHouseCost()){
				gameReferee.buyHouse(code, player, candidates.get(i).getPro().getRealty().getRealtyID());
			}else if(player.getMoney()<1000){
					break;
			}
		}
		
	}
	static class PropertyAndValue{
		private Property pro;
		private float f;
		private int i;
		private float a;
		
		
		public PropertyAndValue(Property pro, float f) {
			super();
			this.pro = pro;
			this.f = f;
		}
		public PropertyAndValue(Property pro, float iwant, float hwants, int owner) {
			super();
			this.pro = pro;
			this.i = owner;
			this.f = iwant;
			this.a = hwants;
		}
		
		public PropertyAndValue(Property pro, float f, int i ){
			super();
			this.i = i;
			this.pro = pro;
			this.f = f;
		}
		public float getF() {
			return f;
		}
		public Property getPro() {
			return pro;
		}
		public int getI() {
			return i;
		}
		public float getA() {
			return a;
		}
		public void setA(float a) {
			this.a = a;
		}
	}
	
	
	private float resolveNeuronNetworkPropertyWanted(Property property, boolean owned){
		float input[] = new float[neuralNetworkPropertyWanted.getNumberOfNeuronsInLayer(0)];
		float price = 0;
		if(property.getType()!=Property.CARD){
			price = property.getRealty().getPrice();
		}
		input[0] = Tools.valueForPlayer(playerId, property, gameReferee.getGame().getAllPropertyPublic());
		float maxWanted = 0;
		for(int i = 0; i<gameReferee.getGame().getNumberOfPalyers(); i++){
			if(i==playerId || !gameReferee.getGame().getPlayer(i).isActive()){
				continue;
			}
			float a =  Tools.valueForPlayer(i, property, gameReferee.getGame().getAllPropertyPublic());
			if(a>maxWanted){
				maxWanted = a;
			}
		}
		input[1] =maxWanted;
		input[2] = Tools.priceValue(property);
		float rateMoney = 0;
		if(player.getMoney()>0 && price !=0){
			if(price>player.getMoney()){
				rateMoney = 1;
			}else{
				rateMoney = price/player.getMoney();
			}
		}
		input[3] = owned?1f-rateMoney:rateMoney;
		input[4] = 1.f/32* player.getPropertiesPublic().size();
		
		float[] o =neuralNetworkPropertyWanted.resolve(input);
		
		return o[0];
	}
	

	
	
	private float resolveNeuronNetworkPropertyWantedAnotherPlayer(Property property, int playerTo, boolean owned){
		float input[] = new float[neuralNetworkPropertyValueForOwner.getNumberOfNeuronsInLayer(0)];
		float price = 0;
		if(property.getType()!=Property.CARD){
			price = property.getRealty().getPrice();
		}
		input[0] = Tools.valueForPlayer(playerTo, property, gameReferee.getGame().getAllPropertyPublic());
		input[1] = Tools.priceValue(property);
		float rateMoney = 0;
		Player player = gameReferee.getGame().getPlayer(playerTo);
		if(player.getMoney()>0 && price !=0){
			if(price>player.getMoney()){
				rateMoney = 1;
			}else{
				rateMoney = price/player.getMoney();
			}
		}
		input[2] = owned?1f-rateMoney:rateMoney;
		input[3] = 1.f/32* player.getPropertiesPublic().size();
		float[] o = neuralNetworkPropertyValueForOwner.resolve(input);
		return o[0];
	}
	
	
	
	private void trading(){
		List<PropertyAndValue> wishList = new ArrayList<>();
		
		for(int i = 0; i<gameReferee.getGame().getNumberOfPalyers(); i++){
			if(i==playerId){
				continue;
			}
			for(Property pro: gameReferee.getGame().getPlayer(i).getPropertiesPublic()){
				if(pro.getNumberOfHouse()==0&& pro.getRealty()!=null){
					float f = resolveNeuronNetworkPropertyWanted(pro, false);
					if(f>0.8f){
						wishList.add(new PropertyAndValue(pro, f, i));
					}
				}
				
			}
		}
		wishList.sort(new Comparator<PropertyAndValue>() {

			@Override
			public int compare(PropertyAndValue o1, PropertyAndValue o2) {
				// TODO Auto-generated method stub
				return Float.compare(o2.getF(), o1.getF());
			}
		});
		if(wishList.size()>0){
			Random random = new Random();
			int rnd = random.nextInt(wishList.size());
			createTrade(wishList.get(rnd).getPro(), wishList.get(rnd).getF());
		}
	}
	
	private void createTrade(Property property, float v){
		//int m = countPropertyMoneyValue(property, v);
		List<Property> p = new ArrayList<Property>();
		p.add(property);
		
		//offerDeal(property.getOwner(), p, 0, null,  property.getRealty().getPrice()+1500);
		offerDealACountPrice(property, countPropertyMoneyValue(property, v), property.getOwner() ,v );
		//choosePropertyToTrading(property, countPropertyMoneyValue(property, v), createListWithValues(player.getPropertiesPublic(), property.getOwner()), property.getOwner());
	}
	
	private List<PropertyAndValue> createListWithValues(List<Property> allMyProperties, int playerTo){
		List<PropertyAndValue> pv = new ArrayList<>();
		for(Property property : allMyProperties){
			if(property.getNumberOfHouse()>0){
				continue;
			}
			float myRate = resolveNeuronNetworkPropertyWanted(property, true);
			if(myRate<0.6f){
				float hisRate = resolveNeuronNetworkPropertyWantedAnotherPlayer(property, playerTo, false);	
				float d= hisRate-myRate;
				pv.add(new PropertyAndValue(property, myRate, hisRate, playerTo));
			}
			
		}
		pv.sort(new Comparator<PropertyAndValue>() {

			@Override
			public int compare(PropertyAndValue o1, PropertyAndValue o2) {
				// TODO Auto-generated method stub
				return Float.compare(o2.getF(), o1.getF());
			}
		});
		return pv;
	}
	
	private void tryToSellAndSave(int money, int playerTo ){
		List<PropertyAndValue> vals= createListWithValues(player.getPropertiesPublic(), playerTo);
		vals.sort(new Comparator<PropertyAndValue>() {

			@Override
			public int compare(PropertyAndValue o1, PropertyAndValue o2) {
				// TODO Auto-generated method stub
				return Float.compare(o2.getA(), o1.getA());
			}
		});
		int m = 0;
		List<Property> toSells= new ArrayList<>();
		for(PropertyAndValue pv: vals){
			toSells.add(pv.getPro());
			m+= pv.getA();
			if(m>money){
				break;
			}
		}
		int a = 0;
		int deficit = money-m;
		if(deficit>0){
			if(deficit>player.getMoney()){
				a+= player.getMoney();
			}else{
				a+=deficit;
			}
		}
		offerDeal(playerTo, toSells, a, true);
	}
	
	private void offerDealACountPrice(Property property, int maxPrice, int playerTo, float myWant){
		float a =resolveNeuronNetworkPropertyWantedAnotherPlayer(property, property.getOwner(), true);
		int hisMinPrice= countPropertyMoneyValue(property, a);
		if(hisMinPrice>maxPrice){
			return;
		}
		
		int price = hisMinPrice;
	
		if(price==0){
			return;
		}
		List<Property> toBuyList = new ArrayList<>();
		toBuyList.add(property);
		List<Property> toSell 	= new ArrayList<>();
		int money = 0;
		//if(player.getMoney()<10000+hisMinPrice){
			money = findSomethingToSell(createListWithValues(player.getPropertiesPublic(), playerTo), price, toSell);
		//}
		int p =  price-money;
		if(p<0){
			p=0;
		}
		offerDeal(playerTo, toBuyList, 0, toSell, p);
	}
	
	private int findSomethingToSell(List<PropertyAndValue> myPropertySell, int money,List<Property> toSell  ){
	
		int mo = 0;
		for(PropertyAndValue pv: myPropertySell){
			if(pv.getF()>pv.getA()){
				continue;
			}
			toSell.add(pv.getPro());
			mo+= countPropertyMoneyValue(pv.getPro(), pv.getA());
			if(mo>money){
				break;
			}
		}
		return mo;
	}
	
	
	
	private boolean offerDeal(int playerTo, List<Property> toBuy, int moneyDemand,  List<Property> toSell, int moneyOffer){
		Deal deal = new Deal(playerId, playerTo);
		if(toBuy!=null){
			for(Property pro:toBuy){
				deal.addDemand(pro);
			}
		}
		if(toSell!=null){
			for(Property pro:toSell){
				deal.addOffer(pro);
			}
		}
		deal.setMoneyDemand(moneyDemand);
		deal.setMoneyOffer(moneyOffer);
		
		return gameReferee.createDeal(code, playerId, deal);
		
	}
	private boolean offerDeal(int playerTo, List<Property> toSell, int moneyOffer, boolean omit){
		Deal deal = new Deal(playerId, playerTo);
		deal.setRentOmittedFromDemand();
		if(toSell!=null){
			for(Property pro:toSell){
				deal.addOffer(pro);
			}
		}
		deal.setMoneyOffer(moneyOffer);
		
		return gameReferee.createDeal(code, playerId, deal);
		
	}
	

	
	private float chanceToBuyAHouse(int houses, int group, int housePrice){
		float[] in = new float[3];
		in[0] = 1f/5*houses;
		in[1]= Tools.priceValue(group);
		in[2] = 1f*housePrice / player.getMoney();
		
		return neuralNetworkHousesBuying.resolve(in)[0];
	}
	
	private boolean buyAHouse(int houses, int group, int housePrice){
		float[] in = new float[3];
		in[0] = 1f/5*houses;
		in[1]= Tools.priceValue(group);
		in[2] = 1f*housePrice / player.getMoney();
		
		return neuralNetworkHousesBuying.resolve(in)[0]>0.5f;
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
		/*Deal d = new Deal(payer.getPlayerId(), player.getPlayerId());
		d.setRentOmittedFromDemand();*/
		//gameReferee.createDeal(code, playerId, d);
		
		if(player.getMoney()<money){
			earnMoney(money-player.getMoney()+1, gameReferee.getPlayerIdDemandTo());
		}
		
		return true;
	}

	@Override
	public boolean dealWasOffered(Player player, Deal deal) {
		// TODO Auto-generated method stub
		//gameReferee.privateDealAnswe(code, playerId, true);
		
		return analyzeDeal(deal);
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
	
	private float propertyValue(Property property){
		float input[] = new float[neuralNetworkPropertyWanted.getNumberOfNeuronsInLayer(0)];
		float price = 0;
		if(property.getType()!=Property.CARD){
			price = property.getRealty().getPrice();
		}
		input[0] = Tools.valueForPlayer(playerId, property, gameReferee.getGame().getAllPropertyPublic());
		float maxWanted = 0;
		for(int i = 0; i<gameReferee.getGame().getNumberOfPalyers(); i++){
			if(i==playerId || !gameReferee.getGame().getPlayer(i).isActive()){
				continue;
			}
			float a =  Tools.valueForPlayer(i, property, gameReferee.getGame().getAllPropertyPublic());
			if(a>maxWanted){
				maxWanted = a;
			}
		}
		input[1] =maxWanted;
		input[2] = Tools.priceValue(property);
		float rateMoney = 0;
		if(player.getMoney()>0 && price !=0){
			if(price>player.getMoney()){
				rateMoney = 1;
			}else{
				rateMoney = price/player.getMoney();
			}
		}
		input[3] = rateMoney;
		input[4] = 1-1.f/32* player.getPropertiesPublic().size();
		
		float[] o =neuralNetworkPropertyWanted.resolve(input);
		return (float) countPropertyMoneyValue(property ,o[0]);
	}
	
	public static int countPropertyMoneyValue(Property property, float wanting){
		if(property.getRealty()==null){
			return 0;
		}
		return (int) (/*(int)Math.pow(*/property.getRealty().getPrice() *(wanting*2f));//);
	}
	
	private boolean analyzeDeal(Deal deal){
		float myProfit = 0;
		
		for(int i = 0; i<deal.getDemandLength(); i++){
			myProfit -= propertyValue(deal.getDemand(i));
			/*itsProfit += propertyValue(deal.getDemand(i), gameReferee.getGame().getPlayer(deal.getPlayerWhoOffer()).getMoney(), 
					Utils.valueForPlayer(deal.getPlayerWhoOffer(), deal.getDemand(i), gameReferee.getGame().getAllPropertyPublic()));*/
		}
		for(int i = 0; i<deal.getOfferLength(); i++){
			myProfit += propertyValue(deal.getOffer(i));
			/*itsProfit -= propertyValue(deal.getOffer(i), gameReferee.getGame().getPlayer(deal.getPlayerWhoOffer()).getMoney(), 
					Utils.valueForPlayer(deal.getPlayerWhoOffer(), deal.getOffer(i), gameReferee.getGame().getAllPropertyPublic()));*/
		}
		int money = deal.getMoneyOffer()-deal.getMoneyDemand();
		int bilance = 0;
		if(deal.getRent()==1){
			bilance-=deal.getRentValue();
		}else if(deal.getRent()==-1){
			bilance+=deal.getRentValue();
		}
		return myProfit+money+bilance>0;
		
	}
	private void earnMoney(int demand, int playerTo){
		int a = sellHouses(demand);
		if(a<demand&& playerTo!=-1){
			tryToSellAndSave(demand-a, playerTo);
		}
	}
	private int sellHouses(int money){
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
		return sold;
	}
	
	@Override
	public String getPlayersType(Player player) {
		// TODO Auto-generated method stub
		return "NeuralNetwork AI";
	}
	
}

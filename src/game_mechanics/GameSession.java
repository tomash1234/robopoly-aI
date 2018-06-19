package game_mechanics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class GameSession implements /* GameReferee,*/ GraphicsRespond{
	
	/*public static final int TOSS_THE_DICE = 1;
	public static final int ASKING_IF_BUY_BANK = 2;
	public static final int ASKING_IF_CARD_READ = 3;
	public static final int ASKING_FOR_MONEY_TO_BANK = 4;
	public static final int ASKING_FOR_RENT = 5;*/
	
	
	public static final int GAME_INFO_GAME_STARTED = 0;
	public static final int GAME_INFO_START_MOVING = 1;
	public static final int GAME_INFO_STOP_ROLLING = 2;
	public static final int GAME_INFO_ARRIVED_ON_PLAT = 3;
	public static final int GAME_INFO_PROPERTY_SOLD = 4;
	public static final int GAME_INFO_PLAYER_PASS_THE_START = 5;
	public static final int GAME_INFO_CARD_FLIP = 6;
	public static final int GAME_INFO_END_OF_GAME= 9;
	
	
	public static final int NUMBER_OF_FIELDS = 40;
	
	
	private Player[] players;
	private Game game;
	private String[] codes;
	private int playerToGo = 0;
	private Random random1 = new Random();
	private Random random2 = new Random();
	private GameInfo gameInfo;
	private int doubleInRows;
	
	private int lastPosition;
	private Realty toBuy;
	private boolean asnwer;
	private boolean payForMoving= false;
	private int lastDiceNumber = 0;
	private int round = 1;
	private boolean checkable = false;
	private Deal deal;
	private List<Player> playersOrder = new ArrayList<>();
	private int mainPlayer=-1;
	
	private Auction auction;
	//private boolean playerQuitGame=false;
	private boolean[] checkedYourPlats;
	private boolean[] pelaseCheckMyPlats;
	private RespondManager respondManager;
	private	boolean waitToReach;
	
	
	public  GameSession(Game game, GameInfo gameInfo, PlayerInteraction playerInteraction) {
		// TODO Auto-generated constructor stub
		this.players = game.getPlayers();
		//this.playerInteraction = playerInteraction;
		this.game = game;
		this.gameInfo = gameInfo;
		respondManager = new RespondManager( players, playerInteraction, this);
		gameInfo.setGraphicsRespond(this);
		gameInfo.setGameReferee(respondManager);
		
	} 
	
	public void setMainPlayer(int mainPlayer) {
		this.mainPlayer = mainPlayer;
	}
	
	/**
	 * Meota začne hru, vygeneruje kódy pro hráče,
	 * odstartuje RespondManager
	 * odstartuje herní smyčku 
	 */
	protected void startGame(){
		codes = new String[players.length];
		for(int i = 0; i<codes.length; i++){
			codes[i] = generateRandomString();
			/*playerInteraction.setGameReferee(this, i);
			playerInteraction.onStartGame(players[i],codes[i]);*/
		}
		respondManager.start(codes);
		/*
		RespondManger
		*/
		playerToGo= 0;
		
		gameLoop();
	}
	
	public synchronized Game getGame() {
		// TODO Auto-generated method stub
		return this.game;
	}
	
	/**
	 * Inicializuje tah hráče, Pozn: pokud hráč hází znovu, je to povážováno za stejný tah
	 * zkontroluje zda není přechozí hráč povinen platit nájemné
	 */
	private void initPlayerTurn(){
		doubleInRows =0;
		//playerQuitGame = false;
		checkedYourPlats = new boolean[players.length];
		pelaseCheckMyPlats = new boolean[players.length]; 
	
		
		for(int i = 0; i<players.length; i++){
			checkedYourPlats[i] = !players[i].isActive();
		}
		
		/*Thread t3 = new Thread(){
			public void run() {
				playerInteraction.isSomeOneTurn(playerToGo);;
			};
		};
		t3.start();*/
		
		/*synchronized (game) {
			try {
				game.wait(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("END");*/
			for(int i = 0; i<players.length; i++){
				//if(pelaseCheckMyPlats[i]){
				if(players[i].isActive()){
					checkFor(i);
				}
				//}
			}
		//}
		
		
		
	}
	/**
	 * Herní smyčka, která se stará o pořadí hračů a kontroluje konec hry
	 */
	private void gameLoop() {
		// while(true){
		a: for (int i = 0; i < 400; i++) {

			for (int j = 0; j < players.length; j++) {
				if (!players[j].isActive()) {
					continue;
				}
				playerToGo = j;
				game.getStats().rounds(playerToGo);
				initPlayerTurn();
				pleasePlay(players[playerToGo]);
				if (isEnd()) {
					break a;
				}
			}
			
			round++;
		}
		finalEnd();
		game.getStats().playerWon(playersOrder.get(playersOrder.size()-1).getPlayerId());
		gameInfo(GAME_INFO_END_OF_GAME, null, 0, 0, 0, null);
		System.out.println("End of game " + round);
		/*
		 * break; }
		 */
	}
	
	/**
	 * Metoda se volá po skončení hry, vyhodnotí hru
	 * Pokud hra je určena předčasně, spočítají se peníze vloží se do playersOrderu
	 */
	private void finalEnd(){
		List<Player> pl = new ArrayList<>();
		for(int i = 0; i<players.length; i++){
			if(players[i].isActive()){
				game.getStats().setTotalMoney(i, players[i].countValueOfProperty());
				pl.add(players[i]);

			}
		}
		pl.sort(new Comparator<Player>() {

			@Override
			public int compare(Player o1, Player o2) {
				// TODO Auto-generated method stub
				return Integer.compare(o1.countValueOfProperty(), o2.countValueOfProperty());
			}
		});
		playersOrder.addAll(pl);
		
		for(int i = 0; i<playersOrder.size(); i++){
			game.getStats().endGame(playersOrder.get(i).getPlayerId(), playersOrder.size()-i);
		}
		
		
	}
	
	/**
	 * Kontroluje, zda ve hře zůstali alespon dva hráči
	 * @return true - pokud je konec
	 */
	private boolean isEnd (){
		int a = 0;
		for(int i = 0; i<players.length; i++){
			if(players[i].isActive()){
				a++;
			}
			
		}
		if(mainPlayer!=-1&&!players[mainPlayer].isActive()){
			return true;
		}
		return a<=1;
	}
	/**
	 * Metoda, která se vykoná potom co se hráč dostane na políčko, jsou odeslány GameInfo
	 * Metoda dále vynohodnotí pollíčka
	 */
	private void arrivedOnPlat(){
		//gameInfo.onArriveOnPlat(game.getPlat(players[playerToGo].getPosition()));
		gameInfo(GAME_INFO_ARRIVED_ON_PLAT, game.getPlat(players[playerToGo].getPosition()), 0, 0,0, null);
		if((players[playerToGo].getPosition()<lastPosition|| payForMoving)&& players[playerToGo].getPosition()!=10){
			//GET PAID
			payForMoving= false;
			game.getBank().passStart(players[playerToGo].getWallet());
			gameInfo(GAME_INFO_PLAYER_PASS_THE_START, null, playerToGo, 0,0, null);
		}
		
		//
		Plat plat = game.getPlat(players[playerToGo].getPosition());
		game.getStats().stepOnPlat(playerToGo, plat.getId());
		Player player = players[playerToGo];
		switch(plat.getPlatType()){
		case Plat.BUYABLE:
			isOnBuyablePlat(plat, player);
			break;
		case Plat.CHANCE:
			Card card = game.getCardDeckChance().takeCard();
			//gameInfo(GAME_INFO_CARD_FLIP, null, CardDeck.CHANCE, 0, 0, card.getText());
			playerStepedOnCard(card,player, game.getCardDeckChance());
			//nextMove();
			break;
		case Plat.FINANCY:
			Card card2 = game.getCardDeckFinancy().takeCard();
			playerStepedOnCard(card2,player,game.getCardDeckFinancy());
			//game.getCardDeckFinancy().addCard(card2);
			
			//nextMove();
			break;
		case Plat.GO_TO_JAIL:
			doubleInRows = 0;
			goToPlat(player, 10);
			game.getStats().prison(playerToGo);
			
			player.goToPrison();
			//nextMove();
			break;
		case Plat.TAX:
			//nextMove();
			requestForPayment(player,plat.getValue());
			
			break;
		}
		
		
		if(doubleInRows>0){
			/*if(playerQuitGame){
				playerQuitGame = false;
				//waitFor=0;
				return;
			}*/
			pleasePlay(player);
		}
	}
	
	/*private void takeACart(int chance, Player player){
		
	}*/
	/**
	 * Pokud hráč dorazí na pole, které obsahuje kupovatelnou nemovitost
	 * @param plat
	 * @param player
	 */
	private void isOnBuyablePlat(Plat plat, Player player){
		if(game.getBank().isAvailable(plat.getId())){
			toBuy = game.getBank().getProperty(plat.getId()).getRealty();
			/*synchronized (game) {
				waitFor = ASKING_IF_BUY_BANK;
				asnwer = false;
				Thread t = new Thread(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						playerInteraction.doYouWantBuyIt(player,toBuy);
					}
				};
				t.start();
				
				try {
					game.wait(10000);
					waitFor = 0;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			int a = respondManager.waitForBuying(player.getPlayerId(), toBuy);
			
			if(a==RespondManager.PLAYER_OK){
				if(buyRealtyFromBank(toBuy, player)){
					//gameInfo.onPropertySold(toBuy, player.getPlayerId());
					gameInfo(GAME_INFO_PROPERTY_SOLD, null,  player.getPlayerId(),0,0,null);
					return;
				}
			}else if(a==RespondManager.PLAYER_QUIT){
				//createAuction(-1, null, game.getProperty(plat.getId()),1);
				//waitFor=0;
				quit(player.getPlayerId(), false,0);
				return;
			}
			//
			//System.out.println("Aukce začala");
			//createAuction(-1, null, game.getProperty(plat.getId()),1);
			//respondManager.createAuction(-1, null,  game.getBank().getProperty(plat.getId()), 1);
			
		}else{
			if(player.hasRealty(plat.getId())){
				//jestli máš všechny
				
			}
			checkable = true;
			asnwer = false;
			
			//wait
			if(asnwer){
				//player 0 - musí zaplatit majetili, nájem
				
			} 
			//zaplat
		}
	}
	/**
	 * Hráč stoupnu na políčku , vem si kartu
	 * @param card
	 * @param player
	 * @param cardDeck
	 */
	private void playerStepedOnCard(Card card, Player player, CardDeck cardDeck){

		//waitFor = ASKING_IF_CARD_READ;
		gameInfo(GAME_INFO_CARD_FLIP, null, card.isChance()?CardDeck.CHANCE:CardDeck.FINANCY, 0, 0, card.getText());
		
		/*Thread t = new Thread(){
			public void run() {
				playerInteraction.haveYouReadTheCard(player, card.getText());
			};
		}; t.start();
		
		if(waitFor!=0){
			synchronized (game) {
				try {
					System.out.println("WAIT FOR CARD");
					game.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}*/
		int a = respondManager.waitToRead(player.getPlayerId(), card.getText());
		//
		if(a==RespondManager.PLAYER_QUIT){
		//	waitFor=0;
			
			cardDeck.addCard(card);
			quit(player.getPlayerId(), false,0);
			return;
		}
		
		switch (card.getCardType()) {
		case GIFT_FROM_BANK:
			game.getBank().getBankWallet().payMoneyTo(player.getWallet(), card.getValue1());
			game.getStats().moneyGotFromBank(player.getPlayerId(), card.getValue1());
			break;
		case GET_OF_JAIL:
			Property card_getOfJail = new Property(card);
			player.addProperty(card_getOfJail);
			//card goes to player
			return;
		case GO_TO_JAIL:
			goToPlat(player, 10);
			player.goToPrison();
			game.getStats().prison(playerToGo);
			doubleInRows = 0;
			break;
		case MOVE_TO_FIELD:
			int newLoc = card.getValue1();
			if(newLoc<0){
				newLoc = player.getPosition()+newLoc;
				if(newLoc<0){
					newLoc = NUMBER_OF_FIELDS+newLoc;
				}
			}
			if(player.getPosition()>newLoc || newLoc==0){
				payForMoving = true;
			}
			goToPlat(player, newLoc);
			break;
		case PAID_FOR_HOUSE_TO_BANK:
			requestForPayment(player, card.getValue1()*(player.getHouses()-5*player.getHotels())+card.getValue2()*player.getHotels());
			break;
		case PAID_TO_BANK:
			requestForPayment(player, card.getValue1());
			break;
			
		}

	
		
		cardDeck.addCard(card);
	}
	
	
	/*private void nextMove(){
		if(doubleInRows==0){
			playerToGo = (playerToGo+1)%players.length;
			play();
		}else{
			pleasePlay(players[playerToGo]);
		}
	}*/
	
	/**
	 * Hra vyzívá hráče, aby hodil kostkou
	 * @param player
	 */
	private void pleasePlay(Player player){
		
		
		//waitFor = TOSS_THE_DICE;
		if(!player.isActive()){
			return;
		}
		lastPosition = player.getPosition();
		
			Thread t2 = new Thread(){
				public void run() {
					gameInfo.onPlayerTurnStart(playerToGo);
				};
			};
			t2.start();
		/*Thread t1 = new Thread(){
			public void run() {
				playerInteraction.onTurnStart(player);
			};
		};
		t1.start();
		
		synchronized (game) {
		try {

			System.out.println("WAIT FOR TOSS");
			game.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}*/

			int a = respondManager.waitForTosting(playerToGo);
		if(a==RespondManager.PLAYER_QUIT){
			System.out.println("Respond leave game");
			checkable = false;
			//waitFor=0;
			quit(playerToGo, false,0);
			return;
		}
		checkable = false;
	//	waitFor=0;
		go();
	}
	
	private void payBail(){
		requestForPayment(players[playerToGo], 1000);
		players[playerToGo].releaseFromPrison();
	}
	 
	private String generateRandomString(){
		return UUID.randomUUID().toString();
	}
	

	/*@Override
	public synchronized boolean pleasePayMe(String code, int owner) {
		// TODO Auto-generated method stub
		if(code==null||!code.equals(codes[owner])/*|| waitFor!=TOSS_THE_DICE *//*|| doubleInRows!=0 ||  !players[owner].isActive()){
	/*		return false;
		}
		pelaseCheckMyPlats[owner] = true;
		return true;
		
	}*/
	
	private  void checkFor(int owner){
		int playerL = playerToGo-1;

			if(playerL<0 ){
				playerL = players.length-1;
			}
			int p = playerL;
			while(!players[p].isActive()){
				p--;
				if(p==-1){
					p = players.length-1;
				}
				if(p==playerL){
					return;
				}
			}
			if(p==owner){
				return ;
			}
			 checkPlaces(owner, p);
	}
	
	private boolean checkPlaces(int owner, int visitor){
		//if(playerToGo!=(visitor+1)%players.length){
			if(!checkable){
				return false;
			}
		//}
			
		int field = players[visitor].getPosition();
		for(Property property: players[owner].getProperties()){
			if(property.getRealty()!=null&&property.getRealty().getRealtyID()==field){
				int moneyDemand = getRentPrice(field);//property.getRealty().getRentPrice(property.getNumberOfHouse());
				if(moneyDemand==0){
					return false;
				}
				game.getStats().addMoneyErned(property.getRealty().getRealtyID(), moneyDemand);
				//int moneyDemanFor = visitor;
				//int moneyDemanOwner = property.getOwner();
				/*
				waitFor = ASKING_FOR_RENT;

				System.out.println("WAIT FOR RENT");
				playerInteraction.youHavetToPayTo(players[visitor], players[owner], moneyDemand);
				if(waitFor==ASKING_FOR_RENT){
					synchronized (game) {
						try {
							game.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out.println("AFTER NOTIFY");
					}
				}
*/
				int i = respondManager.waitToPayToPlayer(visitor, owner, moneyDemand);
				
				if(i==RespondManager.PLAYER_QUIT){

					 game.getStats().setElemination(owner);
					quit(visitor, true, owner);
				}
				
				
				/*if(property.getRealty().getGroup()==5){
					moneyDemand = lastDiceNumber*80;
				}
				
				if(property.getRealty().getGroup()!=2&&ownAll(players[owner], property)){
					if(property.getRealty().getGroup()==5){
						moneyDemand = lastDiceNumber*200;
					}else{
						moneyDemand*=2;
					}
					
				}else if(property.getRealty().getGroup()==2){
					moneyDemand= howManyTrainStations(players[owner])*1000;
				}*/
				

				return true;
			}
		}
		return false;
	}
	
	/**
	 * Metoda spočítá kolik činí najemná na určité políčko
	 * @param platId
	 * @return
	 */
	public synchronized int getRentPrice(int platId){
		Property property= null;
		Player owner= null;
		a:for(Player pl:players){
			for(Property property2: pl.getProperties()){
				if(property2.getRealty()!=null&& property2.getRealty().getRealtyID()==platId){
					owner = pl;
					property= property2;
					break a;
				}
			}
		}
		if(property==null|| property.isMortage()){
			return 0;
		}
		
		int money = property.getRealty().getRentPrice(property.getNumberOfHouse());
		if(property.getRealty().getGroup()==5){
			money = lastDiceNumber*80;
		}
		
		if(property.getRealty().getGroup()!=2&&ownAll(owner, property)){
			if(property.getRealty().getGroup()==5){
				money = lastDiceNumber*200;
			}else if(property.getNumberOfHouse()==0){
				money*=2;
			}
			
		}else if(property.getRealty().getGroup()==2){
			money= howManyTrainStations(owner)*1000;
		}
		return money;
	}
	
/*
	@Override
	public synchronized boolean tossTheDice(String code, Player player) {
		// TODO Auto-generated method stub
		if(code==null||!code.equals(codes[playerToGo])||/* waitFor!=TOSS_THE_DICE|| *//*player.getPlayerId()!=playerToGo){
			return false;
		}
		
		synchronized (game) {
			game.notify();
		}
		
		return true;
		
	}*/
	
	/**
	 * Meotda simuluju hození kostkou
	 */
	private void go(){
		int dice[] = tossDice();
		
		lastDiceNumber = dice[0] + dice[1];
		if(players[playerToGo].isInPrison()){
			players[playerToGo].roundInPrison();
		}
		if(dice[0]==dice[1]){ //double
			if(players[playerToGo].isInPrison()){
				players[playerToGo].releaseFromPrison();
			}else{

				game.getStats().doubles(playerToGo);
				doubleInRows ++;
			}
		}else{
			if(players[playerToGo].isInPrison()){
				if(players[playerToGo].getNumberOfRound()==3){
					payBail();
				}else{
					gameInfo(GAME_INFO_STOP_ROLLING, null, dice[0], dice[1],doubleInRows,null);
					return;
				}
			}
			doubleInRows=0;
		}
		//gameInfo.onDiceStoppedRolling(dice[0], dice[1], doubleInRows);
		gameInfo(GAME_INFO_STOP_ROLLING, null, dice[0], dice[1],doubleInRows,null);
		if(doubleInRows>=3){
			doubleInRows=0;
			goToPlat(players[playerToGo], 10);
			players[playerToGo].goToPrison();

			game.getStats().prison(playerToGo);
			return;
		}
		goToPlat(players[playerToGo], (players[playerToGo].getPosition()+(dice[0]+dice[1]))%NUMBER_OF_FIELDS);

		
	}
	/**
	 * Metoda přesouvá hráčovo figurku na určité políčko
	 * @param player
	 * @param plat
	 */
	private void goToPlat(Player player , int plat){
		player.setPosition(plat);
		
		synchronized (game) {
			try {
				
				gameInfo(GAME_INFO_START_MOVING, null, playerToGo, players[playerToGo].getPosition(),0,null);

				//waitFor = WAIT_REACH;
				game.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		arrivedOnPlat();
	}
	/**
	 * Generovat co padne na kostkách
	 * @return pole s délkou dva
	 */
	private int[] tossDice(){
		return new int[]{random1.nextInt(6)+1, random2.nextInt(6)+1};
	}
	
	/**
	 * Player is trying to buy a realty from bank
	 * @param realty realty to buy
	 * @param player buyer
	 * @return TRUE if trade was completed
	 */
	private boolean buyRealtyFromBank(Realty realty, Player player){
		Property pro = game.getBank().sellRealty(realty, player.getWallet());
		if(pro!=null){
			player.addProperty(pro);
			return true;
		}
		return false;
	}
/*
	@Override
	public synchronized boolean answerToDeal(String code, boolean deal) {
		// TODO Auto-generated method stub
		if(code==null||!code.equals(codes[playerToGo])/*|| waitFor!=ASKING_IF_BUY_BANK){
		//	return false;
		}
		/*if(waitFor==ASKING_IF_BUY_BANK){
			waitFor = 0;*/
			//players[playerToGo];
			/*if(deal){
				if(buyRealtyFromBank(toBuy, players[playerToGo])){
					nextMove();
					return true;
				}
			}
			//start auktion*/
			/*synchronized (game) {
				asnwer = deal;
				game.notify();
				System.out.println("KOUPENO ");
			}
			return true;
		/*}
		return false;
		
	}*/

	/**
	 * Vyvtoří žádost na hráče, aby zaplatit bance
	 * @param player
	 * @param amount
	 */
	private  void requestForPayment(Player player, int amount){
		//this.moneyDemand = amount;
		if(amount==0){
			return;
		}
		/*
		waitFor=  ASKING_FOR_MONEY_TO_BANK;
		Thread t = new Thread(){
			public void run() {
				playerInteraction.youHaveToPayToBank(player, amount);
			};
		}; t.start();
		
		synchronized (game) {
			try {

				System.out.println("WAIT FOR MONEY BANK");
				game.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		int a = respondManager.waitToPayToBank(player.getPlayerId(), amount);
		if(a!=RespondManager.PLAYER_OK){
			System.out.println("Respond failed = " + a);
			quit(player.getPlayerId(), false,0);
		}
		//co když nezaplatí
		
	}





	@Override
	public synchronized void onFigureReachedRightPlace() {
		// TODO Auto-generated method stub
		synchronized (game) {
			/*if(waitFor==WAIT_REACH){
				waitFor = 0;*/
			if(waitToReach){
				game.notify();
				waitToReach = false;
			}
			//}
		}
	}
	
	public void gameInfo(int code, Plat val1, int val2, int val3, int val4,String t){
		if(code==GAME_INFO_START_MOVING){
			waitToReach = true;
		}
		if(!gameInfo.gameInfoAll()){
			if(GAME_INFO_END_OF_GAME!=code&&GAME_INFO_START_MOVING!=code){
				return;
			}
		}
		Thread th = new Thread(){
			public void run() {
				switch(code){
				case GAME_INFO_ARRIVED_ON_PLAT:
					gameInfo.onArriveOnPlat(val1);
					break;
				case GAME_INFO_CARD_FLIP:
					gameInfo.onCardFlip(val2, t);
					break;
				case GAME_INFO_GAME_STARTED:
					gameInfo.onGameStarted("n");
					break;
				case GAME_INFO_PLAYER_PASS_THE_START:
					gameInfo.onPlayerPassStart(val2);
					break;
				case GAME_INFO_PROPERTY_SOLD:
					gameInfo.onPropertySold(toBuy, val2);
					break;
				case GAME_INFO_START_MOVING:
					
					gameInfo.onStartMovingFigure(val2, val3);
					break;	
				case GAME_INFO_STOP_ROLLING:
					gameInfo.onDiceStoppedRolling(val2, val3, val4);
						break;
				/*case GAME_INFO_DEAL_ACCEPTED:
					gameInfo.onDealAccepted(deal);
						break;
				case GAME_INFO_DEAL_REFUSED:
					gameInfo.onDealRefused(deal);
						break;*/
				case GAME_INFO_END_OF_GAME:
					gameInfo.onEndOfGame(game.getStats(), playersOrder);
						break;
				
				
				}
			};
		};
		th.start();
		
	}

/*
	@Override
	public synchronized boolean iHaveReadTheCard(String code) {
		// TODO Auto-generated method stub
		if(code==null||!code.equals(codes[playerToGo])/*|| waitFor!=ASKING_IF_CARD_READ){
			/*return false;
		}
		//waitFor =0;
		synchronized (game) {
			/*game.notify();
			System.out.println("NOTI READ  ");
		}
		return true;
	}
*/
/*
	@Override
	public synchronized boolean payMoneyToBank(String code) {
		// TODO Auto-generated method stub
		if(code==null||!code.equals(codes[playerToGo])/*|| waitFor!=ASKING_FOR_MONEY_TO_BANK){
		//	System.out.println("Reqsute odmítnut : " +  (!code.equals(codes[playerToGo])) + ", " +  (waitFor!=ASKING_FOR_MONEY_TO_BANK));
			/*
			return false;
		}
		if(players[playerToGo].getWallet().payMoneyTo(game.getBank().getBankWallet(), moneyDemand)==Wallet.TRANSACTIONS_OK){
			game.getStats().moneyPaidToBank(playerToGo, moneyDemand);
			//waitFor = 0;
			moneyDemand =0;
			synchronized (game) {
				game.notify();
				System.out.println("NOTI PAID  ");
			}
		}else{
			return false;
		}
		return true;
	}
*/
/**
 * Umožňuje hráči se předčasně dostat z vězení
 * @param code
 * @param card - true hráč chce využít kartu, false - hráč zaplatí
 * @return
 */
	public synchronized boolean getOutOfJail(String code, boolean card) {
		// TODO Auto-generated method stub
		if(code==null||!code.equals(codes[playerToGo])/*|| waitFor!=TOSS_THE_DICE */){
			return false;
		}
		if(!players[playerToGo].isInPrison()){
			return false;
		}
		if(card){
			Card c = players[playerToGo].takeCard();
			if(c!=null){
				if(c.isChance()){
					game.getCardDeckChance().addCard(c);
				}else{
					game.getCardDeckFinancy().addCard(c);
				}
				players[playerToGo].releaseFromPrison();
				return true;
			}
			
		}else{
			if(players[playerToGo].getWallet().payMoneyTo(game.getBank().getBankWallet(), 1000)==Wallet.TRANSACTIONS_OK){
				players[playerToGo].releaseFromPrison();
				return true;
			}else{
				return false;
			}
		}
		return false;
	}


	//prodá dům bance
	/**
	 * Hráč prodá bance nemovitost
	 * @param code
	 * @param player
	 * @param property
	 * @return
	 */
	public synchronized boolean sellToBank(String code, Player player, Property property) {
		// TODO Auto-generated method stub
		if(code==null||!code.equals(codes[player.getPlayerId()])  || !player.isActive()){
			return false;
		}
		if(property.getRealty()!=null&&player.hasRealty(property.getRealty().getRealtyID())&& property.getNumberOfHouse()==0){
			if(game.getBank().getBankWallet().payMoneyTo(player.getWallet(), property.getRealty().getHouseCost()/2)==Wallet.TRANSACTIONS_OK){
				Property pro = player.getProperty(property.getRealty().getRealtyID());
				game.getBank().returnProperty(pro);
				return true;
			}
		}
		return true;
	}


	/**
	 * Hráč prodá jeden dům , ne nemovitostosti
	 * @param code
	 * @param player
	 * @param property
	 * @param houses
	 * @return
	 */
	public synchronized boolean sellToBank(String code, Player player, Property property, int houses) {
		// TODO Auto-generated method stub
		if(code==null||!code.equals(codes[player.getPlayerId()])  || !player.isActive()){
			return false;
		}
		if(property.getRealty()!=null&&player.hasRealty(property.getRealty().getRealtyID())){
			for(Property property2: player.getProperties()){
				if(property2.getRealty()!=null&&property2.getRealty().getGroup()==property.getRealty().getGroup()){
					if(property2.getNumberOfHouse()>property.getNumberOfHouse()){
						return false;
					}
				}
			}
			if(property.getNumberOfHouse()>0){
				if(game.getBank().getBankWallet().payMoneyTo(player.getWallet(), property.getRealty().getHouseCost()/2)==Wallet.TRANSACTIONS_OK){
					property.removeHouses(1);

					game.getStats().sellHousesValue(player.getPlayerId(), property.getRealty().getHouseCost()/2);
					game.getGraphicsController().setHouse(property.getRealty().getRealtyID(), property.getNumberOfHouse());
					return true;
				}
			}
		}
		return false;
	}

/**
 *  Metoda uvalí na nemovitost, hypotéku
 * @param code
 * @param player
 * @param property
 * @param sell
 * @return
 */
	public synchronized boolean mortgage(String code, Player player,Property property, boolean sell) {
		// TODO Auto-generated method stub
		if(code==null||!code.equals(codes[player.getPlayerId()]) || !player.isActive()){
			return false;
		}
		if(sell){
			if(property.getRealty()!=null&&player.hasRealty(property.getRealty().getRealtyID())&& property.getNumberOfHouse()==0){
				if(game.getBank().getBankWallet().payMoneyTo(player.getWallet(), property.getRealty().getMortgage())==Wallet.TRANSACTIONS_OK){
					 property.setMortage(true);
					 return true;
				}
			}
		}else{
			if(property.getRealty()!=null&&player.hasRealty(property.getRealty().getRealtyID())&& property.isMortage()){
				if(player.getWallet().payMoneyTo(game.getBank().getBankWallet(), (int)(property.getRealty().getMortgage()*1.1))==Wallet.TRANSACTIONS_OK){
					 property.setMortage(false);
					 return true;
				}
			}
		}
		return false;
	}

	/**
	 * Metoda zjistí zda hráč vlastní monopol, od určité skupiny
	 * @param player
	 * @param property
	 * @return
	 */
	public  static boolean ownAll(Player player,Property property){
		int a = 0;
		
		for(Property property2: player.getProperties()){
			if(property2.getRealty()!=null&&property2.getRealty().getGroup()==property.getRealty().getGroup()){
				a++;
			}
		}
		if(a==3|| a==2&&( property.getRealty().getGroup()==1|| property.getRealty().getGroup()==10)){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * Vrací počet škol (tranerů), kterých vlastní hráč
	 * @param player
	 * @return
	 */
	public int howManyTrainStations(Player player){
		int a = 0;
		
		for(Property property2: player.getProperties()){
			if(property2.getRealty()!=null&&property2.getRealty().getGroup()==2){
				a++;
			}
		}
		return a;
	}

	/**
	 * Hráč koupí jeden dům na svoji určitou nemovitost
	 * @param code
	 * @param player
	 * @param propertyId
	 * @return
	 */
	public synchronized boolean buyHouse(String code, Player player, int propertyId) {
		// TODO Auto-generated method stub
		if(code==null||!code.equals(codes[player.getPlayerId()])){
			return false;
		}
		Property property = null;
		for(Property property2: player.getProperties()){
			if(property2.getRealty()!=null && property2.getRealty().getRealtyID()==propertyId){
				property = property2;
				break;
			}
		}
	
		if(property==null||property.getRealty()==null|| !player.hasRealty(property.getRealty().getRealtyID()) || property.isMortage() || property.getRealty().getRentPrice(4)==0){
			System.err.println("Nenalezen nebo rent 0, nebo v mortag");
			return false;
		}
		//having all houses from group
		int a = 0;
		boolean balance = true;
		for(Property property2: player.getProperties()){
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
		if(!balance || property.getNumberOfHouse()==5){
			return false;
		}
		
		if(player.getWallet().payMoneyTo(game.getBank().getBankWallet(), property.getRealty().getHouseCost())==Wallet.TRANSACTIONS_OK){
			property.addHouses();
			game.getStats().buyHousesValue(player.getPlayerId(), property.getRealty().getHouseCost());
			game.getGraphicsController().setHouse(property.getRealty().getRealtyID(), property.getNumberOfHouse());
			return true;
		}
		
		

		return false;
	}


	
	/*public synchronized boolean payRent(String code, Player payer, boolean money) {
		// TODO Auto-generated method stub
		/*if(code==null || !code.equals(codes[payer.getPlayerId()])/* || waitFor!=ASKING_FOR_RENT || payer.getPlayerId()!= moneyDemanFor*//* ){
			return false;
		}*/
		/*if(money){
			if( players[payer.getPlayerId()].getWallet().payMoneyTo(players[moneyDemanOwner].getWallet(), moneyDemand)==Wallet.TRANSACTIONS_OK){
				rentPaid();
				synchronized (game) {
					//waitFor = 0;
					game.notify();

					System.out.println("NOTI RECHED  PAID RENT");
				}
				return true;
			}else{
				System.err.println("Problém");
				
			}
		}
		
		return false;
	}*/


	public int getPlayerToGo() {
		// TODO Auto-generated method stub
		return playerToGo;
	}



	public int getGameRound() {
		// TODO Auto-generated method stub
		return round;
	}

	/*
	public boolean createDeal(String code, int owner, Deal deal){
		if( deal==null){
			//ERROR_DEAL_IS_NULL;
			return false;
		}
		if(code==null || !code.equals(codes[owner])|| deal.getPlayerWhoOffer()!=owner||!players[owner].isActive() ||!players[deal.getDealTo()].isActive() || deal.getDealTo()==deal.getPlayerWhoOffer()){ 
			//ERROR_INVALID_USER_OR_CODE
			return false;
		}
		
		if( this.deal!=null){
		//ERROR_CURRENT_DEAL_WASNT_FINISHED	;
			return false;
		}
		if(deal.getMoneyOffer()>players[deal.getPlayerWhoOffer()].getMoney()&&deal.getMoneyDemand()>players[deal.getDealTo()].getMoney()){
			//ERROR_NOT_ENOUGH_MONEY;
		
			return false;
		}
		
		for(int i = 0;i<deal.getOfferLength(); i++){
			if(deal.getPlayerWhoOffer()!=deal.getOffer(i).getOwner() || (deal.getOffer(i).getRealty()!=null && deal.getOffer(i).getNumberOfHouse()>0)){
				//ERROR_DEAL_PROPERTY_DOESNT_OWN;
				return false;
			}
		}
		for(int i = 0;i<deal.getDemandLength(); i++){
			if(deal.getDealTo()!=deal.getDemand(i).getOwner() || deal.getDemand(i).getNumberOfHouse()>0){
				//ERROR_DEAL_PROPERTY_DOESNT_OWN;
				return false;
			}
		}
		this.deal = deal;
		deal.confirmO();
		playerInteraction.dealWasOffered(players[deal.getDealTo()], deal);
		
		return true;
	}*/

	/*
	@Override
	public boolean privateDealAnswe(String code, int demander, boolean answer) {
		// TODO Auto-generated method stub
		if(code==null || !code.equals(codes[demander]) || deal==null || !players[demander].isActive()){
			return false;
		}
		
		if(answer){
			gameInfo(GAME_INFO_DEAL_ACCEPTED, null, 0, 0, 0, null);
			deal.confirmD();
			commitDeal(deal);
		}else{
			gameInfo(GAME_INFO_DEAL_REFUSED, null, 0, 0, 0, null);
			deal = null;
		}
		return true;
	}
	*/
	/*private void rentPaid(){
		game.getStats().moneyGotFromPlayer(moneyDemanOwner,moneyDemand);
		game.getStats().moneyPaidToPlayer(moneyDemanFor,moneyDemand);
		moneyDemand = 0;
		moneyDemanFor = -1;
		moneyDemanOwner = -1;
		//waitFor = TOSS_THE_DICE;
		checkable = false;
		playerInteraction.onTurnStart(players[playerToGo]);
	}*/
	
	/**
	 * Metoda provede Deal
	 * @param deal
	 */
	protected synchronized void commitDeal(Deal deal){
		Player of = players[deal.getPlayerWhoOffer()];
		Player de = players[deal.getDealTo()];
		for(int i = 0; i<deal.getOfferLength(); i++){
			if(deal.getOffer(i).getType()==Property.CARD){
				de.addProperty(new Property(of.takeCard()));
			}else{
				Property po = of.getProperty(deal.getOffer(i).getRealty().getRealtyID());
				po.setOwner(de.getPlayerId());
				if(po.isMortage()){
					if(de.getWallet().payMoneyTo(game.getBank().getBankWallet(), (int) (po.getRealty().getPrice()*0.1))==Wallet.TRANSACTIONS_OK){
						de.addProperty(po);
					}else{
						of.addProperty(po);
					}
				}else{
					de.addProperty(po);
				}
			}
		}
		for(int i = 0; i<deal.getDemandLength(); i++){
			if(deal.getDemand(i).getType()==Property.CARD){
				of.addProperty(new Property(de.takeCard()));
			}else{
				Property po = de.getProperty(deal.getDemand(i).getRealty().getRealtyID());
				po.setOwner(of.getPlayerId());
				if(po.isMortage()){
					if(of.getWallet().payMoneyTo(game.getBank().getBankWallet(), (int) (po.getRealty().getPrice()*0.1))==Wallet.TRANSACTIONS_OK){
						of.addProperty(po);
					}else{
						de.addProperty(po);
					}
				}else{
					of.addProperty(po);
				}
				
			}
		}
	
		
		/// money
		int mo = deal.getMoneyDemand()-deal.getMoneyOffer();
		if(mo>0){
			if(de.getWallet().payMoneyTo(of.getWallet(), mo)==Wallet.TRANSACTIONS_FAIL_NOT_ENOUGH_MONEY){
				de.getWallet().payMoneyTo(of.getWallet(), de.getMoney());
			}
		}else if(mo<0){
			if(of.getWallet().payMoneyTo(de.getWallet(), -mo)==Wallet.TRANSACTIONS_FAIL_NOT_ENOUGH_MONEY){
				of.getWallet().payMoneyTo(de.getWallet(), of.getMoney());
			}
		}
		
		//rent
		/*if(waitFor==ASKING_FOR_RENT && deal.getRent()!=0){

			if(deal.getRent()==-1 && deal.getPlayerWhoOffer()==moneyDemanOwner  && deal.getDealTo()==moneyDemanFor){
				rentPaid();
				
			}else if(deal.getRent()==1 && deal.getPlayerWhoOffer()==moneyDemanFor  && deal.getDealTo()==moneyDemanOwner){
				rentPaid();
			}
		}*/
		System.out.println(of.getName() + "has : "+  of.getProperties() + ", " + de.getName() + "has : "+  de.getProperties());
		deal = null;
		
	}
	/*@Override
	public int getPlayerIdDemandFrom() {
		// TODO Auto-generated method stub
		return moneyDemanFor;
	}
	@Override
	public int getPlayerIdDemandTo() {
		// TODO Auto-generated method stub
		return moneyDemanOwner;
	}
	@Override
	public int getMonayDamand() {
		// TODO Auto-generated method stub
		return moneyDemand;
	}*/


	/*@Override
	public synchronized boolean quit(String code, int player) {
		// TODO Auto-generated method stub
		if(code==null || !code.equals(codes[player]) ||!players[player].isActive()){
			return false;
		}
		System.err.println("Quiiiit 1");
		//quit(player);
		return true;
	}*/
	
	/**
	 * Metoda zajistí opuštění hráče ze hry, vyplacení a vyrovnání 
	 * @param player
	 * @param rent
	 * @param moneyOwner
	 */
	protected void quit(int player, boolean rent, int moneyOwner){

		System.out.println("Player quits");
		sellAllHouses(player);
		takeCards(player);
		 if(rent){
			players[player].getWallet().payMoneyTo(players[moneyOwner].getWallet(), players[player].getMoney());
			givePropertiesAway(player, moneyOwner);
			//waitFor= TOSS_THE_DICE;
			//playerQuitGame = true;
			//moneyDemand = 0;
			//moneyDemanFor = -1;
			//moneyDemanOwner =-1;

			/*synchronized (game) {
			//	waitFor = TOSS_THE_DICE;
				//game.notify();

				//System.out.println("NOTI END GAME  ");
			}*/
		}else{
			players[player].getWallet().payMoneyTo(game.getBank().getBankWallet(), players[player].getMoney());
			givePropertiesAway(player, -1);
			if(playerToGo==player){
			//	playerQuitGame = true;
				//moneyDemand = 0;
				//moneyDemanFor = -1;
				//moneyDemanOwner =-1;

				/*synchronized (game) {
					//waitFor = TOSS_THE_DICE;
					//game.notify();
					System.out.println("NOTI END GAME  2");
				}*/
			}
		}
			System.out.println("Pstats");
		 
		 game.getStats().playerQuit(player, rent?1:0);
		players[player].inactive();
		playersOrder.add(players[player]);
		
		game.getStats().playerEnd(player, round);
		System.out.println("ends");
		
	}
	private void sellAllHouses(int player){
		for(Property property: players[player].getProperties()){
			sellPropertyHouses(property, player);
		}
		
	}
	private void givePropertiesAway(int player, int to){
		List<Property> propertes = new ArrayList<>();
		for(Property pro:players[player].getProperties()){
			propertes.add(pro);
			
			pro.setOwner(-1);
		}
		players[player].noPropertyies();

		for (Property pro : propertes) {
			if (to == -1) {
				game.getBank().returnProperty(pro);
			}else{
				if(pro.isMortage()){
					if(players[to].getWallet().payMoneyTo(game.getBank().getBankWallet(), pro.getRealty().getPrice())==Wallet.TRANSACTIONS_OK){
						players[to].addProperty(pro);
					}else{
						game.getBank().getBankWallet().payMoneyTo(players[to].getWallet(), pro.getRealty().getPrice()/2);
						game.getBank().returnProperty(pro);
					}
				}else{
					players[to].addProperty(pro);
				}
			}
		}
		
	}
	
	private void sellPropertyHouses(Property property, int player){
		if(property.getNumberOfHouse()>0){
			game.getBank().getBankWallet().payMoneyTo(players[player].getWallet(), property.getNumberOfHouse()*property.getRealty().getHouseCost()/2);
			property.setNumberOfHouse(0);
		}
	}
	private void takeCards(int player){
		while(players[player].hasCardPrison()){
			Card c = players[player].takeCard();
			if(c!=null){
				if(c.isChance()){
					game.getCardDeckChance().addCard(c);
				}else{
					game.getCardDeckFinancy().addCard(c);
				}
			}else{
				return;
			}
		}
	}
	public List<Player> getPlayersOrder() {
		return playersOrder;
	}
/*
	@Override
	public boolean bid(String code, Player player, int value) {
		// TODO Auto-generated method stub
		if(code==null || !code.equals(codes[player.getPlayerId()])|| value<1){
			return false;
		}
		if(auction!=null){
			if(player.getMoney()>=auction.getMoney()+value){
				if(!auction.bid(player.getPlayerId(), value)){
					return false;
				}
			}
		}
		System.out.println(auction.getMoney() +">>");
		playerInteraction.onAuctionPriceRised(player.getPlayerId(), auction.getMoney());
		return false;
	}*/


	public GameReferee getGameReferee() {
		// TODO Auto-generated method stub
		return respondManager;
	}

/*
	@Override
	public synchronized boolean createAuction(int player, String code, Property propterty, int price) {
		// TODO Auto-generated method stub
		if(code==null || !code.equals(codes[player])){
			return false;
		}
		if(propterty.getOwner()!=-1){
			if(propterty.getOwner()!=player || code!=codes[player] ){
				return false;
			}
		}
		auction = new Auction(propterty,price);
		playerInteraction.onAuctionStarts(propterty, price);
		return true;
	}

*/
	/*
	@Override
	public synchronized boolean noChecking(String code, int player) {
		// TODO Auto-generated method stub
		if(code==null || !code.equals(codes[player])){
			return false;
		}
		checkedYourPlats[player] = true;
		boolean all = true;
		for(boolean a :checkedYourPlats){
			if(!a){
				all= false;
				break;
			}
		}
		if(all){
			synchronized (game) {
				
				game.notify();

				System.out.println("NOTI END NOCHECKING  ");
			}
		}
		
		return true;
	}*/

}

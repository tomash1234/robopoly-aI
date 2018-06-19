package board_gui;

import java.util.List;

import game_mechanics.Deal;
import game_mechanics.GameInfo;
import game_mechanics.GameReferee;
import game_mechanics.GraphicsRespond;
import game_mechanics.Plat;
import game_mechanics.Player;
import game_mechanics.PlayerInteraction;
import game_mechanics.Property;
import game_mechanics.Realty;
import game_mechanics.RespondManager;
import game_mechanics.Stats;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

public class HumanPlayer implements PlayerInteraction, GameInfo{
	
	private RightPlayerPanel panel;
	private String code[];
	private GameReferee gameReferee;
	private GameAnimation gameAnimation;
	private Player players[];
	//private LeftPanel leftPanel;
	private int playerId=0;
	private int currentPlayerId;
	private  int moneyDamand = 0;
	private DealMaker dealMaker;
	private AuctionDialog auctionDialog;
	private Player playerTo;
	
	public HumanPlayer(GameAnimation gameAnimation){
		this.gameAnimation = gameAnimation;
	}
	public void setPanel(RightPlayerPanel panel, LeftPanel leftPanel) {
		this.panel = panel;
	//	this.leftPanel =leftPanel;
	}
	public void setPlayers(Player[] players) {
		this.players = players;
		code = new String[players.length];
	}
	public void noHumanPlayers(){
		panel.noPlayers();
	}
	@Override
	public void onGameStarted(String info) {
		// TODO Auto-generated method stub
		
	}

	public boolean isInPlayers(int i){
		if(players.length==0){
			return false;
		}
		for(Player p: players){
			if(p.getPlayerId()==i){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void isSomeOneTurn(int player, int playerToGo) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGraphicsRespond(GraphicsRespond graphicsRespond) {
		// TODO Auto-generated method stub
		gameAnimation.setGraphicsRespond(graphicsRespond);
		
	}

	@Override
	public void onStartMovingFigure(int playerId, int destination) {
		// TODO Auto-generated method stub

		gameAnimation.startToMoving(playerId, destination);
	}

	public Player getPlayer() {
		return players[currentPlayerId];
	}
	@Override
	public void onDiceStoppedRolling(int val1, int val2, int doubleRows) {
		// TODO Auto-generated method stub
		panel.setNumbers(val1, val2, doubleRows);
	}

	@Override
	public void onArriveOnPlat(Plat plat) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPropertySold(Realty realty, int player) {
		// TODO Auto-generated method stub
		
		//System.out.println(realty + " was sold to Player " + player);
		if(players.length==0){
			return;
		}
		if(player==playerId){
			panel.buyNewRealty(gameReferee.getGame().getProperty(realty.getRealtyID()));
		}
	}
	public GameReferee getGameReferee() {
		return gameReferee;
	}
	private int playerIndexFromId(int id){
		for ( int i = 0; i<players.length; i++){
			if(players[i].getPlayerId()==id){
				return i;
			}
		}
		return -1;
	}

	@Override
	public void onPlayerPassStart(int player) {
		// TODO Auto-generated method stub
//
	}

	@Override
	public void onCardFlip(int type, String text) {
		// TODO Auto-generated method stub
		Platform.runLater(new Runnable() {
			public void run() {
				showDialogCard(type, text);
			};
		});
	}

	@Override
	public void onStartGame(Player player,String code) {
		// TODO Auto-generated method stub
	
		int i = playerIndexFromId(player.getPlayerId());
		if(i==-1){
			return;
		}
	
		this.code[i] = code;
		
	}

	
	@Override
	public boolean onTurnStart(Player player) {
		// TODO Auto-generated method stub

		//System.out.println("The turn has started");
		
	
		currentPlayerId =playerIndexFromId(player.getPlayerId());
		if(currentPlayerId==-1){
			return false;
		}
		panel.setNextHumanPlayer(this);
		
		/*if(gameReferee.getGame().getPlayer(playerId).isInPrison()){
			panel.setPrisonCard(gameReferee.getGame().getPlayer(playerId).hasCardPrison());
		}*/
		return false;
	}



	@Override
	public void setGameReferee(GameReferee gameReferee, int playerId) {
		// TODO Auto-generated method stub
		this.gameReferee = gameReferee;
		this.playerId = playerId;
	}

	@Override
	public boolean doYouWantBuyIt(Player player,Realty property) {
		// TODO Auto-generated method stub

		

		Platform.runLater(new Runnable() {
			public void run() {
				showDialogRealty(property);
			};
		});
		return false;
	}
	
	private void showDialogRealty(Realty realty){
		
		OptionDialog dialog = new OptionDialog("Nákup nemovitosti", "Chcete koupit " + realty.getName() + " za " + realty.getPrice() + " BTC?", true);
		
		boolean a = dialog.showAndWait();

		if (a) {
		    gameReferee.answerToDeal(code[currentPlayerId], true);
		}else{
		    gameReferee.answerToDeal(code[currentPlayerId], false);
		   }
	}
	
	private void showDialogCard(int type, String text){
		OptionDialog dialog = new OptionDialog(type==0?"Náhoda": "Finance", text, false);
		Thread t = new Thread(){
			public void run() {
				try {
					
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Platform.runLater(new Runnable() {
					public void run() {
						dialog.close(false);
					}
				});
			}
			
		};
		
		if(playerIndexFromId(gameReferee.getPlayerToGo())==-1){
			t.start();
		}
		dialog.showAndWait();
		if(gameReferee!=null){
			gameReferee.iHaveReadTheCard(code[currentPlayerId]);
		}
		
		

	}

	

	public void toss() {
		// TODO Auto-generated method stub
		boolean a =gameReferee.tossTheDice(code[currentPlayerId], players[currentPlayerId]);
		
	}  
	@Override
	public boolean haveYouReadTheCard(Player player,String text) {
		// TODO Auto-generated method stub
		//
		return false;
		
	}
	@Override
	public void youHaveToPayToBank(Player player,int amount) {
		moneyDamand = amount;
		playerTo=null;
		panel.setMoneyToPay(amount, false);
		
	}
	@Override
	public void walletUsed(Player player,int money, int lastTranslaction) {
		// TODO Auto-generated method stub
		panel.setMoney(money);
	}
	public boolean pay() {
		if(moneyDamand>0&&playerTo!=null){
			boolean ok = gameReferee.payRent(code[currentPlayerId], players[currentPlayerId], true);
			if(ok){
				moneyDamand = 0;
				playerTo =null;
			}
			return ok;
		}
		boolean a = gameReferee.payMoneyToBank(code[currentPlayerId]);
		if(a){
			panel.setMoneyToPayOk();
		}
		return a;
		
	}
	public boolean bait() {
		// TODO Auto-generated method stub
		boolean ok = gameReferee.getOutOfJail(code[currentPlayerId], false);
		if(ok){
			panel.setJailOk();
		}
		return ok;
	}
	public boolean useCard() {
		// TODO Auto-generated method stub
		return gameReferee.getOutOfJail(code[currentPlayerId], true);
		
	}
	public void payMe(){
	
		boolean ok = gameReferee.pleasePayMe(code[currentPlayerId], players[currentPlayerId].getPlayerId());
	}
	public void buyAHouse(){
		
		boolean ok = gameReferee.buyHouse(code[currentPlayerId], players[currentPlayerId], players[currentPlayerId].getPosition());
	}
	
	public int getPlayerHaveToPay(){
		if(gameReferee.getPlayerIdDemandFrom()==players[currentPlayerId].getPlayerId()){
			return gameReferee.getMonayDamand();
		}
		return 0;
	}
	public Player playerPayTo(){
		if(gameReferee.getPlayerIdDemandFrom()==players[currentPlayerId].getPlayerId()){
			
			int p = gameReferee.getPlayerIdDemandTo();
			return gameReferee.getGame().getPlayer(p);
		}
		return null;
	}
	@Override
	public boolean youHavetToPayTo(Player payer,Player player, int money) {
		// TODO Auto-generated method stub
		 this.playerTo = player;
		 this.moneyDamand = money;
		 
		 currentPlayerId = playerIndexFromId(payer.getPlayerId());
		 panel.setNextHumanPlayer(this);
		 return false;
	}	
	
	
	
	public void sellHouseProperty(Property property){
		boolean ok = gameReferee.sellToBank(code[currentPlayerId], players[currentPlayerId], property, 1);
	}
	public void buyHouseOnProperty(Property property){
		boolean ok = gameReferee.buyHouse(code[currentPlayerId], players[currentPlayerId], property.getRealty().getRealtyID());
	}
	public void mortage(Property property) {
		boolean ok = gameReferee.mortgage(code[currentPlayerId], players[currentPlayerId], property, !property.isMortage());
		
	}
	public void bid(int player, int money){
		gameReferee.bid(code[player], players[playerIndexFromId(player)],  money);
		
	}
	@Override
	public boolean dealWasOffered(Player player, Deal deal) {
		// TODO Auto-generated method stub
		 currentPlayerId = playerIndexFromId(player.getPlayerId());

			Platform.runLater(new Runnable() {
				public void run() {
		 panel.setNextHumanPlayer(HumanPlayer.this);
		 DealMaker dealMaker = new DealMaker(deal, new OnDealMadeListener() {
			
			@Override
			public void OnDealMade(Deal deal) {
				// TODO Auto-generated method stub
				gameReferee.privateDealAnswe(code[currentPlayerId], players[currentPlayerId].getPlayerId(), deal!=null);
			}
		});
		 dealMaker.showStage();
				}});
			return false;
	}
	public void opanTradeWindow() {
		if(dealMaker!=null){
			dealMaker.cancel();
		}
		 dealMaker = new DealMaker(gameReferee.getGame(), players[currentPlayerId], new OnDealMadeListener() {
			
			@Override
			public void OnDealMade(Deal deal) {
				// TODO Auto-generated method stub
				gameReferee.createDeal(code[currentPlayerId], players[currentPlayerId].getPlayerId(), deal);
			}
		});
		dealMaker.showStage();
	}

	
	public interface OnDealMadeListener{
		void OnDealMade(Deal deal);
	}



	
	@Override
	public void dealAnswer(Player offer, boolean answer, Deal deal) {
		// TODO Auto-generated method stub
		String playersName = ""+ gameReferee.getGame().getPlayer(deal.getDealTo()).getName();
		boolean bot = gameReferee.getGame().getPlayer(deal.getDealTo()).getPlayerType().toLowerCase().equals("human player");
		if(bot){
			return;
		}
		OptionDialog dialog = new OptionDialog(answer?"Obchod přijmut":"Obchod odmítnut", answer?"Obchod byl hráčem " + playersName+ " přijat.":"Hráč "+ playersName + " odmítnul obchod.", false);
		dialog.show();
		
		
	}
	
	public void quitGame(){
		boolean ok =  gameReferee.quit(code[currentPlayerId], players[currentPlayerId].getPlayerId());
	}

	
	@Override
	public int onAuctionStarts(int player, Property property, int price, boolean start, int lastPlayer) {
		// TODO Auto-generated method stub
		if(start){
			Platform.runLater(new Runnable() {
				public void run() {
					if(auctionDialog==null || !auctionDialog.isShow()){
						auctionDialog = new AuctionDialog(property, gameReferee, HumanPlayer.this,  price);
						auctionDialog.showStage();
					}
				}});
		
		}else{

			Platform.runLater(new Runnable() {
				public void run() {
					auctionDialog.priceRised(price, lastPlayer);
				}});
		}
		
		return 0;
	}
	public void nextMan() {
		// TODO Auto-generated method stub
		if(players.length==0){
			return;
		}
		currentPlayerId++;
		if(currentPlayerId>=players.length){
			currentPlayerId = 0;
		}
		panel.setNextHumanPlayer(this);
	}
	public void previousMan() {
		// TODO Auto-generated method stub
		if(players.length==0){
			return;
		}
		currentPlayerId--;
		if(currentPlayerId<0){
			currentPlayerId = players.length-1;
		}
		panel.setNextHumanPlayer(this);
	}
	@Override
	public void setGameReferee(GameReferee gameReferee) {
		// TODO Auto-generated method stub
		this.gameReferee =gameReferee;
	}
	@Override
	public void onPlayerTurnStart(int player) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onEndOfGame(Stats stats, List<Player> players) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean needOwnThread(int player) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean gameInfoAll() {
		// TODO Auto-generated method stub
		return true;
	}
	public boolean isInPrison() {
		// TODO Auto-generated method stub
		return players[currentPlayerId].isInPrison();
	}
	@Override
	public String getPlayersType(Player player) {
		// TODO Auto-generated method stub
		return "Human Player";
	}
}

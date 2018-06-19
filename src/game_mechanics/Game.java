package game_mechanics;


import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Game {
	
	private Plat[] plats = new Plat[40];
	private GraphicsController graphicsController;
	private Player players[];
	private Thread gameThread;
	private Bank bank;
	private GameSession gameSession;
	private CardDeck cardDeckChance;
	private CardDeck cardDeckFinancy;
	private Stats stats;
	private int groupsNumber[] = new int[11];
	private int mainPlayer = -1;
	
	public Game(GraphicsController graphicsController, PlayerInteraction playerInteraction, GameInfo gameInfo, PrePlayer[] prePlayers, String filePrefs){
		this.graphicsController = graphicsController;
		initGame(filePrefs);
		initPlayers(prePlayers, playerInteraction);
		stats=  new Stats(players.length);
		gameThread = new Thread(){
			
			public void run() {
				
				gameSession = new GameSession(Game.this, gameInfo, playerInteraction);
				if(mainPlayer!=-1){
					gameSession.setMainPlayer(mainPlayer);
				}
				synchronized (bank) { 
					bank.notify();
				}
				gameSession.startGame();
			};
		
		};
		
	}
	public void start(){
	gameThread.start();
			
			synchronized (bank) {
				try {
					bank.wait();
				} catch (InterruptedException e) {   
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	
	}
	
	public void setMainPlayer(int m){
		mainPlayer = m;
		if(gameSession!=null){
			gameSession.setMainPlayer(m);
		}
	}
	public Game(GraphicsController graphicsController, PlayerInteraction playerInteraction, GameInfo gameInfo, PrePlayer[] prePlayers, boolean oneThreaded, String filePrefs){
		this.graphicsController = graphicsController;
		initGame(filePrefs);
		initPlayers(prePlayers, playerInteraction);
		stats=  new Stats(players.length);
		gameSession = new GameSession(Game.this, gameInfo, playerInteraction);
		
		gameSession.startGame();
		
		
		
	}
	public GameReferee getGameReferee(){
		return gameSession.getGameReferee();
	}
	public Plat getPlat(int id){
		return plats[id];
	}
	protected Player[] getPlayers(){
		return players;
	}
	public Player[] getPlayersOut(){
		Player[] pl = new Player[players.length];
		for(int i = 0; i<players.length; i++){
			pl[i] = players[i];
		}
		return pl;
	}
	
	
	public Player getPlayer(int id){
		return players[id];
	}
	public int numberOfPlayers(){
		return players.length;
	}
	public void end(){
		if(gameThread!=null){
			gameThread.stop();
		}
	}
	public GraphicsController getGraphicsController() {
		return graphicsController;
	}
	
	private void initGame(String file){
		graphicsController.initBoard();
		List<Property> realties = new ArrayList<>();
		XMLLoader loader;
		if(file==null||file.length()==0){
			loader= new XMLLoader();
		}else{
			loader = new XMLLoader(file);
		}
		
		
		for (int i = 0; i < plats.length; i++) {
			int type = Plat.getType(i);
			loader.loadPlat(i);
			Color color = loader.getColor();
			String image = loader.getImage();
			String text = loader.getText();
			int value = loader.getValue();
			/*if(type==Plat.BUYABLE){
				value = 1;
			}else if(type==Plat.TAX){
				text = "TAX";
				value = 10;
			}else if(type==Plat.CHANCE){
				text = "NÁHODA";
			}else if(type==Plat.FINANCY){
				text = "FINANCE";
			}else if(type==Plat.START){
				text = "VEM SI 200 BTC"; 
			}*/
			if(type==Plat.BUYABLE){
				groupsNumber[loader.getGroup()] ++;
				Realty realty= new Realty(text, i, value, loader.getRents(), value/2, loader.getHousePrice(), loader.getGroup());
				realties.add(new Property(realty));
			}
			plats[i] = new Plat(i, text, value, color, image, loader.getGroup());
			
			graphicsController.initPlat(i, plats[i].getName(), plats[i].getPrice(), plats[i].getImage(), plats[i].getColor());
			
		}
		cardDeckChance = loader.loadCardDeck(CardDeck.CHANCE);
		cardDeckFinancy= loader.loadCardDeck(CardDeck.FINANCY);
		bank = new Bank(1000000000, realties);
	}
	 
	protected Bank getBank() {
		return bank;
	}
	
	private void initPlayers(PrePlayer[] prePlayers, PlayerInteraction playerInteraction){
		players =  new Player[prePlayers.length];
		for (int i = 0; i < prePlayers.length; i++) {
			players[i] = new Player(i, prePlayers[i].getName(), prePlayers[i].getFigurka(),30000, playerInteraction);
			
		}
		
		//players[1] = new Player(0, "Lucci", 0,30000, null);
		graphicsController.initPlayer(players);
		graphicsController.moveWithFigure(0, 0);
		
	}
	protected void shuffleCardDecks(){
		cardDeckChance.shuffle();
		cardDeckFinancy.shuffle();
	}
	
	protected CardDeck getCardDeckChance() {
		return cardDeckChance;
	}
	protected CardDeck getCardDeckFinancy() {
		return cardDeckFinancy;
	}
	public int getGroupNumber(int group){
		return groupsNumber[group];
	}
	
	public List<Property> getAllPropertyPublic(){
		return bank.getAllProperties();
	}
	
	public Property getProperty(int id){
		for(Player player: players){
			for(Property property: player.getProperties()){
				if(property.getRealty()!=null && property.getRealty().getRealtyID()==id){
					return property;
				}
			}
		}
		return bank.getProperty(id);
		
	}
	public int getNumberOfPalyers(){
		return players.length;
	}
	
	public Stats getStats() {
		return stats;
	}
	public List<Player> getPlayerOrder(){
		return gameSession.getPlayersOrder();
	}
}

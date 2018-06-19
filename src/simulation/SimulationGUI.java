package simulation;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import board_gui.Design;
import board_gui.DesignStage;
import game_mechanics.Deal;
import game_mechanics.GameInfo;
import game_mechanics.GameReferee;
import game_mechanics.GraphicsRespond;
import game_mechanics.Plat;
import game_mechanics.Player;
import game_mechanics.Realty;
import game_mechanics.Stats;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SimulationGUI implements GameInfo{
	
	private DesignStage stage;
	private int numberOfGames ;
	private AtomicInteger gameNumber = new AtomicInteger();
	private GraphicsRespond graphicsRespond;
	private Label games = new Label();
	private ProgressBar pb = new ProgressBar();
	private Stats stats;
	private GridPane gridPane = new GridPane();
	private GameReferee gameReferee;
	private boolean init = false;
	private Label labels [][];
	private VBox vbox = new VBox();
	private EventHandler<WindowEvent> e;
	public SimulationGUI(int numberOfGames) {
		// TODO Auto-generated constructor stub
		this.numberOfGames = numberOfGames;

		
		Platform.runLater(new Runnable() {
			public void run() {
		games.setText("0/" + numberOfGames );
		games.setAlignment(Pos.CENTER);
		String[] data = new String[]{"Hráč", "Výhry", "Skore", "Bankroty (banka)", "Bankroty (hráč)", "Dohrané hry", "Eliminace (celkem)", "Prů. hodnota majetku"};
		init(data);
		//gridPane.setMinWidth(540);
		gridPane.setAlignment(Pos.TOP_CENTER);
		//gridPane.setMinHeight(300);
		//Scene scene = new Scene(gridPane);
		
		labels = new Label[data.length][];
		
			}});
	}
	
	private void initAndShow(){
		pb.setMaxWidth(Double.MAX_VALUE);
		VBox hb = new VBox(pb);
		hb.setFillWidth(true);
		vbox.getChildren().addAll(hb, gridPane);
		vbox.setPadding(new Insets(0, 0, 32, 0));
		stage = new DesignStage("Simulace | RobopolyAI", true, vbox);
		stage.getStage().setOnHiding(new EventHandler<WindowEvent>() {

		      public void handle(WindowEvent event) {
		    	  if(gameReferee.getGame()!=null){
		    		  gameReferee.getGame().end();
		    		  e.handle(null);
		    	  }
		    	  
		      }
		      
		 });
		

		stage.show();
	}
	public void setEnded(EventHandler<WindowEvent> ev){  
		e= ev;
	}
	
	private void init(String[] fields){
		gridPane.setPadding(new Insets(4));
		gridPane.setHgap(30);
		
		gridPane.setVgap(4);
		//Kategorie
		for(int i = 0; i<fields.length; i++){
			Label l = new Label(fields[i]);
			l.setStyle(Design.getNormalTextWhiteItalicCSS() + "-fx-text-fill: #ddd");
			gridPane.add(l, 0, i+2);
			
		}
	//	body.getChildren().addAll(gridPane);
		
		
		
		
	}
	
	private void initGame(){
		if(init){
			return;
		}
		init =true;
		int pl = gameReferee.getGame().getNumberOfPalyers();
		//gridPane.add(pb, 0, 0, pl, 1);
		gridPane.add(games, pl, 0);
		//pb.setPrefWidth(200 +100*(pl-1));
		games.setPrefWidth(100);
		//ridPane.setFillWidth(pb, null);
		for(int i = 0; i<labels.length; i++){
			labels[i] = new Label[pl];
		}
		
		for(int i = 0; i<gameReferee.getGame().getNumberOfPalyers(); i++){
			for(int j = 0; j<labels.length; j++){
				labels[j][i] = new Label();

				labels[j][i].setStyle(Design.getNormalTextWhiteCSS());
				GridPane.setHalignment(labels[j][i], HPos.CENTER);
			}
			Label name = new Label(gameReferee.getGame().getPlayer(i).getName());
			name.setPrefWidth(100);
			name.setStyle(Design.getNormalTextWhiteCSS());
			name.setAlignment(Pos.CENTER);
			gridPane.add(name, i+1, 2);
			
		}
	}
	
	private String formatProcenta(float percent){
		return "(" + Math.floor(percent*10000)/100 +"%)";
	}
	
	private String moneyFormat(int price){
		String s = ""+ price;
		StringBuilder n = new StringBuilder();
		int a = 0;
		for(int i = s.length()-1; i>=0; i--){
			a++; 
			if(a==4){
				n.insert(0, ",");
			}
			n.insert(0, s.charAt(i));
		}
		return n.toString() + " BTC";
	}
	@Override
	public void onGameStarted(String info) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setGraphicsRespond(GraphicsRespond graphicsRespond) {
		// TODO Auto-generated method stub
		this.graphicsRespond = graphicsRespond;
	}

	@Override
	public void onStartMovingFigure(int playerId, int destination) {
		// TODO Auto-generated method stub
		graphicsRespond.onFigureReachedRightPlace();
	}

	@Override
	public void onDiceStoppedRolling(int val1, int val2, int doubleRows) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onArriveOnPlat(Plat plat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPropertySold(Realty realty, int player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerPassStart(int player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCardFlip(int type, String text) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setGameReferee(GameReferee gameReferee) {
		// TODO Auto-generated method stub
		this.gameReferee = gameReferee;
		Platform.runLater(new Runnable() {
			public void run() {
		initGame();
			}});
	}

	@Override
	public void onPlayerTurnStart(int player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void onEndOfGame(Stats stat, List<Player> players) {
		// TODO Auto-generated method stub
		this.gameNumber.incrementAndGet();
		if(this.stats==null){
			this.stats = new Stats(players.size());
		}
		this.stats.merge(stat);
		
		Platform.runLater(new Runnable() {
			public void run() {
				if(stage==null){
					initAndShow();
				}
				int start = 2;
				pb.setProgress(1.0/numberOfGames* gameNumber.get());
				games.setText(gameNumber.get()+"/" + numberOfGames );
				games.setStyle(Design.getNormalTextWhiteCSS());
				for(int i = 0; i<players.size(); i++){
					if(labels[1][i].getText().length()==0){
						gridPane.add(labels[1][i], i+1, 1+start);
						gridPane.add(labels[2][i], i+1, 2+start);
						gridPane.add(labels[3][i], i+1, 3+start);
						gridPane.add(labels[4][i], i+1, 4+start);
						gridPane.add(labels[5][i], i+1, 5+start);
						gridPane.add(labels[6][i], i+1, 6+start);
						gridPane.add(labels[7][i], i+1, 7+start);
						
					}
					labels[1][i].setText("" + stats.getPlayerWins(i) + "   " + formatProcenta(1.0f*stats.getPlayerWins(i)/stats.getGamesPlayed(i)) );
					labels[2][i].setText("" + stats.getScore(i) );
					labels[3][i].setText("" + stats.getReasonQuit(i, 0) + "   " + formatProcenta(1.0f*stats.getReasonQuit(i, 0)/stats.getGamesPlayed(i)) );
					labels[4][i].setText("" + stats.getReasonQuit(i,1) + "   " + formatProcenta(1.0f*stats.getReasonQuit(i, 1)/stats.getGamesPlayed(i)));
					labels[5][i].setText("" + stats.getFinished(i) + "   " + formatProcenta(1.0f*stats.getFinished(i)/stats.getGamesPlayed(i)));
					labels[6][i].setText("" + stats.getEliminations(i) );
					labels[7][i].setText("" + moneyFormat(stats.getTotalMoney(i)/stats.getGamesPlayed(i)) );
				}
				
			}
		});
		System.out.println("---- END OF GAME----" +gameNumber.get() +  "/" + numberOfGames);
		int p = players.size();
		for(Player pl: players){
			System.out.println("" + p + ". \t" + pl.getName() + "\t" +  stats.getTotalMoney(pl.getPlayerId()) + " BTC");
			p--;
		}
		System.out.println(stat);
		
	}

	@Override
	public boolean gameInfoAll() {
		// TODO Auto-generated method stub
		return false;
	}


}

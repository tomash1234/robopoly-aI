package board_gui;

import java.util.List;

import game_mechanics.Deal;
import game_mechanics.Game;
import game_mechanics.GameInfo;
import game_mechanics.GameReferee;
import game_mechanics.GraphicsRespond;
import game_mechanics.Plat;
import game_mechanics.Player;
import game_mechanics.Property;
import game_mechanics.Realty;
import game_mechanics.Stats;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class LeftPanel extends BorderPane implements GameInfo{
	
	private Game game;
	private Label round;
	private Label gameInfo;
	private PlayerPanel panels[];
	public LeftPanel(Game game) {
		// TODO Auto-generated constructor stub
		this.game = game;
		setStyle("-fx-background-color: "+Design.DARK_COLOR+";");
		
		round = new Label("Kolo: ?");
		gameInfo = new Label("Na řadě je hráč ");
		round.setStyle(Design.getNormalTextWhiteCSS());
		gameInfo.setStyle(Design.getNormalTextWhiteCSS());
		VBox vbox = new VBox(2);
		vbox.setPadding(new Insets(4));
		vbox.getChildren().addAll(gameInfo, round);
		setPrefWidth(230);
		
		VBox vPlayers = new VBox(8);
		vPlayers.setPadding(new Insets(4));
		panels = new PlayerPanel[game.getNumberOfPalyers()];
		for (int i = 0; i < game.getNumberOfPalyers(); i++) {
			panels[i] = new PlayerPanel(game.getPlayer(i));
			vPlayers.getChildren().add(panels[i].getBorderPane());
		}
		ScrollPane sp = new ScrollPane();
		sp.setStyle("-fx-background: "+Design.LIGHT_BLACK_COLOR+"; -fx-border-color:"+Design.LIGHT_BLACK_COLOR+";");
		
		vPlayers.setStyle("-fx-background-color: "+Design.LIGHT_BLACK_COLOR+";");
		sp.setPadding(new Insets(4));
		sp.setContent(vPlayers);
		setTop(vbox);
		setCenter(sp);
	}
	
	class PlayerPanel{
		
		private Player player;
		private BorderPane borderPane;
		private Button button = new Button("VÍCE");
		private Label data;
		public PlayerPanel(Player player) {
			this.player = player;
			borderPane = new BorderPane();
			setNormal();
			VBox left = new VBox();
			ImageView imageView = new ImageView(new Image(ClassLoader.getSystemResourceAsStream(Figure.IMG_FIG[player.getFigurka()])));
			left.setPadding(new Insets(4));
			imageView.setFitWidth(60);
			borderPane.setPrefWidth(220);
			imageView.setFitHeight(60);
			Label lName = new Label(player.getName());
			lName.setStyle("-fx-text-fill: #505050;" + Design.getFontItalicCSS());
			lName.setPrefWidth(80);
			lName.setTextAlignment(TextAlignment.CENTER);
			left.getChildren().addAll(imageView, lName);
			borderPane.setCenter(left);
			VBox right = new VBox();
			right.setPadding(new Insets(8));
			
			HBox dataBox = new HBox(4);
			Label info = new Label("Majetek\n"
					+ "Domy:\n"
					+ "Peníze:");
			data = new Label("");
			data.setTextAlignment(TextAlignment.RIGHT);
			info.setStyle("-fx-text-fill: #505050;" + Design.getFontItalicCSS());
			data.setStyle("-fx-text-fill: #000000;" + Design.getFontRegularCSS());
			dataBox.getChildren().addAll(info, data);
			right.getChildren().add(dataBox);
			borderPane.setBottom(button);
			borderPane.setRight(right);
			button.setPrefWidth(240);
			update(0);
			button.setStyle(Design.getButtonCSS());
			button.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					PlayerInfoDialog playerInfoDialog = new PlayerInfoDialog(player, game.getStats());
				}
			});
		}
		public BorderPane getBorderPane() {
			return borderPane;
		}
		public void setToGo(){
			borderPane.setStyle("-fx-background-color: #ffcc80;"/*0#D0D060;"*/);
		}
		public void setOut(){
			borderPane.setStyle("-fx-background-color: #A0A0A0;");
		}
		public void setNormal(){
			borderPane.setStyle("-fx-background-color: #E0E0E0;");
		}
		public void update(int to){
			boolean isToGo = to==player.getPlayerId();
					// TODO Auto-generated method stub
					data.setText(player.getNumberOfHousesOwned() + " ("+ player.getNumberOfCardsOwn() +")\n"
							+ "" +player.getHouses() + "h + " + player.getHotels() + "H\n"
							+ "" + PropertyDialog.moneyFormat(player.getMoney())
							+ "" + (player.isInPrison()?"\nVe vězení":""));
					if(!player.isActive()){
						setOut();
					}else if(isToGo){
						setToGo();
					}else{
						setNormal();
					}
				}
		
	}

	public void update() {
		// TODO Auto-generated method stub
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {	

				gameInfo.setText("Na řadě je hráč " + game.getPlayer(game.getGameReferee().getPlayerToGo()).getName());
				round.setText("Kolo: " + game.getGameReferee().getGameRound());
				for(PlayerPanel playerPanel: panels){
					playerPanel.update(game.getGameReferee().getPlayerToGo());
				}
				
			}
			});
	}

	@Override
	public void onGameStarted(String info) {
		// TODO Auto-generated method stub

		update();
	}

	@Override
	public void setGraphicsRespond(GraphicsRespond graphicsRespond) {
		// TODO Auto-generated method stub 
		
	}
 
	@Override
	public void onStartMovingFigure(int playerId, int destination) {
		// TODO Auto-generated method stub
		
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

		update();
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
	public void onPlayerTurnStart(int player) {
		// TODO Auto-generated method stub
		update();
	}
	
	@Override
	public void onEndOfGame(Stats stats, List<Player> players) {
		// TODO Auto-generated method stub
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				GameSummary gS= new GameSummary(players, stats);
				
			}
		});
	}

	@Override
	public void setGameReferee(GameReferee gameReferee) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean gameInfoAll() {
		// TODO Auto-generated method stub
		return true;
	}
}

package board_gui;


import game_mechanics.GameReferee;
import game_mechanics.Player;
import game_mechanics.Property;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AuctionDialog extends BorderPane{
	
	private Stage stage;
	private Label lValue, lPlayer;
	private int value = 0;
	private GameReferee gameReferee;
	
	public AuctionDialog(Property property, GameReferee gameReferee, HumanPlayer humanPlayer, int price) {
		// TODO Auto-generated constructor stub
		this.gameReferee = gameReferee;
		//setTop(PropertyDialog.getVBox(property));
		stage =new Stage();
		stage.setScene(new Scene(this));
		stage.setTitle("Aukce ");
		
		VBox vBoxLabel = new VBox(4);
		lValue = new  Label(PropertyDialog.moneyFormat(price));
		lPlayer = new  Label("-");
		vBoxLabel.getChildren().addAll(lValue, lPlayer);
		setCenter(vBoxLabel);
		
		HBox hbox = new HBox(8);
		for(int i = 0; i <gameReferee.getGame().getNumberOfPalyers(); i++){
			if(humanPlayer.isInPlayers(i)){
				PlayerAuctionPanel auctionPanel = new PlayerAuctionPanel(gameReferee.getGame().getPlayer(i), humanPlayer);
				hbox.getChildren().add(auctionPanel.getVbox());
			}else{
				PlayerAuctionPanel auctionPanel = new PlayerAuctionPanel(gameReferee.getGame().getPlayer(i));
				hbox.getChildren().add(auctionPanel.getVbox());
			}
		}
		setBottom(hbox);
	}
	public void showStage(){
		stage.show();
	}
	public boolean isShow() {
		
		return stage!=null&&stage.isShowing();
	}
	
	public class PlayerAuctionPanel{
		
		private VBox vbox = new VBox(4);
		private Player player;
		private int money = 0;
		public PlayerAuctionPanel(Player player, HumanPlayer hm) {
			//this.player = hm.get;
			this.money = player.getMoney();
			Label label = new Label(player.getName());
			Label labelMoney = new Label(PropertyDialog.moneyFormat(player.getMoney()));
			
			HBox mbox = new HBox(2);
			TextField field = new TextField();
			
			Button bid = new Button("Přihodit");
			bid.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					try{
						int mon = Integer.valueOf(field.getText());
						hm.bid(player.getPlayerId(), mon);
					}catch(Exception e){
						
					}
					
				}
			});
			mbox.getChildren().addAll(field, bid);
			vbox.getChildren().addAll(label, labelMoney, mbox);
		}
			public PlayerAuctionPanel(Player player) {
				this.player = player;
				this.money = player.getMoney();
				Label label = new Label(player.getName());
				Label labelMoney = new Label(PropertyDialog.moneyFormat(player.getMoney()));
				
				HBox mbox = new HBox(2);
				TextField field = new TextField();
				
				/*Button bid = new Button("Přihodit");
				bid.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						try{
							int mon = Integer.valueOf(field.getText());
							gameReferee.bid
						}catch(Exception e){
							
						}
					}
				});
			*/
			mbox.getChildren().addAll(field);
			vbox.getChildren().addAll(label, labelMoney, mbox);
		}
		
		public VBox getVbox() {
			return vbox;
		}
		
		
		
	}

	public synchronized void priceRised(int value, int player) {
		// TODO Auto-generated method stub
		this.value = value;

		Platform.runLater(new Runnable() {
			public void run() {
				lValue.setText(PropertyDialog.moneyFormat(value));
				lPlayer.setText(gameReferee.getGame().getPlayer(player).getName());
			}
		});
	}
}



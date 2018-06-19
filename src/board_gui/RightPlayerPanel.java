package board_gui;

import java.util.ArrayList;
import java.util.List;

import game_mechanics.Player;
import game_mechanics.Property;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class RightPlayerPanel extends VBox {

	private DicePanel dicePanel = new DicePanel();
	private HumanPlayer humanPlayer;
	private Label labelMoney;
	private Label labelPayTo = new Label();
	private Button buttonPay, buttonBait, buttonUseCard, buttonTrade, buttonStats, buttonOut, buttonNextPlayer, buttonPreviousPlayer;
	private List<PropertyPanel> properties = new ArrayList<>();
	private VBox propertyPanels = new VBox(4);
	private Label labelPlayerName;
	private HBox hRequestMoney, hBoxJail;
	private StatsDialog statsDialog;

	public RightPlayerPanel(HumanPlayer humanPlayer) {
		// TODO Auto-generated constructor stub
		this.humanPlayer= humanPlayer;

		setStyle("-fx-background-color: #F0F0F0;");
		
		labelPlayerName = new Label("Tommy");
		
		labelPlayerName.setPrefWidth(200);
		HBox hPlayerName = new HBox(3);
		hPlayerName.setPadding(new Insets(4));
		buttonNextPlayer= new Button(">");
		buttonNextPlayer.setStyle(Design.getButtonCSS());
		buttonPreviousPlayer = new Button("<");
		buttonPreviousPlayer.setStyle(Design.getButtonCSS());
		hPlayerName.getChildren().addAll(labelPlayerName, buttonPreviousPlayer, buttonNextPlayer);
		labelPlayerName.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; "+ Design.getFontMediumCSS());
		hPlayerName.setStyle("-fx-background-color: "+Design.DARK_COLOR+";");
		hPlayerName.setAlignment(Pos.CENTER_LEFT);
		getChildren().add(hPlayerName);
		


		HBox hMoneyPanel = new HBox();
		labelMoney = new Label("30,000 BTC");
		hMoneyPanel.setPadding(new Insets(4));
		labelMoney.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 18px; " +Design.getFontMediumCSS());
		hMoneyPanel.setAlignment(Pos.CENTER);
		hMoneyPanel.setStyle("-fx-background-color: "+Design.DARK_COLOR+";");
		hMoneyPanel.getChildren().add(labelMoney);
		getChildren().add(hMoneyPanel);


		
		setPrefWidth(300); 

		dicePanel.getHbox().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				humanPlayer.toss();
			}
		});
		VBox center = new VBox(0);
		center.setAlignment(Pos.TOP_CENTER);
		VBox.setVgrow(center, Priority.ALWAYS);
		getChildren().add(center);
		
		getChildren().add(dicePanel.getHbox());

		buttonNextPlayer.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				humanPlayer.nextMan();
			}
		});
		buttonPreviousPlayer.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				humanPlayer.previousMan();
			}
		});
		
		buttonPay = new Button("ZAPLAŤ");
		buttonPay.setStyle(Design.getButtonCSS());
		//labelPayTo = new Button("Domluvit se");

		labelPayTo.setStyle(" -fx-text-fill: #ffffff; -fx-font-style: italic; -fx-font-size: 14px;");
		
		buttonPay.setDisable(true);
		buttonPay.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				boolean a = humanPlayer.pay();
				if(a){
					hRequestMoney.setManaged(false);
					hRequestMoney.setVisible(false);

					labelPayTo.setVisible(false);
					labelPayTo.setManaged(false);
					hRequestMoney.setManaged(true);
					hRequestMoney.setVisible(true);
				}
			}
		});

		hRequestMoney= new HBox(4);
		hRequestMoney.setAlignment(Pos.CENTER);
		hRequestMoney.setPadding(new Insets(4));
		hRequestMoney.setStyle("-fx-background-color: #4f4040; ");
		hRequestMoney.getChildren().addAll(buttonPay, labelPayTo);
		center.getChildren().addAll(hRequestMoney);
		hRequestMoney.setVisible(false);
		hRequestMoney.setManaged(false);
		
		
		

		buttonBait = new Button("VYKOUPIT SE");
		buttonBait.setStyle(Design.getButtonSmallCSS());
		buttonBait.setDisable(true);
		buttonBait.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if(humanPlayer.bait()){

					hBoxJail.setVisible(false);
					hBoxJail.setManaged(false);
				}
			}
		});
		

		buttonUseCard = new Button("POUŽÍT KARTU");
		buttonUseCard.setStyle(Design.getButtonSmallCSS());
		buttonUseCard.setDisable(true);
		buttonUseCard.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if(humanPlayer.useCard()){

					hBoxJail.setVisible(false);
					hBoxJail.setManaged(false);
				}
			}
		});
		

		hBoxJail = new HBox(2);
		hBoxJail.setAlignment(Pos.CENTER);
		hBoxJail.setPadding(new Insets(4));
		hBoxJail.setStyle("-fx-background-color: #4f4040; ");
		hBoxJail.getChildren().addAll(buttonBait, buttonUseCard);
		center.getChildren().add(hBoxJail);
		hBoxJail.setVisible(false);
		hBoxJail.setManaged(false);
		
		

		buttonOut= new Button("BANKROT");
		buttonOut.setStyle(Design.getButtonColorCSS());
		buttonOut.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				OptionDialog optionDialog = new OptionDialog("Bankrovat", "Jste si jistí, že chcete bankrotovat a tím vypadnout ze hry?", true);
				if(optionDialog.showAndWait()){
					humanPlayer.quitGame();
				}
			}
		});
		
		
		buttonTrade = new Button("OBCHODOVAT");
		buttonTrade.setStyle(Design.getButtonColorCSS());
		buttonStats = new Button("STATS");
		buttonStats.setStyle(Design.getButtonColorCSS());
		buttonStats.setOnAction(new  EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				
				//
				if(statsDialog==null||!statsDialog.isShow()){
					statsDialog = new StatsDialog(humanPlayer.getGameReferee().getGame());
				}
			}
		});
		

		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setStyle("-fx-background: " + Design.LIGHT_BLACK_COLOR+ ";-fx-border-color: " + Design.LIGHT_BLACK_COLOR+ ";");
		scrollPane.setContent(propertyPanels);
		propertyPanels.setPadding(new Insets(4));
		
		scrollPane.setPrefHeight(600);
		VBox.setVgrow(scrollPane, Priority.ALWAYS);
		center.getChildren().add(scrollPane);
		
		
		buttonTrade.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				humanPlayer.opanTradeWindow();
			}
		});
		
		HBox hButtonPanel = new HBox(2);
		hButtonPanel.setAlignment(Pos.CENTER);
		hButtonPanel.setPadding(new Insets(4));
		hButtonPanel.getChildren().addAll(/*buttonPayMePlease,*/ buttonTrade, buttonStats,buttonOut );
		hButtonPanel.setStyle("-fx-background-color: #303030; ");
	

		center.getChildren().add(hButtonPanel);

		
	}
	
	public void noPlayers(){
		labelMoney.setText("-");
		labelPlayerName.setText("[ ŽÁDNÍ HRÁČI ]");
		buttonOut.setDisable(true);
		buttonTrade.setDisable(true);
	}

	
	public synchronized void setNextHumanPlayer( HumanPlayer humanPlayer){
		final boolean isInPrisona = humanPlayer.isInPrison();
		Player player = humanPlayer.getPlayer();
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				labelMoney.setText(PropertyDialog.moneyFormat(humanPlayer.getPlayer().getMoney()));
				labelPlayerName.setText(humanPlayer.getPlayer().getName());

				if (isInPrisona) {
					buttonBait.setText("VYKOUPIT SE[" + 1000 + " BTC]");
					buttonBait.setDisable(false);
					hBoxJail.setVisible(true);
					hBoxJail.setManaged(true);
					if (player.hasCardPrison()) {
						buttonUseCard.setDisable(false);
					} else {
						buttonUseCard.setDisable(true);
					}
				}else{
					hBoxJail.setVisible(false);
					hBoxJail.setManaged(false);
					
				}
				
				if(humanPlayer.getPlayerHaveToPay()>0){

					buttonPay.setText("ZAPLAŤ ["  + humanPlayer.getPlayerHaveToPay() + " BTC]");
					buttonPay.setDisable(false);
					Player paTo = humanPlayer.playerPayTo();
					String payTo = " bance";
					if(paTo!=null){
						payTo = paTo.getName();
					}
					labelPayTo.setText(payTo);
					hRequestMoney.setManaged(true);
					hRequestMoney.setVisible(true);

					labelPayTo.setVisible(true);
					labelPayTo.setManaged(true);
				}else{

					hRequestMoney.setManaged(false);
					hRequestMoney.setVisible(false);
				}
				
				properties.clear();
				propertyPanels.getChildren().clear();
				

				// set Properties
			
				for(Property property: humanPlayer.getPlayer().getPropertiesPublic()){
					if(property.getRealty()!=null){
						continue;
					}
					PropertyPanel panel = new PropertyPanel(property, humanPlayer);
					properties.add(panel);
					propertyPanels.getChildren().add(panel);
				}
				for(int i = 0; i<Property.GROUPS_ORDER.length; i++){
					for(Property property: humanPlayer.getPlayer().getGroupProperties(Property.GROUPS_ORDER[i])){
						
						PropertyPanel panel = new PropertyPanel(property, humanPlayer);
						properties.add(panel);
						propertyPanels.getChildren().add(panel);
					}
				}
				
				
			}
		});
	}

	public void setMoneyToPay(int money, boolean bank) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				buttonPay.setText("Zaplať ("  + money + " BTC)");
				buttonPay.setDisable(false);
				labelPayTo.setVisible(false);
				labelPayTo.setManaged(false);
				hRequestMoney.setManaged(true);
				hRequestMoney.setVisible(true);
			}
		});
	}
	public void setMoneyToPayOk() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				buttonPay.setText("OK");
				buttonPay.setDisable(true);
				hRequestMoney.setVisible(false);
				hRequestMoney.setManaged(false);
			}
		});
	}
	public void setJailOk() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				
				buttonUseCard.setDisable(true);
				hBoxJail.setVisible(false);
				hBoxJail.setManaged(false);
			}
		});
	}
	
	public void setMoney(int money) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				
				labelMoney.setText(PropertyDialog.moneyFormat(money));
			}
		});
	}
	
	public void setPrisonCaard(boolean haveCard){
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				//buttonBait.setText("VYKOUPITT SE ["  + 1000 + " BTC]");
				//buttonBait.setDisable(false);
				//hBoxJail.setVisible(true);
				//hBoxJail.setManaged(true);
				if(haveCard){
					buttonUseCard.setDisable(false);
				}else{
					buttonUseCard.setDisable(true);
				}
			}
		});
	}

	public void setNumbers(int a, int b, int isdouble) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				dicePanel.setNumbers(a, b);
			}
		});
	}
	
	public  void buyNewRealty(Property realty){
		setNextHumanPlayer(humanPlayer);
		
	}

}

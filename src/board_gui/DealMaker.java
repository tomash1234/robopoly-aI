package board_gui;

import java.util.ArrayList;
import java.util.List;


import board_gui.HumanPlayer.OnDealMadeListener;
import game_mechanics.Deal;
import game_mechanics.Game;
import game_mechanics.Player;
import game_mechanics.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class DealMaker extends VBox{
	
	
	private Game game;
	private DesignStage designStage;
	private HBox hBoxButtons = new HBox(4);
	private Button ok = new Button("OK");
	private Button cancel = new Button("STORNO");
	private OnDealMadeListener dealMadeListener;
	
	public DealMaker(Deal deal, OnDealMadeListener dealMadeListener){

		DealSide dS = new DealSide(deal, true);
		DealSide dR = new DealSide(deal, false);
		
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				dealMadeListener.OnDealMade(deal);
				designStage.close();
			}
		});
		cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dealMadeListener.OnDealMade(null);
				designStage.close();
			}
		});
		setPadding(new Insets(8));
		ok.setText("PŘIJMOUT");
		cancel.setText("ODMÍTNOUT");
		
		HBox hbox = new HBox(8);
		hbox.getChildren().add(dS.getVbox());
		hbox.getChildren().add(dR.getVbox());
		 getChildren().add(hbox);
		 
		 addButtons();
			setPrefWidth(500);
		
		designStage = new DesignStage("Nabídka na obchod", false, this);
		//designStage.setTitle("Deal maker");
		
	}
	
	private void addButtons(){
		
		ok.setStyle(Design.getButtonCSS());
		cancel.setStyle(Design.getButtonCSS());
		
		hBoxButtons.setPadding(new Insets(8,0,0,0));
		hBoxButtons.getChildren().addAll(ok, cancel);
		hBoxButtons.setAlignment(Pos.BASELINE_RIGHT);

		 getChildren().add(hBoxButtons);
	}
	
	public DealMaker(Game game, Player me, OnDealMadeListener dealMadeListener) {
		this.dealMadeListener = dealMadeListener;
		this.game = game;
		
		DealSide dS = new DealSide(me.getPlayerId(), true);
		dS.setPlayer(me.getPlayerId());
		dS.disabledName();

		

		DealSide dR = new DealSide(me.getPlayerId(), false);

		ok.setText("NAVRHNOUT OBCHOD");
		cancel.setText("ZAVŘÍT");
		setPadding(new Insets(8));
		
		cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				designStage.close();
			}
		});
		
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Deal deal = new Deal(me.getPlayerId(), dR.getSelectedPlayer());
				for(Property pr: dS.getPropertySelected()){
					deal.addOffer(pr);
				}
				for(Property pr: dR.getPropertySelected()){
					deal.addDemand(pr);
				}
				deal.setMoneyOffer(dS.getMoney());
				deal.setMoneyDemand(dR.getMoney());
				if(dS.checkBox.isSelected()){
					deal.setRentOmittedFromOffer();
				}else if(dR.checkBox.isSelected()){
					deal.setRentOmittedFromDemand();
				}
				dealMadeListener.OnDealMade(deal);
				
				designStage.close();
			}
		});

		HBox hbox = new HBox(8);
		hbox.getChildren().add(dS.getVbox());
		hbox.getChildren().add(dR.getVbox());
		
		getChildren().add(hbox);
		addButtons();
		

		setPrefWidth(600);
		designStage = new DesignStage("Obchodování", true, this);
		
	}
	
	public class DealSide{
		
		private ChoiceBox<String> cbItem = new ChoiceBox<>();
		private ChoiceBox<String> cbPlayers = new ChoiceBox<>();
		
		private ListView<String> listView = new ListView<>();
		
		//private List<String> cbListString = new ArrayList<>();
		
		
		private List<Property> llProperty = new ArrayList<>();
		private List<Property> propertySelected = new ArrayList<>();

		private ObservableList<String>  observableListStringCL  = FXCollections.observableArrayList ();
		private ObservableList<String> listItemsString = FXCollections.observableArrayList ();
		private VBox vbox = new VBox(4);
		private HBox addBox = new HBox(4);
		private int selectedPlayer = 0;
		private List<Player> players = new ArrayList<>();
		private TextField tfMoney = new TextField();
		private CheckBox checkBox = new CheckBox("Odpustí nájem");
		private Button but = new Button(" + ");
		private Button butRemove = new Button(" × ");
		
		public DealSide(int player, boolean a) {
			// TODO Auto-generated constructor stub
			List<String> listPlayer = new ArrayList<>();
			for (int i = 0; i < game.getNumberOfPalyers(); i++) {
				if(!(player!=-1 && i==player)){
					listPlayer.add(game.getPlayer(i).getName());
					players.add(game.getPlayer(i));
				}
			}
			
			
			ObservableList<String>  playersName = FXCollections.observableArrayList(listPlayer);
			cbPlayers.setItems(playersName);

			cbPlayers.setStyle(Design.getNormalTextWhiteCSS());
			cbPlayers.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					// TODO Auto-generated method stub
					if(newValue.intValue()==-1){
						cbItem.setItems(FXCollections.observableArrayList());
						selectedPlayer = -1;
					}
					selectedPlayer = players.get(newValue.intValue()).getPlayerId();
					setPlayer(selectedPlayer);
				}
			});
			if(cbPlayers.getItems().size()>0){
				cbPlayers.getSelectionModel().select(0);
			}
			
			but.setStyle(Design.getButtonCSS());
			but.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					int i = cbItem.getSelectionModel().getSelectedIndex();
					if(i==-1){
						return;
					}
					propertySelected.add(llProperty.get(i));
					listItemsString.add(llProperty.get(i).toString());
					
					
					llProperty.remove(i);
					observableListStringCL.remove(i);
					
					
				}
			});
			butRemove.setDisable(true);
			butRemove.setStyle(Design.getButtonCSS());
			butRemove.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					int i = listView.getSelectionModel().getSelectedIndex();
					if(i==-1){
						return;
					}
					llProperty.add(propertySelected.get(i));
					observableListStringCL.add(propertySelected.get(i).toString());
					
					
					propertySelected.remove(i);
					listItemsString.remove(i);
					if(listView.getSelectionModel().getSelectedIndex()==-1){
						butRemove.setDisable(true);
					}
				}
			});
			listView.setItems(listItemsString);
			listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					butRemove.setDisable(false);
				}
				
			});
			listView.setPrefWidth(300);
			listView.setStyle(Design.getNormalTextWhiteCSS());
			

			Region region2 = new Region();
			HBox.setHgrow(region2, Priority.ALWAYS);
			addBox.getChildren().addAll(cbItem,region2, but,butRemove);
			cbItem.setPrefWidth(200);
			
			cbItem.setStyle(Design.getNormalTextWhiteCSS());
			
			HBox cbBox = new HBox(4);
			Label l = new Label(a?game.getPlayer(player).getName():"Obchodovat s");
			l.setStyle(Design.getNormalTextWhiteItalicCSS() + "-fx-text-fill: #ddd;;");
			HBox.setHgrow(l, Priority.ALWAYS);

			cbBox.getChildren().add(l);
			Region region = new Region();
			HBox.setHgrow(region, Priority.ALWAYS);
			cbBox.getChildren().add(region);
			cbBox.getChildren().add(cbPlayers);
			
			tfMoney.setStyle("-fx-background-color: " + Design.DARK_COLOR + "; -fx-text-fill: #ffffff;-fx-font-weight: bold;" +Design.getFontMediumCSS());

			Label popis = new Label(a?"Nabízeno:":"Požadováno:");
			popis.setStyle(Design.getNormalTextWhiteItalicCSS());
			
			checkBox.setStyle(Design.getNormalTextWhiteCSS());
			
			HBox hbMoney = new HBox();
			Label lBTC = new Label("BTC");
			lBTC.setStyle("-fx-background-color: " + Design.DARK_COLOR + "; -fx-text-fill: #ffffff;-fx-font-weight: bold;" +Design.getFontMediumCSS());
			tfMoney.setAlignment(Pos.CENTER_RIGHT);
			lBTC.setPrefHeight(tfMoney.getHeight());
			lBTC.setPadding(new Insets(4, 4, 4, 4));
			lBTC.setTextAlignment(TextAlignment.CENTER);
			lBTC.setAlignment(Pos.CENTER);
			Label moneyLa = new Label("Peníze ");
			moneyLa.setStyle(Design.getNormalTextWhiteItalicCSS() + "-fx-text-fill: #ddd;");
			moneyLa.setPadding(new Insets(4, 18, 4, 0));
			moneyLa.setAlignment(Pos.CENTER_LEFT);
			hbMoney.getChildren().addAll(moneyLa, tfMoney,lBTC);
			
			vbox.getChildren().addAll(popis, listView, cbBox, addBox, hbMoney, checkBox);
			vbox.setPrefWidth(400);
			  
		}
		
		public int getMoney() {
			String f = tfMoney.getText();
			try{
				return Integer.valueOf(f);
			}catch(Exception e){
				return 0;
			}
		}

		public DealSide(Deal deal, boolean offer){
			for (int i = 0; i < (offer?deal.getOfferLength(): deal.getDemandLength()); i++) {
				if(offer){
					listItemsString.add(deal.getOffer(i).toString());
				}else{
					listItemsString.add(deal.getDemand(i).toString());
				}
			}
			if(offer){
				if(deal.getMoneyOffer()>0){
					listItemsString.add("Kapitál v hodnotě " +PropertyDialog.moneyFormat(deal.getMoneyOffer()));
				}
				if(deal.getRent()==-1){
					listItemsString.add("Nájem bude odpuštěn ");
				}
			}else{
				if(deal.getMoneyDemand()>0){
					listItemsString.add("Kapitál v hodnotě " + PropertyDialog.moneyFormat(deal.getMoneyDemand()));
				}
				if(deal.getRent()==1){
					listItemsString.add("Nájem bude odpuštěn ");
				}
			}
			listView.setItems(listItemsString);
			Label label = new Label(offer?"Získáte:":"Ztrátíte:");
			label.setStyle(Design.getNormalTextWhiteItalicCSS());
			vbox.getChildren().addAll(label,listView);
			
		}
		
		public int getSelectedPlayer() {
			return selectedPlayer;
		}

		public List<Property> getPropertySelected() {
			return propertySelected;
		}
		public void setPlayer(int pa){
			Player pl= game.getPlayer(pa);
			//cbPlayers.getSelectionModel().select(pa);
			llProperty = pl.getPropertiesPublic();
			observableListStringCL.clear();
			for(Property p: llProperty){
				if(p.getType()==Property.CARD){
					observableListStringCL.add("Card");
				}else{
					observableListStringCL.add(p.getRealty().getName() + " " + p.getRealty().getPrice() + " BTC");
				}
			}
			listView.getItems().clear();
			propertySelected.clear();
			listItemsString.clear();
			cbItem.setItems(observableListStringCL);
			but.setDisable(true);
			
			cbItem.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					// TODO Auto-generated method stub
					but.setDisable(newValue.intValue()==-1);
					
				}
			});
		}
		public VBox getVbox() {
			return vbox;
		}
		
		public void disabledName(){
			cbPlayers.setDisable(true);
			cbPlayers.setVisible(false);
		}
		
	}
	
	public void showStage(){
		designStage.show();
	}

	public void cancel() {
		// TODO Auto-generated method stub
		designStage.close();
	}
	
	
	


}

package board_gui;

import game_mechanics.GameReferee;
import game_mechanics.Plat;
import game_mechanics.Property;
import game_mechanics.Realty;
import game_mechanics.Stats;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PropertyDialog extends BorderPane{
	
	private Property property;
	private HumanPlayer humanPlayer;
	//private Stage dialogStage = new Stage();
	private DesignStage designStage;
	private Label menuRight;
	private Button mortage;
	private Stats stats;
	private String textColor, backColor;
	
	
	public PropertyDialog(Property property, HumanPlayer humanPlayer) {
		
		// TODO Auto-generated constructor stub
		this.humanPlayer = humanPlayer;
		this.stats = humanPlayer.getGameReferee().getGame().getStats();
		this.property =  property;
		if(property.getRealty()!=null){
			createRealty(property);
		}
		
	}
	private void createRealty(Property realty){
		

		setPadding(new Insets(8));
		

	
		setLeft(getVBox(realty));
		
		propertyInfo();
		addButton();
		
		show();
	}
	
	public  VBox getVBox(Property property){
		Realty realty = property.getRealty();
		VBox vbox = new VBox(2);
		//vbox.setPrefWidth(200);
		vbox.setPadding(new Insets(8));
		Label l = new Label(property.getRealty().getName());
		l.setPrefWidth(250);
		l.setPadding(new Insets(4));
		l.setAlignment(Pos.CENTER);
		//String color = Plat.GROUP_COLOR_STRING[property.getRealty().getGroup()];
		l.setStyle(" -fx-font-weight: bold;" );
		this.backColor = Plat.GROUP_COLOR_STRING[property.getRealty().getGroup()];
		this.textColor = (backColor.equals("#ffffff")||backColor.equals("#ffe400")?"#000000":"#ffffff");
		
		
		Label priceAndRent = new Label("CENA " + moneyFormat(realty.getPrice()) + " NÁJEM " + moneyFormat(realty.getRentPrice(0)));
		priceAndRent.setStyle(Design.getNormalTextWhiteCSS() + "-fx-text-fill: #d0d0d0;"+ "-fx-font-size: 10px;");
		//priceAndRent.setPrefWidth(200);
		priceAndRent.setTextAlignment(TextAlignment.CENTER);
		
	HBox hbox = new HBox();
		
		Label left = new Label(" s 1 domem\n"
				+ " s 2 domy\n"
				+ " s 3 domy\n"
				+ " s 4 domy\n"
				+ " s HOTELEM\n"
				+ " Jeden dům stojí\n"
				+ " Hypotéka");
		left.setPrefWidth(130);
		left.setStyle(Design.getNormalTextWhiteCSS() + "-fx-text-fill: #d0d0d0;"+ "-fx-font-size: 11px;");
		Label right = new Label(moneyFormat(realty.getRentPrice(1))+"\n"
				+ moneyFormat(realty.getRentPrice(2))+"\n"
				+ moneyFormat(realty.getRentPrice(3))+"\n"
				+ moneyFormat(realty.getRentPrice(4))+"\n"
				+ moneyFormat(realty.getRentPrice(5))+"\n"
				+ moneyFormat(realty.getHouseCost())+"\n"
				+ moneyFormat(realty.getMortgage()));
		right.setAlignment(Pos.CENTER_RIGHT);
		right.setStyle(Design.getNormalTextWhiteCSS()+ "-fx-font-size: 11px;");
		right.setPrefWidth(120);
		right.setTextAlignment(TextAlignment.RIGHT);
		left.setAlignment(Pos.CENTER_LEFT);
		hbox.getChildren().addAll(left, right);
		
		vbox.getChildren().addAll(/*l,*/ priceAndRent, hbox);
		return vbox;
	}
	
	private String getOwnerName(){
		int i = property.getOwner();
		if(i==-1){
			return "Banka";
		}else{
			return humanPlayer.getGameReferee().getGame().getPlayer(i).getName();
		}
	}
	private void propertyInfo(){
		VBox vbox = new VBox(1);
		HBox hbox = new HBox();
		
		Label left = new Label("Majitel\n"
				+ "Navštíveno\n"
				+ "Výdělek\n"
				+ "Počet domů\n"
				+ "Nájemné\n");
		left.setPrefWidth(130);
		left.setStyle(Design.getNormalTextWhiteCSS() + "-fx-text-fill: #d0d0d0;"+ "-fx-font-size: 11px;");
		menuRight = new Label(getOwnerName()+"\n"
				+ stats.getPlatVisited(property.getRealty().getRealtyID())+"x\n"
				+ PropertyDialog.moneyFormat(stats.getMoneyErned(property.getRealty().getRealtyID()))+"\n"
				+ property.getNumberOfHouse()+"\n"
				+ moneyFormat(humanPlayer.getGameReferee().getRentPrice(property.getRealty().getRealtyID())));
		menuRight.setAlignment(Pos.CENTER_RIGHT);
		menuRight.setPrefWidth(120);
		menuRight.setStyle(Design.getNormalTextWhiteCSS()+ "-fx-font-size: 11px;");
		menuRight.setTextAlignment(TextAlignment.RIGHT);
		left.setAlignment(Pos.CENTER_LEFT);
		hbox.getChildren().addAll(left, menuRight);
		vbox.setPadding(new Insets(32, 0, 0, 0));
		vbox.getChildren().addAll( hbox);
		if(property.isMortage()){
			menuRight.setStyle("-fx-background-color: #ccc;");
		}
		setRight(vbox);
	}
	public void updateRightMenu(){
		menuRight.setText(getOwnerName()+"\n"
				+ "\n"
				+ "\n"
				+ property.getNumberOfHouse()+"\n"
				+ moneyFormat(humanPlayer.getGameReferee().getRentPrice(property.getRealty().getRealtyID())));
		menuRight.setStyle("-fx-text-fill: #FFFFFF;");
		
		if(property.isMortage()){
			menuRight.setStyle("-fx-background-color: #ccc;");
			mortage.setText("ZAPLATIT HYPOTÉKU");
		}else{
			mortage.setText("VZÍT HYPOTÉKU");

			menuRight.setStyle("-fx-background-color: #ffffff00;");
		}
	}
	private void addButton(){
		HBox h = new HBox(4);
		Button buyHouse = new Button("KOUPIT DŮM");
		buyHouse.setStyle(Design.getButtonCSS());
		buyHouse.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				humanPlayer.buyHouseOnProperty(property);
				Platform.runLater(new Runnable() {
					public void run() {
						updateRightMenu();
					}
				});
			}
		});
		Button sellHouse = new Button("PRODAT DŮM");
		sellHouse.setStyle(Design.getButtonCSS());
		sellHouse.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				humanPlayer.sellHouseProperty(property);

				Platform.runLater(new Runnable() {
					public void run() {
						updateRightMenu();
					}
				});
			}
		});
		mortage = new Button("VZÍT HYPOTÉKU");
		mortage.setStyle(Design.getButtonCSS());
		if(property.isMortage()){
			mortage.setText("ZAPLATIT HYPOTÉKU");
		}
		mortage.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				humanPlayer.mortage(property);

				Platform.runLater(new Runnable() {
					public void run() {
						updateRightMenu();
					}
				});
			}
		});
		Button zavrit = new Button("ZAVŘÍT");
		zavrit.setStyle(Design.getButtonCSS());
		zavrit.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				//dialogStage.close();
				designStage.close();
			}
		});
		
		//zavrit.setPrefWidth(60);
		h.setPadding(new Insets(16, 0, 0, 0));
		h.getChildren().addAll(buyHouse,sellHouse, mortage, zavrit);
		h.setAlignment(Pos.BASELINE_RIGHT);
		//h.setPrefWidth(400);
		setBottom(h);
	}
	
	public void show(){/*
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setHeaderText("");
		alert.setTitle(property.getRealty().getName());
		alert.setGraphic(this);
		alert.setContentText("");
		alert.show();*/
		designStage = new DesignStage(property.getRealty().getName(), true, this);
	    /*dialogStage.initModality(Modality.APPLICATION_MODAL );
	    //dialogStage.initStyle(StageStyle.UTILITY);
	    dialogStage.setTitle(property.getRealty().getName());
	    dialogStage.setResizable(false);
	    
	
	    dialogStage.setScene(new Scene(this));*/
		designStage.setTitleBoxColor(textColor, backColor);
		designStage.show();
	}
	public static String moneyFormat(int price){
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

}

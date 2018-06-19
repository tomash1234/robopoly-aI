package board_gui;

import java.util.List;

import game_mechanics.Plat;
import game_mechanics.Player;
import game_mechanics.Property;
import game_mechanics.Stats;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PlayerInfoDialog {
	
	private DesignStage stage ;
	private Player player;
	private VBox vbox = new VBox();
	private HBox firstRow = new HBox();
	private  Stats stats;
	
	public PlayerInfoDialog(Player player, Stats stats){
		this.player = player;
		this.stats = stats;
		//stage.initStyle(StageStyle.UNDECORATED);
		vbox.setStyle("-fx-background-color: " +Design.LIGHT_BLACK_COLOR + "; ");
		//stage.setScene(new Scene(vbox, 720, 400));
		createPicAndName();
		createStats();
		craeteTableOfProperty();
		//stage.show();
		stage = new DesignStage("", true, vbox);
		stage.setTitle("Hráč " + player.getName());
		stage.initModality(Modality.NONE);
		stage.show();
	}
	
	private void createPicAndName(){
		VBox vboxPro = new VBox(2);
		//vboxPro.setPrefWidth(130);
		//vboxPro.setMaxWidth(130);
		vboxPro.setPadding(new Insets(8,8,8,16));
		vboxPro.setAlignment(Pos.TOP_LEFT);
		ImageView image = new ImageView(Figure.getImage(player.getFigurka()));
		image.setFitWidth(100);
		image.setFitHeight(100);
		Label l = new Label(player.getName());
		//Font myFont = Font.loadFont(ClassLoader.getSystemResourceAsStream("RobotoMono-Medium.ttf"), 14);
		l.setStyle(" -fx-font-size: 28px;" + "-fx-text-fill: #ffffff;" +Design.getFontRegularCSS());
		Label ty = new Label(player.getPlayerType());
		ty.setStyle("-fx-font-style: italic; -fx-font-size: 12px;" + "-fx-text-fill: #dfdfdf;" +Design.getFontRegularCSS() );
		Label money = new Label("Human Player");
		money.setStyle(" -fx-font-size: 17px;-fx-text-fill: #ffffff;-fx-font-weight: bold;"  +Design.getFontMediumCSS()  );
		money.setText(PropertyDialog.moneyFormat(player.getMoney()));
		l.setFont(Design.getFontRegular());
		//ty.setFont(myFont);
		//money.setFont(myFont);
		HBox pro = new HBox(4);
		pro.setStyle("-fx-background-color: " + Design.DARK_COLOR+  ";");
		pro.getChildren().addAll(image, vboxPro);
		vboxPro.getChildren().addAll( l, ty, money);
		vbox.getChildren().addAll(pro);
		
	}
	private String lineBracking(String s){
		StringBuilder sb = new   StringBuilder();
		int end = 0;
		for(int i = 0; i<s.length(); i++){
			if(i%8==0&&i>0){
				sb.append(s.substring(end, i)+"\n");
				end = i;
			}
			if(i==s.length()-1){
				sb.append(s.substring(end));
			}
		}
		return sb.toString();
	}
	
	private void createStats(){
		int id = player.getPlayerId();
		
		String[] statsLabels = new String[]{"Počet doublů", "Počet kol", "Počet eliminací", "Dostáno z banky", "Placeno bance", 
				"Dostáno od hráčů", "Placeno hráčům"};
		String[] data = new String[]{""+stats.getDoubles(id),  "" + stats.getRounds(id), ""+stats.getEliminations(id),
				 ""+PropertyDialog.moneyFormat(stats.getMoneyGotFromBank(id)), ""+ PropertyDialog.moneyFormat(stats.getMoneyPaidToBank(id)),
				 ""+PropertyDialog.moneyFormat(stats.getMoneyGotFromPlayer(id)),  ""+PropertyDialog.moneyFormat(stats.getMoneyPaidToPlayer(id))};

		String texts[][] = new String[2][2];
		for(int i = 0; i<texts.length; i++){
			texts[i][0]="";
			texts[i][1]="";
		}
		for(int i = 0; i<statsLabels.length; i++){
			
			texts[i<3?0:1][0]+=statsLabels[i] +"\n";
			texts[i<3?0:1][1]+=data[i] +"\n";
		}

		
		HBox statBox = new HBox(16);
		statBox.setPadding(new Insets(0, 16, 0, 16));
		for(int i = 0; i<texts.length; i++){
			Label lab1s = new Label(texts[i][0]);
			lab1s.setPrefWidth(180);
			Label lab2s = new Label(texts[i][1]);
			lab2s.setPrefWidth(120);
			lab1s.setStyle("-fx-text-fill: #ffffff;-fx-font-size: 14px;" + Design.getFontRegularCSS());
			lab2s.setStyle("-fx-text-fill: #ffffff;-fx-font-size: 14px;" + Design.getFontRegularCSS());
			lab2s.setAlignment(Pos.CENTER_RIGHT);
			lab2s.setPadding(new Insets(0, 0, 0, 28));
			lab2s.setTextAlignment(TextAlignment.RIGHT);
			statBox.getChildren().addAll(lab1s, lab2s);
			
		}
		
		vbox.getChildren().addAll(statBox);
	}
	
	private void craeteTableOfProperty(){
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(16));
		gridPane.setHgap(2);
		for(int i = 0; i<Property.GROUPS_ORDER.length; i++){
			VBox vb = new VBox(4);
			for(Property property:player.getGroupProperties(Property.GROUPS_ORDER[i]) ){
				Button b = new Button(lineBracking(property.getRealty().getName()));
				String color = Plat.GROUP_COLOR_STRING[property.getRealty().getGroup()] ;
				if(property.isMortage()){
					color = "#909090";
				}
				b.setStyle("-fx-background-color: " + color+ ";" + " -fx-font-size: 10px;-fx-text-fill: " + (color.equals("#ffffff")?"#000000":"#ffffff; -fx-font-weight: bold;")) ;
				b.setPrefWidth(70);
				vb.getChildren().add(b);
			}
			gridPane.add(vb, i, 1);
		}
		vbox.getChildren().add(gridPane);
		
		HBox cards = new HBox(4);
		cards.setPadding(new Insets(16));
		
		for(Property property : player.getPropertiesPublic()){
			if(property.getRealty()==null){
				Button b = new Button("Karta: Zdarma opusť vězení");
				b.setStyle("-fx-background-color: " + "#DEDEDE"+ ";" + " -fx-font-size: 10px;-fx-text-fill: #000000; -fx-font-weight: bold;") ;
				cards.getChildren().add(b);
			}
		}
		vbox.getChildren().add(cards);
	}

}

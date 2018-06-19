package board_gui;
import java.util.List;

import game_mechanics.Player;
import game_mechanics.Stats;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
public class GameSummary {

	private GridPane gridPane = new GridPane();
	private String[] data = new String[]{"Hodnota majetku", "Počet doublů", "Eliminace", "Placeno bance", "Placeno hráčům", "Dostáno z karet", "Dostáno od hráčů", "Počet kol", "Nakoupeno domů", "Prodáno domů"};
	private Stats stats;
	private DesignStage designStage;
	private VBox vbox = new VBox(4);
	public GameSummary(List<Player> playerOrder, Stats stat){
		int ind = 1;
		this.stats = stat;
		gridPane.setPadding(new Insets(16));
		gridPane.setPrefWidth(500);
		gridPane.setPrefHeight(200);
		for(int i = playerOrder.size()-1; i>=0; i--){
			insertPlayer(playerOrder.get(i), ind);
			ind++;
		}
		 insertLabels();
		 vbox.getChildren().add(gridPane);
		 HBox butBox= new HBox();
		 butBox.setPadding(new Insets(8));
		 butBox.setAlignment(Pos.CENTER);
		 Button but = new Button("ZAVŘÍT APLIKACI");
		 butBox.getChildren().add(but);
		 
		 but.setStyle(Design.getButtonCSS());
		 but.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		 
		 vbox.getChildren().add(butBox);
		 
		 designStage = new DesignStage("Konec hry", true, vbox);
		 designStage.show();
	}
	
	private void insertLabels(){
		for(int i = 0; i<data.length; i++){
			Label l =new Label(data[i]);
			l.setStyle(Design.getNormalTextWhiteItalicCSS());
			gridPane.add(l, 0, i+1);
		}
		
	}
	private void fillTable(Player player, int index){
		Label ls[]  = new Label[data.length];
		for(int i = 0; i<ls.length; i++){
			ls[i] = new Label();
			ls[i].setPrefWidth(100);
			ls[i].setStyle(Design.getNormalTextWhiteCSS());
			ls[i].setAlignment(Pos.CENTER);
			gridPane.add(ls[i],index, i+1);
		}
		int i = player.getPlayerId();
		ls[0].setText(""+ stats.getTotalMoney(i));
		ls[1].setText(""+ stats.getDoubles(i));
		ls[2].setText(""+ stats.getEliminations(i));
		ls[3].setText(""+ stats.getMoneyPaidToBank(i));
		ls[4].setText(""+ stats.getMoneyPaidToPlayer(i));
		ls[5].setText(""+ stats.getMoneyGotFromBank(i));
		ls[6].setText(""+ stats.getMoneyGotFromPlayer(i));
		ls[7].setText(""+ stats.getRounds(i));
		ls[8].setText(""+ stats.getMoneyHouses(i));
		ls[9].setText(""+ stats.getMoneySellHouses(i));
		
		
	}
	
	private void insertPlayer(Player player, int index){
		
		VBox vbox = new VBox(4);
		vbox.setAlignment(Pos.CENTER);
		Label label = new Label("" + index +".");
		label.setStyle(Design.getNormalTextWhiteItalicCSS());
		label.setPrefWidth(100);
		label.setAlignment(Pos.CENTER);
		ImageView imageView = new ImageView(Figure.getImage(player.getFigurka()));
		imageView.setFitWidth(50);
		imageView.setFitHeight(50);
		Label name = new Label(player.getName());
		name.setStyle(Design.getNormalTextWhiteItalicCSS());
		name.setPrefWidth(100);
		name.setAlignment(Pos.CENTER);
		
		vbox.getChildren().addAll(label, imageView, name);
		gridPane.add(vbox, index, 0);
		fillTable(player, index);
		
	}
	
}

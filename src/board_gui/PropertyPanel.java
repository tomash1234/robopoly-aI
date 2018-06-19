package board_gui;

import game_mechanics.GameReferee;
import game_mechanics.Plat;
import game_mechanics.Property;
import game_mechanics.Realty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class PropertyPanel extends BorderPane{
	
	private Label numberOfHouses; 
	
	private Property property;
	
	public PropertyPanel(Property property, HumanPlayer humanPlayer){
		String n = "";
		int g =0;
		if(property.getRealty()!=null){
			n = property.getRealty().getName().trim();
			g = property.getRealty().getGroup();
		}else{
			n= "Karta - Opustit vězení";
		}
		Label l = new Label(n);
		String color = Plat.GROUP_COLOR_STRING[g];
		if(property.isMortage()){
			color = "#606060";
		}
		if(property.getRealty()==null){
			color = "#D0D0D0";
		}
		setStyle(" -fx-background-color: "+color+";");
		l.setStyle(" -fx-text-fill: " + ((color.equals("#ffffff")||color.equals("#D0D0D0")||color.equals("#ffe400")?"#000000;":"#ffffff;")+" -fx-font-weight: bold;"+ Design.getFontNotMonoCSS()) );
		l.setPadding(new Insets(4));
		setLeft(l);
		Button b = new Button("DETAIL");
		b.setStyle(Design.getButtonCSS());
		b.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				PropertyDialog dialog = new PropertyDialog(property, humanPlayer);
			}
		});
		if(property.getRealty()!=null){
			setRight(b);
		}
			
		
		setPrefWidth(260);
		
	}

}

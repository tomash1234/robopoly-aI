package board_gui;

import game_mechanics.Game;
import game_mechanics.Plat;
import game_mechanics.Property;
import game_mechanics.Stats;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StatsDialog {
	
	private DesignStage designStage;
	private VBox vbox = new VBox(8);
	private Stats stats;
	private Game game;
	
	public StatsDialog(Game game) {
		// TODO Auto-generated constructor stub
		this.stats = game.getStats();
		this.game = game;
		vbox.setPadding(new Insets(16));
		vbox.setStyle("-fx-background-color: " +Design.LIGHT_BLACK_COLOR + "; ");
		
		//stage.setScene(new Scene(vbox, 850, 400));
		
		//stage.show();
		
		createPlayersStats();
		createPropertyStats();
		 createPlat();
		 designStage = new DesignStage("", true, vbox);
			designStage.setTitle("Statistiky");
			designStage.initModality(Modality.NONE);
			designStage.show();
	}
	
	private void createPlayersStats(){
		String[] statsLabels = new String[]{"Jméno","Počet doublů", "Počet kol", "Počet eliminací", "Dostáno z banky", "Placeno bance", 
				"Dostáno od hráčů", "Placeno hráčům"};
		GridPane gridPane = new GridPane();
		for(int i = 0; i<statsLabels.length; i++){
			VBox p = new VBox();
			p.setPrefWidth(850.f/(statsLabels.length+1));
			Label label = new Label(statsLabels[i]);
			label.setStyle(Design.getNormalTextWhiteItalicCSS() + " -fx-text-fill: #d0d0d0; -fx-font-size: 9px;");
			p.getChildren().add(label);
			p.setPadding(new Insets(4));
			p.setAlignment(Pos.CENTER);
			p.setStyle("-fx-background-color: " +Design.DARK_COLOR + "; ");
			gridPane.add(p, i, 0);
			for (int j = 0; j < game.getNumberOfPalyers(); j++) {
				VBox box = new VBox();
				box.setAlignment(Pos.CENTER);
				String text = "";
				switch(i){
				case 0:
					text = game.getPlayer(j).getName();
					break; 
				case 1:
					text = ""+stats.getDoubles(j);
					break; 
				case 2:
					text = ""+stats.getRounds(j);
					break; 
				case 3:
					text = ""+stats.getEliminations(j);
					break; 
				case 4:
					text = PropertyDialog.moneyFormat(stats.getMoneyGotFromBank(j));
					break; 
				case 5:
					text = PropertyDialog.moneyFormat(stats.getMoneyPaidToBank(j));
					break; 
				case 6:
					text = PropertyDialog.moneyFormat(stats.getMoneyGotFromPlayer(j));
					break; 
				case 7:
					text = PropertyDialog.moneyFormat(stats.getMoneyPaidToPlayer(j));
					break; 
					
				}
				Label l = new Label(text);
				l.setStyle(Design.getNormalTextWhiteCSS() +" -fx-font-size: 11px;");
				box.getChildren().add(l);
				gridPane.add(box,i, j+1);
			}
		}
		Label popisGenerou = new Label("Obecné statistiky");
		popisGenerou.setStyle(Design.getNormalTextWhiteItalicCSS() +" -fx-font-size: 14px;");
		
		vbox.getChildren().addAll(popisGenerou, gridPane);
	}
	
	private void createPropertyStats(){
		String[] statsLabels = new String[]{"Jméno", "Penize", "Počet nemovitostí", "Počet karet", "Počet domů", "Počet hotelů", "Cena hypotéky", "Celková cena"};
		GridPane gridPane = new GridPane();
		for(int i = 0; i<statsLabels.length; i++){
			VBox p = new VBox();
			p.setPrefWidth(850.f/(statsLabels.length+1));
			Label label = new Label(statsLabels[i]);
			label.setStyle(Design.getNormalTextWhiteItalicCSS() + " -fx-text-fill: #d0d0d0; -fx-font-size: 9px;");
			p.getChildren().add(label);
			p.setPadding(new Insets(4));
			p.setAlignment(Pos.CENTER);
			p.setStyle("-fx-background-color: " +Design.DARK_COLOR + "; ");
			gridPane.add(p, i, 0);
			
			for(int j = 0; j<game.getNumberOfPalyers(); j++){
				String text = "";
				if(i==0){
					text = game.getPlayer(j).getName();
				}else{
					text = getInfo(i, j);
				}
				VBox box = new VBox();
				box.setAlignment(Pos.CENTER);
				Label l = new Label(text);
				l.setStyle(Design.getNormalTextWhiteCSS()+" -fx-font-size: 11px;");
				box.getChildren().add(l);
				gridPane.add(box,i, j+1);
			}
		}
		Label popis = new Label("Majetek");
		popis.setStyle(Design.getNormalTextWhiteItalicCSS() +" -fx-font-size: 14px;");
		
		vbox.getChildren().addAll(popis, gridPane);
	}

	public boolean isShow() {
		// TODO Auto-generated method stub
		return designStage.isShowing();
	}
	
	private String getInfo(int i, int player){
		String r = "";
		switch(i){
		case 1:
			
			r = PropertyDialog.moneyFormat(game.getPlayer(player).getMoney());
			break; 
		case 2:
			int p = 0;
			for(Property property: game.getPlayer(player).getPropertiesPublic()){
				if(property.getRealty()!=null){
					p++;
				}
			}
			r +=p;
			break; 
		case 3:
			int p2 = 0;
			for(Property property: game.getPlayer(player).getPropertiesPublic()){
				if(property.getRealty()==null){
					p2++;
				}
			}
			r +=p2;
			break;
		case 4:
			int p3 = 0;
			for(Property property: game.getPlayer(player).getPropertiesPublic()){
				p3+= property.getNumberOfHouse();
			}
			r +=p3;
			break;
		case 5:
			int p4 = 0;
			for(Property property: game.getPlayer(player).getPropertiesPublic()){
				
				if(property.getNumberOfHouse()==5){
					p4++;
				}
			}
			r +=p4;
			break;
		case 6:
			int p5= 0;
			
			for(Property property: game.getPlayer(player).getPropertiesPublic()){
				
				if(property.getRealty()!=null){
					if(property.isMortage()){
						p5+=property.getRealty().getPrice()*0.1;
						p5+=property.getRealty().getPrice()*0.5;
					}
				}
			}

			r= PropertyDialog.moneyFormat(p5);
			break;
			
		case 7:
			int p6= game.getPlayer(player).getMoney();
			
			for(Property property: game.getPlayer(player).getPropertiesPublic()){
				
				if(property.getRealty()!=null){
					if(!property.isMortage()){
						p6+=property.getRealty().getPrice();
						p6+=property.getNumberOfHouse()*property.getRealty().getHouseCost();
					}else{
						p6+=property.getRealty().getPrice()*0.5;
					}
				}
			}

			r= PropertyDialog.moneyFormat(p6);
			break;
			
		}
		
		return r;
	}
	
	private void createPlat(){
		GridPane gridPane = new GridPane();
		String[] statsLabels = new String[]{"Nejnavsťěvovanější","Nejvýdělečnější", "Nejdražší"};
		for(int i = 0; i<statsLabels.length; i++){
			VBox p = new VBox();
			p.setPrefWidth(850.f/(statsLabels.length+1));
			Label label = new Label(statsLabels[i]);
			label.setStyle(Design.getNormalTextWhiteItalicCSS() + " -fx-text-fill: #d0d0d0; -fx-font-size: 10px;");
			p.getChildren().add(label);
			p.setPadding(new Insets(4));
			p.setAlignment(Pos.CENTER);
			p.setStyle("-fx-background-color: " +Design.DARK_COLOR + "; ");
			gridPane.add(p, i, 0);
			String[] data = findMostVisited();
			if(i==1){
				data= findMostErned();
			}else if(i==2){
				data= findMostExpensive();
			}
			
			for(int j = 0; j<2; j++){
				String text = data[j];
				VBox box = new VBox();
				box.setAlignment(Pos.CENTER);
				Label l = new Label(text);
				l.setStyle(Design.getNormalTextWhiteCSS()+" -fx-font-size: 11px;");
				box.getChildren().add(l);
				gridPane.add(box,i, j+1);
			}
		
			
		}
		
		
		
		Label popis = new Label("Políčka");
		popis.setStyle(Design.getNormalTextWhiteItalicCSS() +" -fx-font-size: 14px;");
		
		
		vbox.getChildren().addAll(popis, gridPane);
	}
	
	private String[] findMostVisited(){
		int max =0;
		String name = "";
		for(int i = 0; i<40; i++){
			if (Plat.getType(i)==Plat.BUYABLE){
				int p = stats.getPlatVisited(i);
				if(p>=max){
					name = game.getProperty(i).getRealty().getName();
					max= p;
				}
			}
		}
		if(max==0){
			return new String[]{"-", "-"};
		}
		return new String[]{name,""+max};
	}
	private String[] findMostErned(){
		int max =0;
		String name = "";
		for(int i = 0; i<40; i++){
			if (Plat.getType(i)==Plat.BUYABLE){
				int p = stats.getMoneyErned(i);
				if(p>=max){
					name = game.getProperty(i).getRealty().getName();
					max= p;
				}
			}
		}
		if(max==0){
			return new String[]{"-", "-"};
		}
		return new String[]{name,""+PropertyDialog.moneyFormat(max)};
	}
	
	private String[] findMostExpensive(){
		int max=0;
		String name = "-";
		for(int i = 0; i<game.getNumberOfPalyers(); i++){
			for(Property p : game.getPlayer(i).getPropertiesPublic()){
				if(p.getRealty()!=null){
					int v= game.getGameReferee().getRentPrice(p.getRealty().getRealtyID());
					if(v>=max){
						max = v;
						name =p.getRealty().getName();
					}
				}
			}
		}

		if(max==0){
			return new String[]{"-", "-"};
		}
		return new String[]{name,""+PropertyDialog.moneyFormat(max)};
	}

}

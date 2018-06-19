package board_gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.scene.control.skin.ChoiceBoxSkin;

import game_mechanics.PrePlayer;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import nn4ai_tools.NeuralNetworkAttr;
import simulation.SimulatedGame;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Lobby {
	
	public static final String AI_LIST_FILE = "aiList.txt";
	
	private BorderPane pane = new BorderPane();
	private FlowPane flowPane = new FlowPane(Orientation.HORIZONTAL, 16,16);
	private List<PlayerPanel> playerPanels = new ArrayList<>();
	private VBox boardGameSettings = new VBox(2);
	private VBox simulationSettings = new VBox(2);
	private EventHandler eventHandler;
	private TextField tfPrefsFile = new TextField();
	
	private List<AIItem> aiPlayers = new ArrayList<>();
	private final ChoiceBox<String> cbAnimationSpeed = new ChoiceBox<>();
	
	
 	

	
	private void init(Stage stage){
		VBox vbox = new VBox(8);
		vbox.setAlignment(Pos.TOP_CENTER);
		vbox.setStyle("-fx-background-color: " +  Design.LIGHT_BLACK_COLOR + ";");
		loadAIFromFile();
		
		PlayerPanel playerPanel = new PlayerPanel();
		playerPanels.add(playerPanel);
		PlayerPanel playerPanel2 = new PlayerPanel();
		playerPanels.add(playerPanel2);
		PlayerPanel playerPanel3 = new PlayerPanel();
		playerPanels.add(playerPanel3);
		
		flowPane.getChildren().addAll(playerPanel.getVBox(),playerPanel2.getVBox(),playerPanel3.getVBox() );
		flowPane.setAlignment(Pos.CENTER);
		flowPane.setPadding(new Insets(16, 16, 48,16));
		
		
		Button addButton = new Button("+");
		addButton.setStyle(Design.getButtonCSS());
		addButton.setOnAction(new  EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				PlayerPanel playerPanel = new PlayerPanel();
				playerPanels.add(playerPanel);
				flowPane.getChildren().add(flowPane.getChildren().size()-1,playerPanel.getVBox());
			}
		});
		flowPane.getChildren().add(addButton);
		
		//settings
		VBox vbSetting = new VBox(8);
		vbSetting.setMaxWidth(250);
		//vbSetting.setAlignment(Pos.CENTER);
		final ToggleGroup group = new ToggleGroup();

		ToggleButton tb1 = new ToggleButton("HRÁT JEDNU HRU");
		tb1.setId("1");
		tb1.setStyle("-fx-text-fill: #ffffff; -fx-cursor: hand; -fx-font-weight: bold;" +Design.getFontMediumCSS());
		tb1.setToggleGroup(group);
		tb1.setSelected(true);

		ToggleButton tb2 = new ToggleButton("SIMULACE");
		tb2.setStyle("-fx-text-fill: #ffffff; -fx-cursor: hand; -fx-font-weight: bold;" +Design.getFontMediumCSS());
		tb2.setToggleGroup(group);
		tb2.setId("2");
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().addAll(tb1, tb2);
		
		
		//boardGameSettings
		cbAnimationSpeed.getItems().add("Pomalu");
		cbAnimationSpeed.getItems().add("Normalně");
		cbAnimationSpeed.getItems().add("Rychle");
		
		HBox hbAnimationInfo = new HBox(4);
		//hbAnimationInfo.setAlignment(Pos.CENTER);
		cbAnimationSpeed.getSelectionModel().select(0);
		Label animLabel = new Label("Rychlost animací");
		animLabel.setPadding(new Insets(4, 0, 4, 0));
		Region raAnim = new Region();
		animLabel.setStyle(Design.getNormalTextWhiteItalicCSS());
		HBox.setHgrow(raAnim, Priority.ALWAYS);
		hbAnimationInfo.getChildren().addAll(animLabel,raAnim,cbAnimationSpeed);

		//hbAnimationInfo.setAlignment(Pos.CENTER);
		
		boardGameSettings.getChildren().add(hbAnimationInfo);
		//Simulations Settings
		final TextField tfNumberOfGames = new TextField();

		HBox hbSimulationsInfo = new HBox(4);
		//hbSimulationsInfo.setAlignment(Pos.CENTER);

		Label numberOfGamesLabel = new Label("Počet simulovaných her");
		numberOfGamesLabel.setPadding(new Insets(4, 0, 4, 0));
		numberOfGamesLabel.setStyle(Design.getNormalTextWhiteItalicCSS());
		tfNumberOfGames.setPrefWidth(90);
		//HBox.setHgrow(tfNumberOfGames, Priority.ALWAYS);
		hbSimulationsInfo.getChildren().addAll(numberOfGamesLabel, tfNumberOfGames);

		tfNumberOfGames.setStyle("-fx-background-color: " + Design.DARK_COLOR + "; -fx-text-fill: #ffffff; -fx-font-weight: bold;" +Design.getFontMediumCSS());
		simulationSettings.getChildren().addAll( hbSimulationsInfo);
		//simulationSettings.setAlignment(Pos.CENTER);

		simulationSettings.setVisible(false);
		simulationSettings.setManaged(false);
		
		
		
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov,
		        Toggle toggle, Toggle new_toggle) {
		    	if(new_toggle==null){
		    		return;
		    	}
		    	if(((ToggleButton)new_toggle).getId().equals("1")){
		    		boardGameSettings.setVisible(true);
		    		boardGameSettings.setManaged(true);
		    		simulationSettings.setVisible(false);
		    		simulationSettings.setManaged(false);
		    	}else{
		    		simulationSettings.setVisible(true);
		    		simulationSettings.setManaged(true);
		    		boardGameSettings.setVisible(false);
		    		boardGameSettings.setManaged(false);
		    		
		    	}
		    }
		});
		
		
		//Preferences
		HBox hBfield = new HBox(2);
		//hBfield.setAlignment(Pos.CENTER);
		Button bChoose = new Button("VYBRAT");
		bChoose.setStyle(Design.getButtonCSS());
		bChoose.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				fileChooser.getExtensionFilters().add(new ExtensionFilter("XML prefs", "*.xml"));
				File file = fileChooser.showOpenDialog(stage);
				if(file!=null){
					tfPrefsFile.setText(file.getAbsolutePath());
				}
			}
		});
		
		Label prefXmlLabel = new Label("Preference xml");
		prefXmlLabel.setPadding(new Insets(16, 0, 0, 0));
		prefXmlLabel.setStyle(Design.getNormalTextWhiteItalicCSS());
		tfPrefsFile.setStyle("-fx-background-color: " + Design.DARK_COLOR + "; -fx-text-fill: #ffffff; -fx-font-weight: bold;" +Design.getFontMediumCSS());
		HBox.setHgrow(tfPrefsFile, Priority.ALWAYS);
		hBfield.getChildren().addAll( tfPrefsFile, bChoose);
		

		vbSetting.getChildren().addAll(hbox, boardGameSettings, simulationSettings, prefXmlLabel, hBfield);

		Button playButton = new Button("SPUSTIT");
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if(((ToggleButton)group.selectedToggleProperty().get())==null){
					chybaNeniVybranaHra();
					return;
				}
				if(!checkFileExist(tfPrefsFile.getText())){
					return;
				}
				if(((ToggleButton)group.selectedToggleProperty().get()).getId().equals("1")){
					if(playerPanels.size()==0){
						chybaZadniHraci();
						
						return;
					}
					eventHandler.handle(null);
				}else{ 
					int games = 0;
					try{
						games = Integer.valueOf(tfNumberOfGames.getText());
					}catch(Exception e){
						chybaNespravnyPocetHer();   
						return;
					}
					final int g = games;
					Thread t = new Thread(){
						public void run() {
							boolean end = false;
							for(PlayerPanel pp : playerPanels){
								if(pp.getPrePlayer().getTypeOfPlayer()==PrePlayer.PLAYER_TYPE_HUMAN){
									end =  true;
								}
							}
							if(end){
								chybaHumanPlayer();
								
								return;
							}
							if(playerPanels.size()==0){
								
								chybaZadniHraci();
								return;
							}
							if(g<=0){
								chybaNespravnyPocetHer();
							}
							
									SimulatedGame simulatedGame = new SimulatedGame(g, tfPrefsFile.getText(),getPrePlayers() );
								
						};
					};
					t.start();
					
				}
			}
		});
		
		VBox vBoxAIList = new VBox();
		Button bAdd = new Button("PŘIDAT AI");
		bAdd.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						AIListView alist = new AIListView();
						alist.getStage().setOnHiding(new EventHandler<WindowEvent>() {

						      public void handle(WindowEvent event) {
					              updateAis();
						      }
						      
						 });
						alist.show();
					}
				});
				
			
			}
		});
		VBox aiLearning = new VBox(4);
		Button bLearning = new Button("UČENÍ AI");
		aiLearning.getChildren().add(bLearning);
		aiLearning.setAlignment(Pos.CENTER);
		bLearning.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				/*Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub*/
						LearningGUI gui = new LearningGUI();
						gui.show();
				/*	}
				});*/
			}
			
		});
		
		Label headline = new Label("LOBBY | ROBOPOLY AI");
		headline.setStyle(Design.getFontMediumCSS() + "-fx-text-fill: white; -fx-font-size: 30px;");
		HBox hboxHeadline = new HBox();
		hboxHeadline.setPadding(new Insets(4));
		hboxHeadline.setAlignment(Pos.CENTER);
		hboxHeadline.setStyle("-fx-background-color: black;");
		hboxHeadline.getChildren().add(headline);
		
		Region empty  = new Region();
		VBox.setVgrow(empty, Priority.ALWAYS);
		Region empty2  = new Region();
		VBox.setVgrow(empty2, Priority.ALWAYS);
		
		HBox downBox = new HBox(16);
		downBox.setAlignment(Pos.CENTER);
		downBox.getChildren().addAll( vBoxAIList, aiLearning);
		downBox.setPadding(new Insets(8));
		downBox.setStyle("-fx-background-color: " + Design.DARK_COLOR+ ";");
		playButton.setStyle(Design.getButtonCSS());
		bLearning.setStyle(Design.getButtonCSS());
		bAdd.setStyle(Design.getButtonCSS());
		
		vbox.getChildren().addAll(hboxHeadline,  empty2,flowPane, vbSetting,  playButton,empty, downBox);
		
		
		vBoxAIList.setAlignment(Pos.CENTER);
		vBoxAIList.getChildren().addAll(bAdd);
		
		pane.setCenter(vbox);
	}
	
	public long getAnimationTime(){
		switch (cbAnimationSpeed.getSelectionModel().getSelectedIndex()) {
		case 0:
			return 1000;
		case 1:
			return 500;
		case 2:
			return 200;
		}
		return 1000;
		
	}
	private boolean checkFileExist(String file){
		if(file.isEmpty()){
			return true;
		}
		File f = new File(file);
		if(f.exists()){
			return true;
		}else{
			chybaPrefXMlNeexistuje(file);
		}
		return false;
	}
	
	private void chybaPrefXMlNeexistuje(String file){
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				OptionDialog dialog = new OptionDialog("Chyba, xml soubor neexistuje!", "Nelze nalézt soubor \"" + file +"\". Zadejte správnou adresu souboru nebo nechte pole prázdné.", false);
				dialog.setTitleColor("#c62828");
				dialog.show();
			}
		});
		
	}
	
	private void chybaZadniHraci(){
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				OptionDialog dialog = new OptionDialog("Chyba, žádní hráči!", "Nelze spustit simulaci. Nejsou nastaveni žádní hráči.", false);
				dialog.setTitleColor("#c62828");
				dialog.show();
			}
		});
		
	}
	
	private void chybaHumanPlayer(){
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				OptionDialog dialog = new OptionDialog("Chyba, nelze spustit!", "Nelze spustit simulaci. Human Player se nemůže účastnit simulací.", false);
				dialog.setTitleColor("#c62828");
				dialog.show();
			}
		});
	}
	
	private void chybaNespravnyPocetHer(){
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				OptionDialog dialog = new OptionDialog("Chyba, špatný počet her!", "Nelze spustit simulaci. Počet her musí být celé číslo větší než 0.", false);
				dialog.setTitleColor("#c62828");
				dialog.show();
			}
		});
	}
	private void chybaNeniVybranaHra(){
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				OptionDialog dialog = new OptionDialog("Chyba, není vybrán typ hry!", "Nelze spustit hru nebo simulaci, není totiž vybrán typ hry. Vyberte si,"
						+ "zda chcete hrát jen jednu hru s grafickým prostředím nebo simulovat víc her.", false);
				dialog.setTitleColor("#c62828");
				dialog.show();
			}
		});
	}
	
	private void updateAis(){
		loadAIFromFile();
		for(PlayerPanel panel: playerPanels){
			panel.update(aiPlayers);
		}
	}
	
	
	private void loadAIFromFile(){
		aiPlayers.clear();
		BufferedReader input =null;
		try {
			input = new BufferedReader(new FileReader(new File(AI_LIST_FILE)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return;
		}
		try {
			String line = null;
			while ((line = input.readLine()) != null) {
				String[] rr = line.split(";");
				aiPlayers.add(new AIItem(rr[0], rr[1]));
				
			}
		}catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	
	
	
	public void show(Stage stage){
		init(stage);
		//DesignStage de = new DesignStage("Robopoly", true, pane, stage);
		updateAis();
		Scene scene = new Scene(pane, 1270, 720); 
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);
		
		
	}
	
	public PrePlayer[] getPrePlayers(){
		PrePlayer[] ret = new PrePlayer[playerPanels.size()];
		for(int i = 0; i<ret.length; i++){
			ret[i] = playerPanels.get(i).getPrePlayer();
		}
		return ret;
	}
	
	public static class AIItem{
		private String name;
		private String file;
		public AIItem(String name, String file) {
			this.name = name;
			this.file = file;
		}
		public String getFile() {
			return file;
		}
		public String getName() {
			return name;
		}
		
	}
	
	private class PlayerPanel{
		private ImageView imageView;
		//private ChoiceBox<String> chBoFigure;
		private TextField fieldName = new TextField();
		private ChoiceBox<String> chBoType;
		private Button butDelete = new Button("ODSTRANIT");
		private VBox vBPlayer= new VBox(4);
		private Button left = new Button(" < "), right = new Button(" > ");
		private int fig;
		private VBox all = new VBox();
		
		
		public PlayerPanel() {
			// TODO Auto-generated constructor stub
			vBPlayer.setPrefWidth(150);
			vBPlayer.setAlignment(Pos.TOP_CENTER);
			//vBPlayer.setPadding(new Insets(8));
			vBPlayer.setStyle("-fx-background-color: "+ Design.DARK_COLOR+";");
			all.setStyle("-fx-border-insets: 16;-fx-background-insets: 16;-fx-effect: dropshadow(three-pass-box, rgba(0, 0,0, .6), 12, 0.2, 1, 0.5);");
			fig = playerPanels.size()%Figure.IMG_FIG.length;
			imageView = new ImageView(Figure.getImage(fig));
			imageView.setFitWidth(100);
			imageView.setFitHeight(100);
			
			/*chBoFigure = new ChoiceBox<String>();
			chBoFigure.setPrefWidth(142);
			*/
			chBoType = new ChoiceBox<String>();
			chBoType.setPrefWidth(142);
			
			fieldName.setPromptText("Jméno hráče");
			/*
			for(int i =0; i<Figure.IMG_FIG.length; i++){
				chBoFigure.getItems().add("Figure " + i);
			}*/
			
			/*chBoFigure.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					// TODO Auto-generated method stub
					imageView.setImage(Figure.getImage(newValue.intValue()));
				}
			});
			chBoFigure.getSelectionModel().select(0);*/
			left.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					// TODO Auto-generated method stub
					fig++;
					imageView.setImage(Figure.getImage(fig%Figure.IMG_FIG.length));
				}
			});
			right.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					// TODO Auto-generated method stub
					fig--;
					if(fig<0){
						fig = Figure.IMG_FIG.length-1;
					}
					imageView.setImage(Figure.getImage(fig%Figure.IMG_FIG.length));
				}
			});
			
			
			
			chBoType.getSelectionModel().select(0);
			
			butDelete.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					delete();
				}
			});
			
			chBoType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					// TODO Auto-generated method stub
					if(newValue.intValue()>=2){
						fieldName.setText(aiPlayers.get(newValue.intValue()-2).getName());
					}
				}
			});
			
			HBox hbox = new HBox();
			//hbox.setStyle("-fx-background-color: " + Design.DARK_COLOR + ";");
			Region regBut = new Region();
			HBox.setHgrow(regBut, Priority.ALWAYS);
			Region regBut2 = new Region();
			HBox.setHgrow(regBut2, Priority.ALWAYS);
			//Label figLabel = new Label("Výběr figurky");
			//figLabel.setStyle(Design.getNormalTextWhiteItalicCSS() +  "-fx-font-size: 8px;-fx-text-fill: #d0d0d0;");
			hbox.getChildren().addAll(left,regBut,/*figLabel, regBut2,*/right);
			
			left.setStyle(Design.getButtonCSS());
			right.setStyle(Design.getButtonCSS());
			

			HBox hboxBut = new HBox();
			hboxBut.setPadding(new Insets(4));
			//hboxBut.setStyle("-fx-background-color: " + Design.DARK_COLOR + ";");
			
			fieldName.setStyle("-fx-background-color: " + Design.DARK_COLOR + "; -fx-text-fill: #ffffff;-fx-font-weight: bold;" +Design.getFontMediumCSS());
			
			VBox vbox = new VBox();
			vbox.setAlignment(Pos.TOP_CENTER);
			
			vbox.setStyle("-fx-background-color: "+ Design.LIGHT_BLACK_COLOR+";");
			vbox.getChildren().addAll(imageView);
			
			butDelete.setStyle(Design.getButtonCSS());
			
			
			hboxBut.getChildren().addAll(butDelete);
			hboxBut.setAlignment(Pos.CENTER);
			vBPlayer.getChildren().addAll(vbox, hbox, fieldName,chBoType ,hboxBut);
			all.getChildren().add(vBPlayer);
			
			update(aiPlayers);
			
			
		}
		public void update(List<AIItem> aiPlayers) {
			// TODO Auto-generated method stub
			chBoType.getItems().clear();
			chBoType.getItems().add("Human Player");
			chBoType.getItems().add("Static AI");
			for(AIItem item: aiPlayers){
				chBoType.getItems().add(item.getName());
			}
			chBoType.getSelectionModel().select(0);
		}
		private void delete(){
			playerPanels.remove(this);
			flowPane.getChildren().remove(getVBox());
		}
		public PrePlayer getPrePlayer(){
			PrePlayer pre = new PrePlayer(fieldName.getText(), fig%Figure.IMG_FIG.length);
			switch(chBoType.getSelectionModel().getSelectedIndex()){
			case 0:
				pre.setType(0);
				break;
			case 1:
				pre.setType(1);
				break;
			default:
				pre.setAIFile(aiPlayers.get(chBoType.getSelectionModel().getSelectedIndex()-2).getFile());
				break;
			}
			return pre;
		}


		public VBox getVBox() {
			// TODO Auto-generated method stub
			return all;
		}
		
		
	}
	
	public String getPrefsFile(){
		return tfPrefsFile.getText();
	}

	public void setEvent(EventHandler<ActionEvent> eventHandler) {
		// TODO Auto-generated method stub
		this.eventHandler= eventHandler;
	}

}

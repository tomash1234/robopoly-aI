package board_gui;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import game_ai.AILearningTest;
import game_ai.LearningAI;
import game_ai.LearningAI.CreatureObserver;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import nn4ai_tools.Creature;
import nn4ai_tools.EvolutionManager;
import nn4ai_tools.LearningInfo;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LearningGUI {
	
	private final VBox vbox = new VBox();
	private final HBox hbox = new HBox(8);
	private static final String LABEL_AVARAGE_ROUNDS = "Průmerný počet kol: ";
	private static final String LABEL_STATIC_BOT_WINRATE = "Statický bot (vítězství): ";
	private LearningAI learning = new LearningAI();
	private final String[] labels = new String[]{"Každý hráč odehraje: ", "Celkový počet her za generaci: "};
	private final Label lNumberGamesForOnePlayer= new Label(labels[0]);
	private final Label lNumberOfTotalGames = new Label(labels[1]);
	private final TextField tfNumberOfCreatures = new TextField();
	private final TextField tfNumberGroups = new TextField();
	private final TextField tfNumberRepeations = new TextField();
	private final TextField tfNumberPlayersInGame = new TextField();
	private  DesignStage stage;
	private final ListView<String> listView = new ListView<>();
	private final ProgressBar pbTest = new ProgressBar();
	private final ProgressBar pbGen = new ProgressBar();
	private final Label tvTestProgress = new Label();
	private final Label tvGenerations = new Label();
	private final Label tvAvarageRounds = new Label(LABEL_AVARAGE_ROUNDS);
	private final Label tvStaticBotWinRate = new Label(LABEL_STATIC_BOT_WINRATE);
	private final TextField tfNumberGeneration = new TextField();
	private final ChoiceBox<String> choiseBoxCreatureSelector = new ChoiceBox<>();
	private final ChoiceBox<String> chooseTest = new ChoiceBox<>();
	
	
	
	private final Button bStart = new Button("START");
	private final Button bStop = new Button("ZASTAVIT");
	private Thread learningThread;
	
	public LearningGUI(){
		VBox leftSide = new VBox(8);
		HBox hBoxCreateCreaturesInfo = new HBox(4);

		
		hBoxCreateCreaturesInfo.setAlignment(Pos.CENTER_LEFT);
		tfNumberOfCreatures.setPrefWidth(40);
		Button buttonCreateCreatures = new Button("VYTVOŘIT");
		buttonCreateCreatures.setStyle(Design.getButtonCSS());
		buttonCreateCreatures.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				
				int i = 0;
				try{
					i= Integer.valueOf(tfNumberOfCreatures.getText());
				}catch(Exception e){
					
				}
				listView.getItems().clear();
				learning.createRandomCreatures(i);
				Iterator<Creature> iterator = learning.getCreatureIterator();
				while(iterator.hasNext()){
					Creature c = iterator.next();
					listView.getItems().add(c.getName() + " ");
				}
			}
		});
		Region ccCretureRegion = new Region();
		Label lCreature =new Label("Počet nových stvoření: ");
		HBox.setHgrow(ccCretureRegion, Priority.ALWAYS);
		tfNumberOfCreatures.setStyle(Design.getEditTextCSS());
		lCreature.setStyle(Design.getNormalTextWhiteItalicCSS());
		hBoxCreateCreaturesInfo.getChildren().addAll(lCreature,ccCretureRegion,tfNumberOfCreatures, buttonCreateCreatures);

		HBox hBoxGroups= new HBox(4);
		hBoxGroups.setAlignment(Pos.CENTER_LEFT);
		tfNumberGroups.setPrefWidth(40);

		Region ccGroupsRegion = new Region();
		Label lGroups =new Label("Počet her, které každý hrát odehraje: ");
		tfNumberGroups.setStyle(Design.getEditTextCSS());
		HBox.setHgrow(ccGroupsRegion, Priority.ALWAYS);
		lGroups.setStyle(Design.getNormalTextWhiteItalicCSS());
		hBoxGroups.getChildren().addAll(lGroups,ccGroupsRegion,tfNumberGroups);


		HBox hBoxRepeatGame =new HBox(4);
		hBoxRepeatGame.setAlignment(Pos.CENTER_LEFT);
		tfNumberRepeations.setPrefWidth(40);
		tfNumberRepeations.setStyle(Design.getEditTextCSS());
		Label lGameRepeat =new Label("Opakování her: ");
		lGameRepeat.setStyle(Design.getNormalTextWhiteItalicCSS());
		Region ccRepeatRegion = new Region();
		HBox.setHgrow(ccRepeatRegion, Priority.ALWAYS);
		hBoxRepeatGame.getChildren().addAll(lGameRepeat,ccRepeatRegion,tfNumberRepeations);
		
		
		HBox hBoxPlayerInGame =new HBox(4);
		hBoxPlayerInGame.setAlignment(Pos.CENTER_LEFT);
		tfNumberPlayersInGame.setPrefWidth(40);
		tfNumberPlayersInGame.setStyle(Design.getEditTextCSS());
		Label lPlayersInGame =new Label("Počet hráčů ve hře: ");
		lPlayersInGame.setStyle(Design.getNormalTextWhiteItalicCSS());
		Region ccPlayersGame = new Region();
		HBox.setHgrow(ccPlayersGame, Priority.ALWAYS);
		hBoxPlayerInGame.getChildren().addAll(lPlayersInGame,ccPlayersGame,tfNumberPlayersInGame);
		
		VBox vboxInfoAboutGame = new VBox(4);
		lNumberGamesForOnePlayer.setStyle(Design.getNormalTextWhiteCSS());
		lNumberOfTotalGames.setStyle(Design.getNormalTextWhiteCSS());
		vboxInfoAboutGame.getChildren().addAll(lNumberGamesForOnePlayer, lNumberOfTotalGames);
		
		
		///
		
		HBox hBoxNumberOfGen= new HBox(4);
		hBoxNumberOfGen.setAlignment(Pos.CENTER_LEFT);
		tfNumberGeneration.setPrefWidth(40);
		tfNumberGeneration.setStyle(Design.getEditTextCSS());
		Label lGameGen =new Label("Počet generací: ");
		Region ccGenRegion = new Region();
		lGameGen.setStyle(Design.getNormalTextWhiteItalicCSS());
		HBox.setHgrow(ccGenRegion, Priority.ALWAYS);
		hBoxNumberOfGen.getChildren().addAll(lGameGen,ccGenRegion,tfNumberGeneration);
		
		
		tfNumberGroups.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 0) {
				update();
			}
		});
		tfNumberPlayersInGame.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 0) {
				update();
			}
		});
		tfNumberRepeations.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 0) {
				update();
			}
		});
		learning.setObserver(new CreatureObserver() {
			
			@Override
			public void change(int number) {
				// TODO Auto-generated method stub
				update();
			}
		});
		
	
		

		HBox hBoxCreatureSelector =new HBox(4);
		choiseBoxCreatureSelector.getItems().addAll("SimpleSelector20", "SimpleSelector20-high mut", "SimpleSelector20-extrem mut", "SimpleSelector40");
		choiseBoxCreatureSelector.getSelectionModel().select(0);
		Region ccCreSelectorRegion = new Region();
		HBox.setHgrow(ccCreSelectorRegion, Priority.ALWAYS);
		Label creSelectoLabel = new Label("Creature selector: ");
		creSelectoLabel.setStyle(Design.getNormalTextWhiteItalicCSS());
		hBoxCreatureSelector.getChildren().addAll(creSelectoLabel,ccCreSelectorRegion, choiseBoxCreatureSelector);
		
		HBox hBoxTestSelector=new HBox(4);
		for(AILearningTest test: LearningAI.LEARNING_TEST){
			chooseTest.getItems().add(test.getNameTest());
			
		}
		chooseTest.getSelectionModel().select(0);
		Region ccTestSelectorRegion = new Region();
		HBox.setHgrow(ccTestSelectorRegion, Priority.ALWAYS);
		Label creTestSelectoLabel = new Label("Test: ");
		creTestSelectoLabel.setStyle(Design.getNormalTextWhiteItalicCSS());
		hBoxTestSelector.getChildren().addAll(creTestSelectoLabel,ccTestSelectorRegion, chooseTest);
		lGroups.setDisable(true);
		tfNumberGroups.setDisable(true);

		
		chooseTest.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				learning.setTestType(newValue.intValue());
				if (newValue.intValue() != 1) {
					lGroups.setDisable(true);
					tfNumberGroups.setDisable(true);

				} else {

					lGroups.setDisable(false);
					tfNumberGroups.setDisable(false);
				}
				
			}
		});
		
		
		leftSide.getChildren().addAll(hBoxCreateCreaturesInfo, hBoxTestSelector, hBoxGroups,hBoxRepeatGame,hBoxPlayerInGame, hBoxNumberOfGen, /*hBoxMutateRate, hboxMutateRange,*/ hBoxCreatureSelector, vboxInfoAboutGame);
		
		
		VBox rightSide = new VBox();
		listView.setPrefHeight(200);
		listView.setPrefWidth(350);
		listView.setStyle(Design.getFontMediumCSS());
		HBox hhListView = new HBox();
		hhListView.getChildren().add(listView);
		HBox hb = new HBox(6);
		rightSide.getChildren().addAll(hhListView, hb);
		Button bClearAll = new Button("VYMAZAT VŠE");
		bClearAll.setStyle(Design.getButtonCSS());
		Button bClear = new Button("VYMAZAT");
		bClear.setStyle(Design.getButtonCSS());
		Button bSave = new Button("ULOŽIT");
		bSave.setStyle(Design.getButtonCSS());
		Button bLoad= new Button("NAČÍST SLOŽKU");
		bLoad.setStyle(Design.getButtonCSS());
		bLoad.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser  dhooser = new DirectoryChooser ();
				//dhooser.setInitialDirectory(new File("C:\\Users\\Tomas\\Documents\\myWork\\RobopolyAI\\AI\\ev\\slot1"));
				dhooser.setTitle("Načíst AI soubory");
				File f =dhooser.showDialog(stage.getStage());
				if(f!=null){
					learning.loadCreatures(f.getAbsolutePath());
					listView.getItems().clear();
					listView.getItems().setAll(learning.getStringValueForCreatures());
				}
			}
		});
		
		learning.setLearingInfo(new LearningInfo() {
			
			@Override
			public void nextGenerationCreated(final List<Creature> creatures, int generation, int total) {
				// TODO Auto-generated method stub
				List<String> re = learning.getResult(creatures);
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						listView.getItems().clear();
						listView.getItems().setAll(re);
						pbGen.setProgress((1.0/total*(generation+1)));
						tvGenerations.setText("Generace: "+(generation+1)+"/" + (total));
						
						tvAvarageRounds.setText(LABEL_AVARAGE_ROUNDS + roundNumber(learning.getAvarageRounds(), 0)); 
						tvStaticBotWinRate.setText(LABEL_STATIC_BOT_WINRATE + roundNumber(learning.getStaticWinRate()*100, 2) + " %"); 
					}
				});
			}


			@Override
			public void creatureTested(int creature, int total) {
				// TODO Auto-generated method stub
				Platform.runLater(new Runnable() {
						
						@Override
						public void run() {
							pbTest.setProgress(1.0/total*(creature+1));
							tvTestProgress.setText("" +creature+"/"+ total);
						}
	
				});
			}
		});
		
		bClearAll.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				learning.clearAll();
				listView.getItems().clear();
			}
		});
		
		bClear.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				learning.clear(listView.getSelectionModel().getSelectedIndex());
				if(listView.getSelectionModel().getSelectedIndex()!=-1&&listView.getSelectionModel().getSelectedIndex()<listView.getItems().size()){
					listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());
				}
			}
		});
		
		bSave.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if(listView.getSelectionModel().getSelectedIndex()==-1){
					return;
				}
				 FileChooser fileChooser = new FileChooser();
		            fileChooser.setTitle("Save robopoly AI");
		            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("AI File", "*.robAI");
		            fileChooser.getExtensionFilters().add(extFilter);
		            File file = fileChooser.showSaveDialog(stage.getStage());
		            
		            if(file!=null){
		            	//uložit
		            	Creature creature = learning.getCreature(listView.getSelectionModel().getSelectedIndex());
		            	creature.setName(file.getName().replace(".robAI", ""));
		            	creature.saveCreature(file.getParentFile().getAbsolutePath(), file.getName(), "");
		            	
		            }
			}
		});
		
		hb.getChildren().addAll(bClearAll, bClear, bSave, bLoad);
		HBox tempBox = new HBox();
		
		Label labelTempDir = new Label("Ukládat průběžně soubory do:\nAdresář se bude přemazávat");
		TextField tfTempDir = new TextField();
		Button vybrat = new Button("VYBRAT");
		vybrat.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				DirectoryChooser  dhooser = new DirectoryChooser ();
				//dhooser.setInitialDirectory(new File("C:\\Users\\Tomas\\Documents\\myWork\\RobopolyAI\\AI\\ev\\slot1"));
				dhooser.setTitle("Vybrat složku na průběžné soubory (složka se bude přemazávat)");
				File f =dhooser.showDialog(stage.getStage());
				if(f!=null){
					tfTempDir.setText(f.getAbsolutePath());
				}
			}
		});
		
		tfTempDir.setStyle(Design.getEditTextCSS());
		labelTempDir.setStyle(Design.getNormalTextWhiteItalicCSS() + "-fx-text-fill: #ddd;-fx-font-size: 10px;");
		labelTempDir.setPadding(new Insets(4, 0, 4, 0));
		vybrat.setStyle(Design.getButtonCSS());
		Region tfBoRegion = new Region();
		HBox.setHgrow(tfBoRegion, Priority.ALWAYS);
		tempBox.getChildren().addAll(labelTempDir, tfBoRegion,tfTempDir, vybrat);
		tempBox.setPadding(new Insets(16, 0, 0, 0));
		

		tvAvarageRounds.setStyle(Design.getNormalTextWhiteCSS() + "-fx-text-fill: #ddd;-fx-font-size: 10px;");
		tvAvarageRounds.setPadding(new Insets(4, 0, 0, 0));
		tvStaticBotWinRate.setStyle(Design.getNormalTextWhiteItalicCSS() + "-fx-text-fill: #ddd;-fx-font-size: 10px;");
		rightSide.getChildren().addAll(tvAvarageRounds, tvStaticBotWinRate);
		rightSide.getChildren().add(tempBox);
		
		hbox.setPadding(new Insets(8));

		hbox.getChildren().addAll(leftSide, rightSide);
		
		HBox buttonBox = new HBox();
		bStart.setStyle(Design.getButtonCSS());
		bStart.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				//String s = "C:\\Users\\Tomas\\Documents\\myWork\\RobopolyAI\\AI\\ev\\slot1";
				
				String s = tfTempDir.getText();
				File f = new File(s);
				if(!f.exists() || !f.isDirectory()){
					chybaTempDir(s);
					return;
				}

				bStart.setDisable(true);
				bStop.setDisable(false);
				tvGenerations.setText("Generace: "+(0)+"/" + getValue(tfNumberGeneration, 0));
				tvTestProgress.setText("0/" + learning.getNumberPerPlayerInOneGaneration(getValue(tfNumberGroups, 1), getValue(tfNumberGeneration, 0), getValue(tfNumberPlayersInGame,4,2)));
				learning.setSelector(choiseBoxCreatureSelector.getSelectionModel().getSelectedIndex());
				
				final String temp = s;
				learningThread= new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						start(temp);
					}
				});
				learningThread.start();
			}
		});

		bStop.setStyle(Design.getButtonCSS());
		bStop.setDisable(true);
		bStop.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if(learning!=null){
		    		learning.end();
		    	 }
				bStart.setDisable(false);
				bStop.setDisable(true);
			}
		});
		buttonBox.getChildren().addAll(bStart, bStop);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setPadding(new Insets(4));
		buttonBox.setStyle(Design.getBackgroundColorCSS(Design.DARK_COLOR));
		
		VBox vboxPB = new VBox();
		tvTestProgress.setStyle(Design.getNormalTextWhiteCSS());
		tvGenerations.setStyle(Design.getNormalTextWhiteCSS());
		vboxPB.setPadding(new Insets(16));
		vboxPB.setAlignment(Pos.CENTER);
		vboxPB.getChildren().addAll( tvTestProgress, pbTest,tvGenerations, pbGen);
		pbTest.setPrefWidth(300);
		pbGen.setPrefWidth(300);
		
		vbox.getChildren().addAll(hbox, vboxPB, buttonBox);
		
	}
	
	private float roundNumber(float num, int des){
		return (float) (Math.floor(num*Math.pow(10, des))/Math.pow(10, des));
	}
	
	public void start(String tempDir){
		learning.start(getValue(tfNumberGroups, 1),getValue(tfNumberRepeations, 1), getValue(tfNumberGeneration, 1), getValue(tfNumberPlayersInGame, 4,2), tempDir);
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				tvTestProgress.setText("Hotovo");
				tvGenerations.setText("Hotovo");

				bStart.setDisable(false);
				bStop.setDisable(true);
			}

	});
		
		
	
	}
	public void show(){
		stage = new DesignStage("Správce učení", true, vbox);
		stage.getStage().setOnHiding(new EventHandler<WindowEvent>() {

		      public void handle(WindowEvent event) {
		    	 if(learning!=null){
		    		learning.end();
		    	 }
		      }
		      
		 });
		stage.show();
	}

	public void update(){
		lNumberOfTotalGames.setText(labels[1] +" " +learning.getNumberOfGamesPerGeneration( getValue(tfNumberGroups, 1), getValue(tfNumberRepeations,1), getValue(tfNumberPlayersInGame,4,2) ));
		lNumberGamesForOnePlayer.setText(labels[0] +" " + learning.getNumberPerPlayerInOneGaneration( getValue(tfNumberGroups, 1), getValue(tfNumberRepeations,1), getValue(tfNumberPlayersInGame,4,2)));
	}
	public int getValue(TextField tf, int def){
		try{
			return Integer.valueOf(tf.getText());
		}catch(Exception e){
			return def;
		}
		
	}
	public int getValue(TextField tf, int def, int min){
		try{
			int a =  Integer.valueOf(tf.getText());
			if(a<min){
				return min;
			}
			return a;
		}catch(Exception e){
			return def;
		}
		
	}
	private void chybaTempDir(String f){
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				OptionDialog dialog = new OptionDialog("Chyba, nelze spustit!", "\"" +f + "\" není platný adresář. Zadejte adresu prázdného adresáře, do kterého budou průběžně ukládány soubory stvoření. Vytvořte nový adresář, adresář se průběžně vymazává.", false);
				dialog.setTitleColor("#c62828");
				dialog.show();
			}
		});
	}
	
}

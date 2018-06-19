package board_gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

import game_ai.NeuronNetworkPlayer;
import game_ai.ArtificialPlayer;
import game_mechanics.Game;
import game_mechanics.GameInfoRequester;
import game_mechanics.GameReferee;
import game_mechanics.Player;
import game_mechanics.PlayerInteraction;
import game_mechanics.PlayerRequester;
import game_mechanics.PrePlayer;
import game_mechanics.Property;
import game_mechanics.Realty;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nn4ai_tools.Creature;

public class GUI extends Application{

	private Game game;
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("RobopolyAI");
	/*	PrePlayer pl = new PrePlayer("Tommy", 1);
		PrePlayer pl2 = new PrePlayer("Luci", 2);
		PrePlayer pl3 = new PrePlayer("Adam", 3);*/
		
		Lobby lobby = new Lobby();
		lobby.setEvent(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							statGame(primaryStage, lobby.getPrePlayers(), lobby.getPrefsFile(),lobby.getAnimationTime());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}); 
			}
		});
		lobby.show(primaryStage);
	
		primaryStage.show();
		primaryStage.setOnHiding(new EventHandler<WindowEvent>() {

		      public void handle(WindowEvent event) {
		    	  if(game!=null){
		    		  game.end();
		    	  }
		    	  
		      }
		      
		 });
	}
	
	private void statGame(Stage primaryStage, PrePlayer[] prePlayers, String prefs, long animTime) throws IOException{

		GameBoard gameBoard = new GameBoard();

		PlayerInteraction[] ppIteraction = new PlayerInteraction[prePlayers.length];
		
		//humanPlayer-always
		HumanPlayer humanPlayer = new HumanPlayer(new GameAnimation(gameBoard, animTime));
		GameInfoRequester gameInfoRequester = new GameInfoRequester();
		gameInfoRequester.addGameInfo(humanPlayer);

		//
		for(int i = 0; i<prePlayers.length; i++){
			switch(prePlayers[i].getTypeOfPlayer()){
			case PrePlayer.PLAYER_TYPE_HUMAN:
				ppIteraction[i] = humanPlayer;
				break;
			case PrePlayer.PLAYER_TYPE_STATIC_AI:
				ppIteraction[i] = new ArtificialPlayer();
				break;
			case PrePlayer.PLAYER_TYPE_AI_FILE:
				ppIteraction[i] = new NeuronNetworkPlayer(new Creature(prePlayers[i].getAiFile()));
				break;
				
			}
		}
		

		PlayerRequester playerRequester = new PlayerRequester();
		playerRequester.setInteractions(ppIteraction);
		
		
		
		
		game = new Game(gameBoard, playerRequester,  gameInfoRequester, prePlayers, prefs);
		
		//Setting human players
		List<Player> humanPlayers = new ArrayList<>();
		for(int i = 0; i<prePlayers.length; i++){
			if(prePlayers[i].getTypeOfPlayer()==PrePlayer.PLAYER_TYPE_HUMAN){
				humanPlayers.add(game.getPlayer(i));
			}
		}
		Player humanPlayersArr[] = new Player[humanPlayers.size()];
		for(int i = 0; i<humanPlayers.size(); i++){
			humanPlayersArr[i] = humanPlayers.get(i);
		}
		humanPlayer.setPlayers(humanPlayersArr);
	
		//
		
		LeftPanel leftPanel = new LeftPanel( game);
		gameInfoRequester.addGameInfo(leftPanel);
		RightPlayerPanel panel = new RightPlayerPanel(humanPlayer);
		humanPlayer.setPanel(panel, leftPanel);
		HBox hbox1 = new HBox();
		if(humanPlayersArr.length==0){
			humanPlayer.noHumanPlayers();
		}
		
		game.start();
		//screenCanvas.addPaintableElement(new PaintableElement(PaintableElement.SHAPE_RECTANGLE, 10, 10, 500, 300, 10,Color.WHITE));
		
		gameBoard.widthProperty().bind(
				hbox1.widthProperty());
		gameBoard.heightProperty().bind(
				hbox1.heightProperty());
		hbox1.getChildren().add(gameBoard);
		

		BorderPane root = new BorderPane(); 
		
		root.setCenter(hbox1);
		root.setLeft(leftPanel);
		root.setRight(panel);
		
		Scene scene = new Scene(root, 1280, 720, Color.BLACK);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		scene.widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		    	Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub

				    	hbox1.resize(newSceneWidth.doubleValue()-500, scene.getHeight());
				    	//root.resize(, height);
					}
				});
		    }
		});
		primaryStage.setScene(scene);
	}
	
	
	public GUI() {
		// TODO Auto-generated constructor stub
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	

}

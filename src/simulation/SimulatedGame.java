package simulation;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import board_gui.GameSummary;
import game_ai.NeuronNetworkPlayer;
import game_ai.ArtificialPlayer;
import game_mechanics.Deal;
import game_mechanics.Game;
import game_mechanics.GameReferee;
import game_mechanics.Player;
import game_mechanics.PlayerInteraction;
import game_mechanics.PlayerRequester;
import game_mechanics.PrePlayer;
import game_mechanics.Property;
import game_mechanics.Realty;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nn4ai_tools.Creature;

public class SimulatedGame {
	
	private  AtomicBoolean run = new AtomicBoolean(true);
	public SimulatedGame(int numberOfGames, String prefs, PrePlayer[] prePlayers) {
		// TODO Auto-generated constructor stub

		NoneGraphics noneGraphics = new NoneGraphics();
		Game game =null;
		SimulationGUI  gameInfo = new SimulationGUI(numberOfGames);
		
	

		//PrePlayer[] pp = new PrePlayer[]{new PrePlayer("Edie", 0),new PrePlayer("Melvin", 0), new PrePlayer("Bender", 0) };
		

		PlayerRequester playerRequester = new PlayerRequester();
		
		PlayerInteraction[] iteractions = new PlayerInteraction[prePlayers.length];
		for(int i=0; i<iteractions.length; i++){
			switch(prePlayers[i].getTypeOfPlayer()){
			case PrePlayer.PLAYER_TYPE_HUMAN:
				System.err.println("Human cant play simulations");
				break;
			case PrePlayer.PLAYER_TYPE_AI_FILE:
				 try {
					iteractions[i] = new NeuronNetworkPlayer(new Creature(prePlayers[i].getAiFile()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case PrePlayer.PLAYER_TYPE_STATIC_AI:
				iteractions[i] = new ArtificialPlayer();
				break;
			}
		}
		
		playerRequester.setInteractions(iteractions);
		
		


		gameInfo.setEnded(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				run.set(false);
			}
		});
		
		for(int i = 0; i<numberOfGames&&run.get(); i++){
			game = new Game(noneGraphics, playerRequester, gameInfo, prePlayers, true, prefs);
		}
		final Game gameS = game;
		/*if(run.get()){

			Platform.runLater(new Runnable() {
				public void run() {
			GameSummary gS= new GameSummary(gameS.getPlayerOrder(), gameS.getStats());
			Stage stage = new Stage();
			stage.setScene(gS.getScene());
			stage.show();
				}});
		}*/
	}
	
}

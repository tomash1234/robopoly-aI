package game_ai;

import java.util.Arrays;
import java.util.List;

import game_mechanics.Game;
import game_mechanics.Player;
import game_mechanics.PlayerInteraction;
import game_mechanics.PlayerRequester;
import game_mechanics.PrePlayer;
import game_mechanics.Stats;
import nn4ai_tools.Creature;
import nn4ai_tools.EvolutionManager;
import nn4ai_tools.LearningInfo;
import simulation.NoneGraphics;
import simulation.TextGameInfo;

public class LearningAgainsStaticPlayers implements AILearningTest{

	//private EvolutionManager evolutionManager;
	private int[] games;
	private int[] wins;
	private int gamesSimulated;
	private float[] fitness;
	private int repeats;
	private int numberOfCreature;
	private int creatureNotWin = 0;
	private int playersInGame = 0;
	private int rounds = 0;
	private float winRateStatic, avRounds;
	private EvolutionManager evolutionManager;
	
	@Override
	public float testCreature(Creature creature, int index) {
		// TODO Auto-generated method stub
		return testGameWithStatic(creature, index);
	}

	@Override
	public int getNumberOfGamesPerPlayer(int players, int repeats, int numberOfMatches, int playersInGame) {
		// TODO Auto-generated method stub
		return repeats;
	}

	@Override
	public int getNumberOfGamesPerGeneration(int players, int repeats, int numberOfMatches, int playersInGame) {
		// TODO Auto-generated method stub
		return players*repeats;
	}

	private void startGameAgainstStatic(Creature c, int id){
		NoneGraphics graphics = new NoneGraphics();
		TextGameInfo gameInfo = new TextGameInfo();
		PlayerRequester playerRequester = new PlayerRequester();
		PrePlayer prePlayer[] = new PrePlayer[playersInGame];
		prePlayer[0] = new PrePlayer(c.getName() +" id" + id, 0);
		
		
		PlayerInteraction  interaction[] = new PlayerInteraction[prePlayer.length];
		for(int i = 0; i<prePlayer.length; i++){
			if(i>0){
				prePlayer[i] = new PrePlayer("Static AI Player", 0);
				interaction[i] = new ArtificialPlayer();
				continue;
			}
			NeuronNetworkPlayer artificialNeuronNetworkPlayer = new NeuronNetworkPlayer(c);
			interaction[i] = artificialNeuronNetworkPlayer;
		}
		playerRequester.setInteractions(interaction);
		
		Game game = new Game(graphics, playerRequester, gameInfo, prePlayer, true, null);
		game.setMainPlayer(0);
		/*gamesStatic++;
		winsStatic +=(game.getPlayerOrder().get(game.getPlayerOrder().size()-1).getPlayerId()==end.length-1)?1:0;*/
		
		int order = getOlder(game.getPlayerOrder(), 0);
		if(order==playersInGame-1){
			wins[id]++;
		}else{
			creatureNotWin++;
		}
		games[id]++;
		rounds += game.getStats().getRounds(playersInGame-1);
		countFitnessBasic(order, 0, game.getStats(), id);
			
		
		gamesSimulated++;
		
		//learningInfo.creatureTested(gamesSimulated, totalGames );
		
	}
	@Override
	public void setLearningInfo(LearningInfo info) {
		// TODO Auto-generated method stub
		
	}
	private float countFitnessBasic(int order, int gameId, Stats stats, int creatureId){
		float fitness = 0;
		if(order==playersInGame-1){
			fitness+=150;
		}else if(order==playersInGame-2){
			fitness+=80;
		}else if(order==playersInGame-3){
			fitness+=20;
		}
		fitness+=stats.getEliminations(gameId)*10;
		/*if(order==3 && stats.getTotalMoney(0)+stats.getTotalMoney(1)+stats.getTotalMoney(2)+stats.getTotalMoney(3)==stats.getTotalMoney(gameId)){
			fitness+=20;
		}*/
		if(stats.getMoneyHouses(gameId)>0){
			fitness+=30;
		}
		fitness+=0.001f*stats.getMoneyGotFromPlayer(gameId);
		fitness+=0.0005f * stats.getRounds(gameId);
		fitness+=0.00005f*stats.getTotalMoney(gameId);
		
		this.fitness[creatureId] += fitness;
		return fitness;
		
	}
	private int getOlder(List<Player> pl, int a){
		for(int i = 0; i<pl.size(); i++){
			if(pl.get(i).getPlayerId()==a){
				return i;
			}
		}
		return pl.size();
	}
	private float testGameWithStatic(Creature creature, int id){
		if(id==0){

			fitness = new float[evolutionManager.getPopulationSize()];
			wins = new int[evolutionManager.getPopulationSize()];
			games = new int[evolutionManager.getPopulationSize()];
			gamesSimulated =0;
			creatureNotWin= 0;
			rounds = 0;
			for(int i = 0; i<evolutionManager.getPopulationSize(); i++){
				evolutionManager.getCreature(i).setId(i);
			}
		}
		for(int i = 0; i<repeats; i++){
			startGameAgainstStatic(creature, id);
		}
		if(id==evolutionManager.getPopulationSize()-1){
			winRateStatic = 1f*creatureNotWin/gamesSimulated;
			avRounds = 1f*rounds/gamesSimulated;
		}
		return fitness[id];
	}

	

	@Override
	public void setNumberOfCreatures(int number, int groups, int repeats, EvolutionManager evolutionManager) {
		// TODO Auto-generated method stub
		this.evolutionManager = evolutionManager;
		this.numberOfCreature=evolutionManager.getPopulationSize();
		this.playersInGame = number;
		this.repeats = repeats;

		
	}

	@Override
	public String getNameTest() {
		// TODO Auto-generated method stub
		return "Hrát jen proti statickým";
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getFitness(int id) {
		// TODO Auto-generated method stub
		return fitness[id];
	}

	@Override
	public float getWinPercent(int id) {
		// TODO Auto-generated method stub
		float a = (1.0f/games[id])*wins[id];
		return a;
	}

	@Override
	public int getAutoSave() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public float getStaticWinRate() {
		// TODO Auto-generated method stub
		return winRateStatic;
		//return (1.f*creatureNotWin)/gamesSimulated;
	}

	@Override
	public float getAvarageRounds() {
		// TODO Auto-generated method stub
		return avRounds;
		//return  (1.f*rounds)/gamesSimulated;
	}

}

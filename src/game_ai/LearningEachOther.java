package game_ai;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

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

public class LearningEachOther implements AILearningTest{
	

	//private EvolutionManager evolutionManager;
	private int[] score;
	private int[] games;
	private int[] wins;
	private int gamesSimulated;
	private int totalGames;
	private float[] fitness;
	private int winsStatic, gamesStatic;
	private int numberOfMatches;
	private int repeats;
	private int rounds;
	private int playersInGame;
	private AtomicBoolean go = new AtomicBoolean(true);
	private EvolutionManager evolutionManager;
	
	private  LearningInfo learningInfo;
	
	private float winRateStatic, avRounds;

	@Override
	public void setLearningInfo(LearningInfo info) {
		// TODO Auto-generated method stub
		learningInfo = info;
	}
	
	@Override
	public float testCreature(Creature creature, int index) {
		// TODO Auto-generated method stub
		if(index==0){
			//testAlLCreatures(numberOfMatches, repeats, playersInGame);
			 testAll();
			return 1f*fitness[creature.getId()]/games[creature.getId()];
		}else{
			return 1f*fitness[creature.getId()]/games[creature.getId()];
		}
	}

	private void testAll(){
		
		for(int i = 0; i<evolutionManager.getPopulationSize(); i++){
			evolutionManager.getCreature(i).setId(i);
		}
		testCreaturesNewType();
	}
	
	@Override
	public int getNumberOfGamesPerPlayer(int players, int repeats, int numberOfMatches, int playersInGame) {
		return numberOfMatches*repeats;
	}

	@Override
	public int getNumberOfGamesPerGeneration(int players, int repeats, int numberOfMatches, int playersInGame) {
		// TODO Auto-generated method stub
		
		int po = repeats*numberOfMatches*players;
		int total = (int)Math.ceil(po*1.0/ (playersInGame*1.0));
		return total;
	}

	public static BigInteger binomial(final int N, final int K) {
		
	    BigInteger ret = BigInteger.ONE;
	    for (int k = 0; k < K; k++) {
	        ret = ret.multiply(BigInteger.valueOf(N-k))
	                 .divide(BigInteger.valueOf(k+1));
	    }
	    return ret;
	}
	private void fillListWithPlayersId(List<Integer> list){
		for(int i= 0; i<evolutionManager.getPopulationSize(); i++){
			list.add(new Integer(i));
		}
	}

	private void testCreaturesNewType(){
		games = new int[evolutionManager.getPopulationSize()];fitness = new float[evolutionManager.getPopulationSize()];
		wins  = new int[evolutionManager.getPopulationSize()];
		gamesSimulated = 0;gamesStatic = 0;winsStatic = 0; rounds=0;
		totalGames = getNumberOfGamesPerGeneration(evolutionManager.getPopulationSize(), repeats, numberOfMatches, playersInGame);
		
		int[] repeatCount =new int[evolutionManager.getPopulationSize()];
		List<Integer> idList = new ArrayList<>();
		fillListWithPlayersId(idList);
	
		Random random = new Random();
		
		while (idList.size() >= playersInGame && go.get()) {
			List<Integer> myiIdList = new ArrayList<>(idList);
			int pl[] = new int[playersInGame];
			for (int i = 0; i < playersInGame; i++) {
				int rndPos = random.nextInt(myiIdList.size());
				Integer id = myiIdList.remove(rndPos);
				if (repeatCount[id] == numberOfMatches - 1) {
					idList.remove(id);
				}
				repeatCount[id] ++;
				pl[i] = id;
			}
			for(int a = 0; a<repeats; a++){
				startMatch(pl, pl.length);
				
			}
		}
		while(!idList.isEmpty() && go.get()){
			int l = idList.size();
			int pl[] = new int[playersInGame];
			for(int i = 0; i<idList.size(); i++){
				Integer id = idList.get(i);
				if (repeatCount[id] >= numberOfMatches - 1) {
					idList.remove(id);
				}
				repeatCount[id] ++;
				pl[i] = id;
				
			}
			for(int i = l; i<pl.length; i++){
				pl[i] = random.nextInt(evolutionManager.getPopulationSize());
			}
			for(int a = 0; a<repeats; a++){
				startMatch(pl, l);
			}
		}
		winRateStatic = getWinRateStaticAI();
		avRounds = (1f*rounds)/gamesSimulated;
	}
	
	private void startMatch(int[] playerIds, int size){
		NoneGraphics graphics = new NoneGraphics();
		TextGameInfo gameInfo = new TextGameInfo();
		PlayerRequester playerRequester = new PlayerRequester();
		PrePlayer prePlayer[] = new PrePlayer[playerIds.length+1];
		for(int i = 0; i<prePlayer.length-1; i++){
			prePlayer[i] = new PrePlayer(evolutionManager.getCreature(i).getName() +" id" + playerIds[i], 0);
		}
		prePlayer[prePlayer.length-1] = new PrePlayer("Static AI Player", 0);
		
		PlayerInteraction  interaction[] = new PlayerInteraction[prePlayer.length];
		for(int i = 0; i<prePlayer.length; i++){
			if(i==prePlayer.length-1){
				interaction[i] = new ArtificialPlayer();
				continue;
			}
			NeuronNetworkPlayer artificialNeuronNetworkPlayer = new NeuronNetworkPlayer(evolutionManager.getCreature(playerIds[i]));
			interaction[i] = artificialNeuronNetworkPlayer;
		}
		playerRequester.setInteractions(interaction);
		
		Game game = new Game(graphics, playerRequester, gameInfo, prePlayer, true, null);
		//
		int[] end = new int[prePlayer.length];
		
		gamesStatic++;
		winsStatic +=(game.getPlayerOrder().get(game.getPlayerOrder().size()-1).getPlayerId()==end.length-1)?1:0;
		rounds+=game.getStats().getRounds(playersInGame);

		for(int i = 0;i<end.length-1&&i<size; i++){
			end[i] =game.getStats().getPlayerEnded(i);
			
			wins[playerIds[i]]+=(game.getPlayerOrder().get(game.getPlayerOrder().size()-1).getPlayerId()==i)?1:0;
			games[playerIds[i]]++;
			countFitness(getOlder(game.getPlayerOrder(), i), i, game.getStats(), playerIds[i]);
		}
		gamesSimulated++;
		learningInfo.creatureTested(gamesSimulated, totalGames);
	}
	
	
	public float getWinRateStaticAI(){
		return (1f*winsStatic)/ gamesStatic;
	}
	

	
	private float countFitness(int order, int gameId, Stats stats, int creatureId){
		float fitness = 0;
		if(order==playersInGame){
			fitness+=200;
		}else if(order==playersInGame-1){
			fitness+=80;
		}else if(order==playersInGame-2){
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
		

	
	public void stop(){
		go.set(false);
		if(evolutionManager!=null){
		evolutionManager.stop();
		}
	}

	

	private int getOlder(List<Player> pl, int a){
		for(int i = 0; i<pl.size(); i++){
			if(pl.get(i).getPlayerId()==a){
				return i;
			}
		}
		return pl.size();
	}
	@Override
	public int getAutoSave() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void setNumberOfCreatures(int number, int numberOfMatches, int repeats, EvolutionManager evolutionManager) {
		// TODO Auto-generated method stub
		this.playersInGame = number;
		this.repeats = repeats;
		this.numberOfMatches = numberOfMatches;
		this.evolutionManager = evolutionManager;

		go.set(true);
	}

	@Override
	public String getNameTest() {
		// TODO Auto-generated method stub
		return "Hrát všichni proti všem";
	}

	@Override
	public float getFitness(int id) {
		// TODO Auto-generated method stub
		return fitness[id];
	}

	@Override
	public float getWinPercent(int id) {
		// TODO Auto-generated method stub
		return wins[id]*1.0f/games[id];
	}

	@Override
	public float getStaticWinRate() {
		// TODO Auto-generated method stub
		return winRateStatic;
	}

	@Override
	public float getAvarageRounds() {
		// TODO Auto-generated method stub
		return avRounds;
	}


}

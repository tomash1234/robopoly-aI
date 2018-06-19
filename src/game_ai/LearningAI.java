package game_ai;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import game_mechanics.Game;
import game_mechanics.Player;
import game_mechanics.PlayerInteraction;
import game_mechanics.PlayerRequester;
import game_mechanics.PrePlayer;
import game_mechanics.Property;
import game_mechanics.Stats;
import javafx.scene.control.ListView;
import nn4ai_tools.Creature;
import nn4ai_tools.EvolutionManager;
import nn4ai_tools.FitnessTest;
import nn4ai_tools.LearningInfo;
import nn4ai_tools.NeuralNetworkAttr;
import nn4ai_tools.SimpleSelector;
import simulation.NoneGraphics;
import simulation.TextGameInfo;

public class LearningAI implements FitnessTest{
	
	
	//private List<Creature> creatures = new ArrayList<>();
	/*private int [] score;
	private int [] wins;
	private int [] games;
	private float [] fitness;*/
	public static final String CREATURE_FILE_SUFFIX = "robAI";
	private int groups, repeats;
/*	private int gamesStatic;
	private int winsStatic;*/
	private AtomicBoolean running = new AtomicBoolean(false);

	private CreatureObserver observer;
	private int nnPlayerInOneGroup = 0;
	private EvolutionManager evolutionManager = new EvolutionManager(this, SimpleSelector.createSimpleSelectorFor20(0));
	private LearningInfo learningInfo;
	//private int totalGames = 0;
	//private int gamesSimulated;
	private int testType = 0;
	public static final AILearningTest[] LEARNING_TEST = new AILearningTest[]{ new LearningAgainsStaticPlayers(), new LearningEachOther()}; 
			
	public void createRandomCreatures(int number){
		for(int i = 0; i<number; i++){
			NeuralNetworkAttr[] attrs = new NeuralNetworkAttr[NeuronNetworkPlayer.SCHEMAS.length];
			for(int j = 0; j<attrs.length; j++){
				attrs[j] = new NeuralNetworkAttr(NeuronNetworkPlayer.SCHEMAS[j], NeuronNetworkPlayer.MIN_WEIGHT, NeuronNetworkPlayer.MAX_WEIGHT);
			}
			/*NeuralNetworkAttr networkAttrs = new NeuralNetworkAttr(new int[]{4, 3,1}, -1000, 1000);
			NeuralNetworkAttr networkAttrsBuying = new NeuralNetworkAttr(new int[]{3, 2,1}, -1000, 1000);*/
			Creature creature = new Creature(attrs);
			evolutionManager.addCreature(creature);
		}
		if(observer!=null){
			observer.change(evolutionManager.getPopulationSize());
		}
	}
	public void loadCreatures(String dir){
		evolutionManager.addAllCreatures(EvolutionManager.loadAllCreaturesFromDir(dir, CREATURE_FILE_SUFFIX));
		if(observer!=null){
			observer.change(evolutionManager.getPopulationSize());
		}
	}
	
	public void clear(int index){
		if(index==-1||index>=evolutionManager.getPopulationSize()){
			return;
		}
		evolutionManager.removeCreature(index);
		
		if(observer!=null){
			observer.change(evolutionManager.getPopulationSize());
		}
	}
	
	public void setSelector(int a){
		switch(a){
		case 0:
			evolutionManager.setCreatureSelector(SimpleSelector.createSimpleSelectorFor20(0));
			break;
		case 1:
			evolutionManager.setCreatureSelector(SimpleSelector.createSimpleSelectorFor20(0, 2.f));
			break;
		case 2:
			evolutionManager.setCreatureSelector(SimpleSelector.createSimpleSelectorFor20(0, 3.f));
			break;
			
		case 3:
			evolutionManager.setCreatureSelector(SimpleSelector.createSimpleSelectorFor40(0));
			break;
		}
	}
	public Creature getCreature(int index){
		return evolutionManager.getCreature(index);
	}

	public void setObserver(CreatureObserver observer) {
		this.observer = observer;
	}
	
	public interface CreatureObserver{
		void change(int number);
		
	}
	
	public void clearAll(){
		evolutionManager.clearAll();
		if(observer!=null){
			observer.change(evolutionManager.getPopulationSize());
		}
	}
	public int getNumberOfGamesPerGeneration( int games, int repeat, int playersInGame){
		return LEARNING_TEST[testType].getNumberOfGamesPerGeneration(evolutionManager.getPopulationSize(), repeat, games, playersInGame);
		/*if(testType==0){
			return BigInteger.valueOf(evolutionManager.getPopulationSize()*repeat);
		}
		int players = evolutionManager.getPopulationSize();
		int perGroup = players/groups;
		int lastGroup = players%groups;
		BigInteger i = binomial(perGroup, 3).multiply(BigInteger.valueOf(groups==1?1:(groups-1)));
		i.add(binomial(lastGroup, 3));
		return i.multiply(BigInteger.valueOf(repeat).multiply(BigInteger.valueOf(4)));*/
		
	}
	
	public int getNumberPerPlayerInOneGaneration(int games, int repeat, int playersInGame){
		//if(testType==0){
		return LEARNING_TEST[testType].getNumberOfGamesPerPlayer(evolutionManager.getPopulationSize(), repeat, games,playersInGame);
		/*	return repeat;
		}
		
		int players = evolutionManager.getPopulationSize();
		int perGroup = players/groups;
		if(perGroup<=2){
			return 0;
		}
		if(perGroup<=0){
			perGroup = players%groups;
		}
		return (perGroup-1)*(perGroup-2)*repeat/2;*/
	}
	

	
	public Iterator<Creature> getCreatureIterator(){
		return evolutionManager.getCreaturesPublic().iterator();
	}
	
	
	
	public void setTestType(int testType) {
		if(!running.get()){
			this.testType = testType;
			
		}
	}
	
	public void start(int groups, int repeats, int generation, int playersInGame, String tempDir) {
		// TODO Auto-generated method stub
		running.set(true);
		
		this.groups=groups;
		LEARNING_TEST[testType].setLearningInfo(learningInfo);
		this.repeats = repeats;
		LEARNING_TEST[testType].setNumberOfCreatures(playersInGame, groups, repeats, evolutionManager);
		evolutionManager.startEvolvingAutoSave(generation, tempDir, LEARNING_TEST[testType].getAutoSave(), CREATURE_FILE_SUFFIX);
		running.set(false);
	}
	
	public void end(){
		//go.set(false);
		LEARNING_TEST[testType].stop();
		running.set(false);
	}
	
	
	/*public List<String> getResult(){
		List<String> ll =new ArrayList<>();
		for(int i = 0; i<evolutionManager.getPopulationSize(); i++){
			Creature c = evolutionManager.getCreature(i);
			int id = c.getId();
			ll.add(c.getName() + "(" + ( (Math.floor(1f*wins[id]/games[id]*1000)/10)+"% )"  + " " + Math.floor(1f*fitness[id]/games[id]*1000)/10));
		}
		return ll;
	}*/
	
	public List<String> getResult(List<Creature> cc){
		List<String> ll =new ArrayList<>();
		for(Creature c: cc){
			int id = c.getId();
			ll.add(c.getName() + "(" + ( (Math.floor(LEARNING_TEST[testType].getWinPercent(id)*1000)/10)+"% )"  + " " + Math.floor(LEARNING_TEST[testType].getFitness(id))));
		}
		return ll;
	}
	public float getAvarageRounds(){
		return LEARNING_TEST[testType].getAvarageRounds();
	}
	public float getStaticWinRate(){
		return LEARNING_TEST[testType].getStaticWinRate();
	}
	
	public List<String> getStringValueForCreatures(){
		List<String> ll =new ArrayList<>();
		for(int i = 0; i<evolutionManager.getPopulationSize(); i++){
			Creature c = evolutionManager.getCreature(i);
			ll.add(c.getName());
		}
		return ll;
		
	}
	@Override
	public float testCreature(Creature creature, int index) {
		// TODO Auto-generated method stub
		
		//simulovat vše a poté postupně vracet
		/*if(testType==0){
			LearningAgainsStaticPlayers ll =new LearningAgainsStaticPlayers();
			return ll.testCreature(creature, index);
			//return testGameWithStatic(creature, index);
		}else if(testType==1){
			LearningEachOther eachOther = new LearningEachOther();
			return eachOther.testCreature(creature, index);
		}*/
		
		/*if(index==0){
			testAlLCreatures(groups, repeats);
			return 1f*fitness[creature.getId()]/games[creature.getId()];
		}else{
			return 1f*fitness[creature.getId()]/games[creature.getId()];
		}*/
		creature.setId(index);
		return LEARNING_TEST[testType].testCreature(creature, index);
	}
	
	public void setLearingInfo(LearningInfo learningInfo) {
		// TODO Auto-generated method stub
		this.learningInfo = learningInfo;
		evolutionManager.setLearningInfo(learningInfo);
	}

}

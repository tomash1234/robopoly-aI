package game_ai;

import java.util.Arrays;
import java.util.List;

import nn4ai_tools.Creature;
import nn4ai_tools.CreatureSelector;
import nn4ai_tools.EvolutionManager;
import nn4ai_tools.FitnessTest;
import nn4ai_tools.LearningInfo;
import nn4ai_tools.NeuralNetwork;
import nn4ai_tools.NeuralNetworkAttr;
import nn4ai_tools.SimpleSelector;

public class BasicLearning /*implements AILearningTest*/{


	public static final float[][] TEST_DATA_INPUT =new float[][]{{1, 0.2f, 0.5f, 0.5f, 0},
		{1, 0.4f,0.5f, 0.8f, 1},
		{1, 0.0f,0.5f, 0.0f, 0},
		{0.5f, 0.0f,0.5f, 1, 0},
		{0.5f, 0.0f,0.5f, 0.5f, 0},
		{0.5f, 0.0f, 0.5f,0.5f, 0.2f},
		{0.3f, 0.0f, 0.5f,0.8f, 0.5f},
		{0.2f, 0.0f, 0.5f,0.5f, 0.5f},
		{0.3f, 0.0f, 0.1f,0.9f, 0.9f},
		{0.3f, 1.0f, 0.1f,0.9f, 0.9f},
		{0.5f, 1.0f, 0.1f,0.2f, 0.9f},
		{0.5f, 0f, 0.1f,0.2f, 0.9f},
	};
	public static final float[][] TEST_DATA_OUTPUT=new float[][]{ {1f},
		{1f},
		{1f},
		{0.3f},
		{0.8f},
		{0.6f},
		{0.2f},
		{0.3f},
		{0},
		{0},
		{0.7f},
		{0.7f},
	
	};

	
	private float[] fitness;
	
	//@Override
	public int getNumberOfGamesPerPlayer(int players, int repeats, int numberOfMatches, int playersInGame) {
		// TODO Auto-generated method stub
		return repeats;
	}

	//@Override
	public int getNumberOfGamesPerGeneration(int players, int repeats, int numberOfMatches, int playersInGame) {
		// TODO Auto-generated method stub
		return players*repeats;
	}

	//@Override
	public float testCreature(Creature creature, int index) {
		// TODO Auto-generated method stub
		float f= testNeuralNetwork(creature.getNeuralNetworkAttr(0), TEST_DATA_INPUT,TEST_DATA_OUTPUT);
		fitness[index] = f;
		return f;
	}
	
	
	public static float testNeuralNetwork(NeuralNetworkAttr attr, float[][] input, float[][] output){
		NeuralNetwork network = new NeuralNetwork(attr.getSchema(), attr.getWeights(), attr.getMin(), attr.getMax());
		float abs = output.length*output[0].length;
		for(int i =0; i<input.length; i++){
			float[] out = network.resolve(input[i]);
			for(int j = 0; j<out.length; j++){
				float f = out[j]-output[i][j];
				abs-= f*f;
			}
		}
		return abs*100000;
	}

	//@Override
	public void setNumberOfCreatures(int number, int numberOfMatches, int repeats, EvolutionManager evolutionManager) {
		// TODO Auto-generated method stub
		fitness= new float[number];
		
	}
	public void setLearningInfo(LearningInfo info) {};

	//@Override
	public String getNameTest() {
		// TODO Auto-generated method stub
		return "Test 1: základy ceny nemovitostí";
	}

	//@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public float getFitness(int id) {
		// TODO Auto-generated method stub
		return fitness[id];
	}

	//@Override
	public float getWinPercent(int id) {
		// TODO Auto-generated method stub
		return fitness[id]/(TEST_DATA_OUTPUT.length*TEST_DATA_OUTPUT[0].length*100000);
		
	}

	

	

	/*@Override
	public void testAll(EvolutionManager evolutionManager) {
		// TODO Auto-generated method stub
		int i = 0;
		for(Creature c: evolutionManager.getCreaturesPublic()){
			testCreature(c, i);
		}
	}*/

	
	
	//@Override
	public int getAutoSave() {
		// TODO Auto-generated method stub
		return 10;
	}
	/*
	public static void test(){
		//Neuronová sít se snaží zjistit, jak funguje hra kámen nůžky papír
		FitnessTest fitnessTest = new FitnessTest() {
			@Override
			public float testCreature(Creature creature, int index) {	
				// TODO Auto-generated method stub
				NeuralNetworkAttr neuralNetworkAttr = creature.getNeuralNetworkAttr(0);
				NeuralNetwork nn = new NeuralNetwork(neuralNetworkAttr);
				//{kámen, nůžky, papír}
				float[][] testDataIn = new float[][]{{1, 0, 0}, {0,1,0}, {0, 0, 1}};	//testovací data
				float[][] testDataOut = new float[][]{{0, 0, 1}, {1,0,0}, {0, 1, 0}};
				float fitness = 1000;
				for(int i = 0; i<testDataIn.length; i++){
					float[] out = nn.resolve(testDataIn[i]);
					for(int j = 0; j<out.length; j++){
						fitness-=Math.pow(out[j]-testDataOut[i][j], 2.0);
					}
				}
				return fitness;
			}
		};
		CreatureSelector creatureSelector = SimpleSelector.createSimpleSelectorFor20(0);
		int[] schema = new int[] { 3, 3 };
		float weightMin = -1000;
		float weightMax = 1000;

		EvolutionManager evolutionManager = new EvolutionManager(fitnessTest, creatureSelector);
		for (int i = 0; i < 20; i++) { // vygeneruje 20 náhodných stvoření
			evolutionManager.addCreature(new Creature(new NeuralNetworkAttr(schema, weightMin, weightMax)));
		}
		evolutionManager.startEvolving(10); // počet generací
		Creature bestCreature = evolutionManager.getCreature(0); // nejlepší stvoření po 10 generací, které ovládá hru kámen nužky papír
		
	}
	public static void main(String[] args) {
		test();
	}
	*/
}

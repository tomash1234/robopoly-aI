package game_ai;

import java.util.List;

import nn4ai_tools.Creature;
import nn4ai_tools.EvolutionManager;
import nn4ai_tools.FitnessTest;
import nn4ai_tools.LearningInfo;

public interface AILearningTest extends FitnessTest{
	
	int getNumberOfGamesPerPlayer(int players, int repeats, int numberOfMatches, int playersInGame);
	int getNumberOfGamesPerGeneration(int players, int repeats, int numberOfMatches, int playersInGame);

	//void testAll(EvolutionManager evolutionManager);
	void setNumberOfCreatures(int playersInGame, int numberOfMatches, int repeats, EvolutionManager evolutionManager);
	
	String getNameTest();
	
	void stop();
	
	float getFitness(int id);
	float getWinPercent(int id);
	int getAutoSave();
	
	float getStaticWinRate();
	float getAvarageRounds();
	
	void setLearningInfo(LearningInfo info);

}

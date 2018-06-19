package nn4ai_tools;

import java.util.List;
/**
 * 
 * @author Tomáš Hromada
 *
 * Toto slouží k informování o průběhu procesu evoluce
 */

public interface LearningInfo {
	/**
	 * Metoda je volána, po tom, co se vytvoří nová generace 
	 * @param creatures nová generace
	 * @param generation číslo generace
	 * @param totalGeneration celkový počet generací
	 */
	void nextGenerationCreated(List<Creature> creatures, int generation, int totalGeneration);
	/**
	 * Metoda je volána potom, co je konkretní svoření testováno
	 * @param creature index stvoření které bylo právě testováno
	 * @param total celkový počet stvoření
	 */
	void creatureTested(int creature, int total);
	

}

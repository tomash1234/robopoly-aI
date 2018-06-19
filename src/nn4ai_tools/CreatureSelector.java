package nn4ai_tools;

import java.util.List;
/**
 * 
 * @author Tomáš Hromada
 *
 */
public interface CreatureSelector {
	/**
	 * Metoda vytvoří novou populaci. V parametru je předán list aktuálních stvoření seřazený podle fitnessu od nejschopnější a po nejméně.
	 * @param creatures poslední generace, seřazená podle fitnessu
	 * @return list nové generace
	 */
	 List<Creature> creaturesToNextGeneration(List<Creature> creatures);

}
 
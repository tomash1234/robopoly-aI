package nn4ai_tools;
/**
 * 
 * @author Tomáš Hromada
 *
 */
public interface FitnessTest {
	/**
	 * Test, který přiřadí zdatnost (fitness) k stvoření
	 * @param creature testované stvoření
	 * @param index index stvoření
	 * @return vrátí float hodnota zdatnosti, čím výšší hodnat tím větší zdatnost
	 */
	float testCreature(Creature creature, int index);

}

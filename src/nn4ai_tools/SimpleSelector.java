package nn4ai_tools;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Tomáš Hromada
 *
 * Implementace CreatureSelectoru
 */
public class SimpleSelector implements CreatureSelector{
	
	private int numberOfNewCreatures;;
	private int direct = 2;
	
	private int mutates[][] = new int[][]{{3, 2, 1}, {3, 2, 2},{2, 2, 2,2,2}};
	private int crossbreed[][]  = new int[][]{{1,2}, {2,3}, {3,5}};
	private float mutateRates[] = new float[]{0.04f, 0.08f, 0.1f};
	private float mutateRange[] = new float[]{0.6f, 0.8f, 1f};
	private float crossbreedRange = 0.5f;
	
	
	private float deadline;

	/**
	 * Vytvoří SimpleSelector
	 * @param numberOfNewCreatures počet nově vytvořeních stvoření
	 * @param deadline minimální hodnota zdatnosti, pokud stvoření bude mín menší bude odstraněno
	 */
	public SimpleSelector(int numberOfNewCreatures, float deadline) {
		// TODO Auto-generated constructor stub
		this.deadline = deadline;
		this.numberOfNewCreatures = numberOfNewCreatures;
	}
	
	/**
	 * Vytvoření selector, který pracuje s populací o velikosti 20
	 * @param deadline minimální hodnota zdatnosti, pokud stvoření bude mín menší bude odstraněno
	 * @return instaci SimpleSelectoru
	 */
	public static SimpleSelector createSimpleSelectorFor20(float deadline){
		SimpleSelector simpleSelector = new SimpleSelector(4, deadline);
		simpleSelector.setDirect(1);							//1
		simpleSelector.setMutates(new int[][]{{1,1,1}, {1,1,1}, {1,1,1,1,1,1}}); //12
		simpleSelector.setCrossbreed(new int[][]{{1,2}, {2,3}, {1,4}});//3
		return simpleSelector;
	}
	/**
	 * Vytvoření selector, který pracuje s populací o velikosti 20
	 * @param deadline minimální hodnota zdatnosti, pokud stvoření bude mín menší bude odstraněno
	 * @param mutation násobek pravděpodonosti zmutování
	 * @return instaci SimpleSelectoru
	 * 
	 */
	public static SimpleSelector createSimpleSelectorFor20(float deadline, float mutation){
		SimpleSelector simpleSelector = new SimpleSelector(4, deadline);
		simpleSelector.setMutateRates(new float[]{0.035f*mutation,0.09f*mutation,0.1f*mutation });
		simpleSelector.setDirect(1);							//1
		simpleSelector.setMutates(new int[][]{{1,1,1}, {1,1,1}, {1,1,1,1,1,1}}); //12
		simpleSelector.setCrossbreed(new int[][]{{1,2}, {2,3}, {1,4}});//3
		return simpleSelector;
	}
	/**
	 * Vytvoření selector, který pracuje s populací o velikosti 40
	 * @param deadline minimální hodnota zdatnosti, pokud stvoření bude mín menší bude odstraněno
	 * @return instaci SimpleSelectoru
	 */
	public static SimpleSelector createSimpleSelectorFor40(float deadline){
		SimpleSelector simpleSelector = new SimpleSelector(10, deadline);
		simpleSelector.setDirect(3);							//3
		simpleSelector.setMutates(new int[][]{{3,2,1}, {3,2,1,1}, {3,2,1,1,1,1}}); //6+7+9
		simpleSelector.setCrossbreed(new int[][]{{1,2}, {2,3}, {1,4}, {1,3}, {2,4}});//5
		return simpleSelector;
	}
	/*public static SimpleSelector createSimpleSelectorFor100(float deadline){
		SimpleSelector simpleSelector = new SimpleSelector(30, deadline);
		simpleSelector.setDirect(5);							//5
		simpleSelector.setMutates(new int[][]{{5,2,2,2,2}, {4,2,2,1,1,1,1,1}, {4,2,2,2,2,1,1,1,1,1,1,1}}); //6+13+7+8+4=38
		simpleSelector.setCrossbreed(new int[][]{{1,2},{1,3}, {2,3}, {1,4}, {2,4}, {1,5}, {2,3}, {1, 6}, {1,7}, {1,8},{2,5},{3,6}});//12
		return simpleSelector;
	}*/
	public int getNumberOfCreaturesInPopulation(){
		int sum = 0;
		for(int[] a : mutates){
			for(int b: a){
				sum+=b;
			}
		}
		sum+=crossbreed.length+numberOfNewCreatures;
		return sum;
	}
	public float getDeadline() {
		return deadline;
	}
	public int getDirect() {
		return direct;
	}
	public int getNumberOfNewCreatures() {
		return numberOfNewCreatures;
	}
	
	public void setDeadline(float deadline) {
		this.deadline = deadline;
	}
	public void setNumberOfNewCreatures(int numberOfNewCreatures) {
		this.numberOfNewCreatures = numberOfNewCreatures;
	}
	/**
	 * Creatures made by crossbreed
	 * @param crossbreed Arrays of pairs[]= new int[2] , pairs[0]=parent1 and pairs[1]= parent2
 	 */
	public void setCrossbreed(int[][] crossbreed) {
		this.crossbreed = crossbreed;
	}
	/**
	 * Number of first creatures which will be moved to next generation withou any changes
	 * @param direct
	 */
	public void setDirect(int direct) {
		this.direct = direct;
	}
	
	/**
	 * Set creatures which will be mutated, mutates[x][y] = n; x= category of mutating; 
	 * y= index of original creature, starts 0; n = number of mutate versions of original creature
	 * @param mutates
	 */
	public void setMutates(int[][] mutates) {
		this.mutates = mutates;
	}
	public void setCrossbreedRange(float crossbreedRange) {
		this.crossbreedRange = crossbreedRange;
	}
	public void setMutateRates(float[] mutateRates) {
		this.mutateRates = mutateRates;
	}
	public void setMutateRange(float[] mutateRange) {
		this.mutateRange = mutateRange;
	}
	
	@Override
	public List<Creature> creaturesToNextGeneration(final List<Creature> creatures) {
		// TODO Auto-generated method stub
		Creature basic = null;
		for(int i = 0;i<creatures.size(); i++){
			if(i==0){
				basic=creatures.get(i);
			}
			Creature c = creatures.get(i);
			if(c.getFitness()<deadline){
				creatures.remove(c);
				i--;
			}
		}
		List<Creature> nCreatures = new ArrayList<>();
		
		//direct to next generation, no mutation
		for(int i =0; i<direct&&i<creatures.size(); i++){
			nCreatures.add(creatures.get(i));
		}
		
		//mutating
		for(int category = 0; category<mutates.length; category++){
			for (int i = 0; i < mutates[category].length&&i<creatures.size(); i++) {
				for (int j = 0; j < mutates[category][j]; j++) {
					Creature c = creatures.get(i).mutate(mutateRates[category], mutateRange[category]);
					nCreatures.add(c);
				}
			}
		}
		//crossbreed
		for (int i = 0; i < crossbreed.length; i++) {
			Creature parent1 = null;
			Creature parent2 = null;
			if(crossbreed[i][0]<creatures.size()){
				parent1 = creatures.get(crossbreed[i][0]);
			}
			if(crossbreed[i][1]<creatures.size()){
				parent2 = creatures.get(crossbreed[i][1]);
			}
			if(parent1!=null&&parent2!=null){
				nCreatures.add(parent1.crossbreed(parent2, crossbreedRange));
			}
		}
		
		//fill population with random creatures
		int total = getNumberOfCreaturesInPopulation();
		while(nCreatures.size()<=total){
			NeuralNetworkAttr networkAttr[] = new NeuralNetworkAttr[basic.getNumberOfNetworks()];
			for(int i = 0; i<networkAttr.length; i++){
				networkAttr[i] = new NeuralNetworkAttr(basic.getNeuralNetworkAttr(i).getSchema(), basic.getNeuralNetworkAttr(i).getMin(), basic.getNeuralNetworkAttr(i).getMax());
			}
			nCreatures.add(new Creature(networkAttr));
		}
		return nCreatures;
	}
	
	

}

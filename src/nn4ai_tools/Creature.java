package nn4ai_tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;
/**
 * @author Tomas Hromada
 *
 */
public class Creature {
	
	private NeuralNetworkAttr[] neuralNetworkAttrs;
	private String name;
	private float fitness;
	private int id;
	
	
	/**
	 * Vytvoří nové svtoření, které obsahuje neuronové sítě předané v konstruktoru
	 * @param networkAttrs defice neuronových sítí
	 */
	public Creature(NeuralNetworkAttr... networkAttrs){
		this.neuralNetworkAttrs = networkAttrs;
		this.name = generateName();
	}

	
	private String generateName(){
		StringBuilder sb = new StringBuilder();
		sb.append("Creature-");
		for(NeuralNetworkAttr networkAttr: neuralNetworkAttrs){
			Float f = networkAttr.getWeightSum();
			sb.append(f.hashCode());
			sb.append("-");
		}
		String out = sb.toString();
		return out.substring(0, out.length()-1);
	}
	public void setFitness(float fitness) {
		this.fitness = fitness;
	}
	public float getFitness() {
		return fitness;
	}
	
	/**
	 * Načte stvoření ze souboru
	 * @param file 
	 * @throws IOException vyhodí vyjímku pokud se nemůže dostat k souboru
	 */
	public Creature(String file) throws IOException{
		load(new File(file));
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	private void load(File file) throws NumberFormatException, IOException {
		File dir = file.getParentFile();
		BufferedReader input =null;
		input = new BufferedReader(new FileReader(file));
		
		try {
			String line = null;
			int n = 0;
			int numberOfNN = 0;
			while ((line = input.readLine()) != null) {
				if (n == 0) {
					this.name = line;
				} else if (n == 1) {
					numberOfNN = Integer.valueOf(line);
					this.neuralNetworkAttrs = new NeuralNetworkAttr[numberOfNN];
				} else {
					this.neuralNetworkAttrs[n - 2] = new NeuralNetworkAttr(dir.getAbsolutePath() + File.separator + line);
				}
				n++;

			}
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	/**
	 * Uloží stvoření do nově vytvořeného souboru
	 * @param dir	cesta k adresáři, kde se vytvoří soubor
	 * @param name	jméno souboru
	 * @param suffix přípona souboru
	 */
	public void saveCreature(String dir, String name, String suffix){
		File file = new File(dir + File.separator + name + "." + suffix);
		String[] paths = new String[neuralNetworkAttrs.length];
		int i = 0;
		for(NeuralNetworkAttr attr: neuralNetworkAttrs){
			paths[i] =  name + "-" + (i+1) + ".nna";
			try{
				attr.save(paths[i], dir);
			}catch(Exception e){
				return;
			}
			
			i++;
		}
		BufferedWriter  output;
		try {
			output = new BufferedWriter(new FileWriter(file));
			try {
				//first line name
				output.write(this.name);
				output.newLine();
				//second line number of nns
				output.write("" + paths.length);
				output.newLine();
				//
				for(String path: paths){
					output.write(path);
					output.newLine();
				}
				
				
			} finally {
				output.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public String getName() {
		return name;
	}
	/**
	 * Vratí počet neunových sítí
	 * @return
	 */
	public int getNumberOfNetworks(){
		return neuralNetworkAttrs.length;
	}
	/**
	 * Vratí neuronovou sítě na určitém indexu
	 * @param index
	 * @return
	 */
	public NeuralNetworkAttr getNeuralNetworkAttr(int index){
		return neuralNetworkAttrs[index];
	}
	
	/**
	 * Randomly changes some weight values. The new value of weight takes value from oldValue-range/2 to oldValue+range/2 and also from 0 included to 1 included. 
	 * @param mutateRate Probability that one weight will mutate 
	 * @param range  range how much value will be changed (0<x<=1) 
	 * @return new Creature with mutated weights 
	 */
	public Creature mutate(float mutateRate, float range){
		NeuralNetworkAttr[] newNeuralNetworkAttrs = new NeuralNetworkAttr[neuralNetworkAttrs.length];
		Random random = new Random();
		for(int i = 0; i<newNeuralNetworkAttrs.length; i++){
			float[] weights = new float[neuralNetworkAttrs[i].getWeights().length];
			for (int j = 0; j < weights.length; j++) {
				float f = this.neuralNetworkAttrs[i].getWeights()[j];
				if(random.nextFloat()<mutateRate){
					f += range*(random.nextFloat()-0.5f)*2;
					f=f<0?0:f>1?1:f;
				}
				weights[j] =  f;
			}
			newNeuralNetworkAttrs[i] = new NeuralNetworkAttr(neuralNetworkAttrs[i].getSchema(), neuralNetworkAttrs[i].getMin(), neuralNetworkAttrs[i].getMax(), weights);
		}
		return new Creature(newNeuralNetworkAttrs);
	}
	
	/**
	 * Randomly crossbreed two creatures. The new value of the weight is from minimal value of the gen - range to  maximal value of the gen
	 * + range.
	 * @param parent another parent of new creature
	 * @param range range of max change 0f to 1f
	 * @return new Creature
	 */
	public Creature crossbreed(Creature parent, float range){
		if(parent.getNumberOfNetworks()!=getNumberOfNetworks()){
			return null;
		}
		
		NeuralNetworkAttr[] newNeuralNetworkAttrs = new NeuralNetworkAttr[neuralNetworkAttrs.length];
		Random random = new Random();
		for(int i = 0; i<newNeuralNetworkAttrs.length; i++){
			float[] weights = new float[neuralNetworkAttrs[i].getWeights().length];
			float[] weights2 = new float[parent.getNeuralNetworkAttr(i).getWeights().length];
			if(weights.length!=weights2.length){
				return null;
			}
			for (int j = 0; j < weights.length; j++) {
				float f = this.neuralNetworkAttrs[i].getWeights()[j];
				float g = parent.getNeuralNetworkAttr(i).getWeights()[j];
				
				float from = Math.min(f, g)-range;
				float to = Math.max(f, g)+range;
				float h = from+ (to-from)*random.nextFloat();
				h=h>1?1:h<0?0:h;
				weights[j] =  h;
			}
			newNeuralNetworkAttrs[i] = new NeuralNetworkAttr(neuralNetworkAttrs[i].getSchema(), neuralNetworkAttrs[i].getMin(), neuralNetworkAttrs[i].getMax(), weights);
		}
		
		return new Creature(newNeuralNetworkAttrs);	
	}


}

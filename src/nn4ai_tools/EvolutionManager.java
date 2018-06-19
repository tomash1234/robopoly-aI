package nn4ai_tools;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * @author Tomáš Hromada
 *
 */
public class EvolutionManager {
	
	private List<Creature> creatures = new ArrayList<>();
	private int generation = 0;
	private FitnessTest fitnessTest;
	private CreatureSelector creatureChooser;
	private LearningInfo learningInfo;
	private AtomicBoolean go = new AtomicBoolean();
	
	/**
	 * Vytvoří EvolutionManager.
	 * @param fitnessTest test, který zjistí schopnost stvoření
	 * @param creatureChooser rozhraní, které vytvoří ze staré populace populaci novou
	 */
	public EvolutionManager(FitnessTest fitnessTest, CreatureSelector creatureChooser) {
		this.fitnessTest = fitnessTest;
		this.creatureChooser = creatureChooser;
	}
	/**
	 * Appends all of the creatures in the creature population
	 * @param creatures list of creatures
	 */
	public void addAllCreatures(List<Creature> creatures){
		this.creatures.addAll(creatures);
		
	}
	/**
	 * Appends creature to the population
	 * @param creature to add
	 */
	public void addCreature(Creature creature){
		creatures.add(creature);
	}
	
	public void setCreatureSelector(CreatureSelector creatureChooser) {
		this.creatureChooser = creatureChooser;
	}

	/**
	 * Removes the creature by its index
	 * The index can be got with {@link #getIndexOfCreature(Creature creature) getIndexOfCreature()} 
	 * @param index index of creature in population
	 */
	public void removeCreature(int index){
		creatures.remove(index);
	}
	public int getIndexOfCreature(Creature c){
		return creatures.indexOf(c);
	}
	/**
	 * Returns size of population
	 * @return size of population
	 */
	public int getPopulationSize(){
		return creatures.size();
	}
	/**
	 * Returns creature on specific position int the population
	 * The index can be got with {@link #getIndexOfCreature(Creature creature) getIndexOfCreature()} 
	 * @param index
	 * @return specific creature 
	 */
	public Creature getCreature(int index){
		return creatures.get(index);
	}
	/**
	 * Returns new ArrayList, which contains creatures from the population 
	 * @return new List with creatures
	 */
	public List<Creature> getCreaturesPublic(){
		return new ArrayList<Creature>(creatures);
	}
	/**
	 * Creates new generation
	 */
	public void nextGeneration(){
		aplyfitnessTestToAllCreatures();
		sort(false);
		generation++;
	}
	public int getGeneration() {
		return generation;
	}
	/**
	 * Začne proces evoluce, který bude fungovat po určitý počet generací. Stvoření nejsou průběžně ukládány do souborů.
	 * @param generations počet generací, které budou vytvořeny
	 */
	public void startEvolving(int generations){
		go.set(true);
		for(int i = 0; i<generations&&go.get(); i++){
			nextGeneration();
			if(learningInfo!=null){
				learningInfo.nextGenerationCreated(new ArrayList<>(creatures), i, generations);
			}
			if(i!=generations-1){
				creatures = creatureChooser.creaturesToNextGeneration(creatures);
			}
		}
	}
	
	public void setLearningInfo(LearningInfo learningInfo) {
		this.learningInfo = learningInfo;
	}
	/**
	 * Začne proces evoluce, který bude fungovat po určitý počet generací. Každá generace se bude průběžně ukládat do určené složky
	 * @param generations počet generací, které budou vytvořeny
	 * @param dirToSave adresář k průběžnému ukládání
	 * @param frequencyOfSaving frekvence ukládání stvoření, nelze být 0 , 1- každá, 2- každá druhá
	 * @param fileSuffix	přípona souborů
	 */
	public void startEvolvingAutoSave(int generations, String dirToSave, int frequencyOfSaving, String fileSuffix){
		go.set(true);
		for(int i = 0; i<generations&&go.get(); i++){
			nextGeneration();
			if((i+1)%frequencyOfSaving==0){
				saveOrUpdateAllCreatures(dirToSave, fileSuffix);
			}
			if(learningInfo!=null){
				learningInfo.nextGenerationCreated(new ArrayList<>(creatures), i, generations);
			}
			if(i!=generations-1){
				creatures = creatureChooser.creaturesToNextGeneration(creatures);
			}
		}
		saveOrUpdateAllCreatures(dirToSave, fileSuffix);
		
	}
	/**
	 * Sorts creatures by its fitness
	 * @param asc true asc, false des
	 */
	public void sort(boolean asc){
		creatures.sort(new Comparator<Creature>() {

			@Override
			public int compare(Creature o1, Creature o2) {
				// TODO Auto-generated method stub
				return asc?Float.compare(o1.getFitness(), o2.getFitness()):Float.compare(o2.getFitness(), o1.getFitness());
			}
		});
	}
	
	
	private void aplyfitnessTestToAllCreatures(){
		int i = 0;
		for(Creature creature:creatures){
			creature.setFitness(0);
			creature.setFitness(fitnessTest.testCreature(creature, i));
			if(learningInfo!=null){
				learningInfo.creatureTested(i, creatures.size());
			}
			i++;
			
		}
	}
	/**
	 * Otestuje určité stvoření
	 * @param index index stvoření
	 */
	public void testSpecificCreature(int index){
		Creature creature = creatures.get(index);
		creature.setFitness(0);
		creature.setFitness(fitnessTest.testCreature(creature, index));
	}
	
	/**
	 * Deletes directory than makes the dir again and saves creatures 
	 * @param dir 
	 */
	public void saveOrUpdateAllCreatures(String dir, String suf){
		File fDir = new File(dir);
		for(File f:fDir.listFiles()){
			if(f.isDirectory()){
				continue;
			}
			if(f.getName().contains("."+suf)||f.getName().contains(".nna")){
				f.delete();
			}
		}
		/*if(fDir.exists()){
			Path fp = fDir.toPath();
			try {
				Files.delete(fp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}*/
		fDir.mkdirs();
		
		for(Creature creature: creatures){
			creature.saveCreature(dir, creature.getName(), suf);
		}
	}
	
	/**
	 * Return list of creatures loaded from files located in given directory
	 * @param dir directory containing creatures files
	 * @param suffix suffix of creature
	 * @return lsit of creatures
	 */
	public static List<Creature> loadAllCreaturesFromDir(String dir, String suffix){
		List<Creature> ll = new ArrayList<>();
		File fDir = new File(dir);
		if(!fDir.isDirectory()){
			return ll;
		}
		
		for(File f: fDir.listFiles()){
			String[] as = f.getName().split("\\.");
			if(!as[as.length-1].equals(suffix)){
				continue;
			}
			try{
				Creature c = new Creature(f.getAbsolutePath());
				ll.add(c);
			}catch(IOException exception){
				exception.printStackTrace();
			}
		}
		return ll;
	}
	
	public void stop(){
		go.set(false);
	}
	
	/*public static void saveNeuralNetwrokBinarFile(String file,NeuralNetworkAttr networkAttr){
        DataOutputStream out =null;
		File output = new File(file);
		int layers[] = networkAttr.getSchema();
		float minW = networkAttr.getMin();
		float maxW = networkAttr.getMax();
		float[] data = networkAttr.getWeights();
		
        try{
            out = new DataOutputStream(new FileOutputStream(output));
            //writes how many floats are for data 
            out.writeFloat(layers.length + 2);
            for(int i = 0; i<layers.length; i++){
            	out.writeFloat(layers[i]); //writes schema
            }
            //writes min and max weight
            out.writeFloat(minW);
            out.writeFloat(maxW);
            //writes weights
            for(float a:data){
            	out.writeFloat(a);
            }
        }catch(IOException e){
        	System.err.println(e.getMessage());
        }finally{
            if(out!=null){
                try {
                    out.close();
                } catch (IOException ex) {
                	System.err.println(ex.getMessage());
                }
	}
        }
    }*/
	
	
	
	
	/*/**
	 * Loads all files from dir
	 * @param dir path to dir with files
	 * @return List of creatures loaded from dir
	 */
	/*public static List<NeuralNetworkAttr> loadaNFromDir(String dir){
		File f = new File(dir);
		String[] files =  f.list();
		List<NeuralNetworkAttr> cr = new ArrayList<>();
		for(String file: files){
			NeuralNetworkAttr nn = loadNNAttr(dir + "/" + file);
			if(nn!=null){
				cr.add(nn);
			}
		}
		return cr;
		
	}*/
	/*/**
	 * Loads genes from file
	 * @param file path to file
	 * @return creature with genes from file - null if loading failed
	 */
	/*public static NeuralNetworkAttr  loadNNAttr(String file){
		DataInputStream in = null;
		try{
			in = new DataInputStream(new FileInputStream(file));
			List<Float> data = new ArrayList<>();
			float numberOfDatas = in.readFloat();
			int[] schema = new int[(int)(numberOfDatas-2)];
			for(int i = 0; i<numberOfDatas-2; i++){
				schema[i] = (int)in.readFloat();
			}
			float minW = in.readFloat();
			float maxW = in.readFloat();
			
			while(in.available() > 0){
				float x = in.readFloat();
				data.add(x);
			}
			float[] genes=  new float[data.size()];
			for (int i = 0; i < genes.length; i++) {
				genes[i] = data.get(i);
			}
			NeuralNetworkAttr neuralNetworkAttr = new NeuralNetworkAttr(schema, minW, maxW, genes);
			/*c= new Creature(genes);
			creatures.add(c);*/
	/*		return neuralNetworkAttr;
		}catch(IOException e){
			System.err.println(e.getMessage());
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.err.println(e.getMessage());
				}
			}
		}
		return null;
	}
*/
	public NeuralNetworkAttr getNeuralNetworkAttr(int creatureID, int nnId){
		return creatures.get(creatureID).getNeuralNetworkAttr(nnId);//new NeuralNetworkAttr(nnLayers, weightMin, weightMax, creatures.get(creatureID).getWeights(nnId));
	}
	public void clearAll() {
		creatures.clear();
	}

}

package nn4ai_tools;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * Třída nese informace o neuronové síti
 * @author Tomáš Hromada
 *
 */
public class NeuralNetworkAttr {

	private int[] schema;
	private float min, max;
	private float[] weights;
	
	
	
	
	public NeuralNetworkAttr(int[] schema, float min, float max, float[] weights) {
		super();
		this.schema = schema;
		this.min = min;
		this.max = max;
		this.weights = weights;
	}
	
	public NeuralNetworkAttr(int[] schema, float min, float max) {
		super();
		this.schema = schema;
		this.min = min;
		this.max = max;
		int n = getNumberOfWeights();
		Random random = new Random();
		weights = new float[n];
		for(int i = 0; i<n; i++){
			weights[i] = random.nextFloat();
		}
	}
	
	public NeuralNetworkAttr(String file){
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
			
			this.schema = schema;
			this.min = minW;
			this.max = maxW;
			this.weights = genes;
		}catch(IOException e){
			e.printStackTrace();
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
	}
	
	public float getWeightSum(){
		float f = 0;
		for(float g: weights){
			f+=g;
		}
		return f;
	}
	
	public float getMax() {
		return max;
	}
	public float getMin() {
		return min;
	}
	public int[] getSchema() {
		return schema;
	}
	public float[] getWeights() {
		return weights;
	}
	public int getNumberOfWeights(){
		int number = 0;
		for(int i = 0; i<schema.length-1; i++){
			number+= schema[i]*schema[i+1];
		}
		
		return number;
	}

	public void setWeights(float[] r) {
		this.weights = r;
		
	}

	/**
	 * Vytvoří soubor reprezentují neuronovou síť.
	 * @param file název souboru
	 * @param dir adresář, ve kterém se vytvoří soubor
	 * @throws IOException pokud se nepodaří vytvořit soubor
	 */
	public void save(String file, String dir) throws IOException {
		DataOutputStream out = null;
		File output = new File(dir+ File.separator + file);
		int layers[] = getSchema();
		float minW = getMin();
		float maxW = getMax();
		float[] data = getWeights();

		try {
			out = new DataOutputStream(new FileOutputStream(output));
			// writes how many floats are for data
			out.writeFloat(layers.length + 2);
			for (int i = 0; i < layers.length; i++) {
				out.writeFloat(layers[i]); // writes schema
			}
			// writes min and max weight
			out.writeFloat(minW);
			out.writeFloat(maxW);
			// writes weights
			for (float a : data) {
				out.writeFloat(a);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException ex) {
					System.err.println(ex.getMessage());
				}
			}
		}
	}
	
	
}

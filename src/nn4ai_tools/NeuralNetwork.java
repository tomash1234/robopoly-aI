package nn4ai_tools;

import java.util.Random;
/**
 * 
 * @author Tomáš Hromada
 *
 */
public class NeuralNetwork {
	
	
	private float[][] values; // layer, index
	private float[][][] weights; //layer, index, indexNext
	private float weightMin;
	private float weightMax;
	
	/**
	 * Creates neural network with random weights
	 * @param layers array with integer representing number of neurons in each layer
	 * @param weightMin the lowest possible value of weight
	 * @param weightMax the highest possible value of weight
	 */
	public NeuralNetwork(int[] layers, float weightMin, float weightMax){
		this.weightMin = weightMin;
		this.weightMax = weightMax;
		init(layers, null);
		
	}
	/**
	 * Creates neural network with weights from param
	 * @param layers array with integer representing number of neurons in each layer
	 * @param weights  weights from 0 to 1 
	 * @param weightMin the lowest possible value of weight
	 * @param weightMax the highest possible value of weight
	 */
	public NeuralNetwork(int[] layers, float[] weights, float weightMin, float weightMax){
		this.weightMin = weightMin;
		this.weightMax = weightMax;
		init(layers, weights);
		
	}
	/**
	 * Creates neural network from NeuralNetworkAttr
	 * @param neuralNetworkAttr
	 */
	public NeuralNetwork(NeuralNetworkAttr neuralNetworkAttr) {
		// TODO Auto-generated constructor stub
		this.weightMin = neuralNetworkAttr.getMin();
		this.weightMax = neuralNetworkAttr.getMax();
		init(neuralNetworkAttr.getSchema(), neuralNetworkAttr.getWeights());
	}
	public int getNumberOfLayers(){
		return weights.length;
	}
	public int getNumberOfNeuronsInLayer(int layer){
		return weights[layer].length;
	}
	public int getNumberOfNeuronsInNextLayer(int layer, int neuron){
		return weights[layer][neuron].length;
	}
	/**
	 * 
	 * @param layers
	 * @param data
	 */
	private void init(int[] layers,  float[] data){
		Random random = new Random();
		int index = 0;
		values = new float[layers.length][];
		for(int i = 0; i<layers.length; i++){
			values[i] = new float[layers[i]];
		}
		weights = new float[layers.length][][];
		for(int i = 0; i<layers.length; i++){
			weights[i] = new float[layers[i]][];
			for(int j = 0; j<weights[i].length; j++){
				if(i!=layers.length-1){
					weights[i][j] = new float[layers[i+1]];
					for (int k = 0; k < weights[i][j].length; k++) {
						if(data!=null){
							weights[i][j][k] = weightMin + data[index]*(weightMax-weightMin);
							index++;
						}else{
							weights[i][j][k]  = weightMin + (random.nextFloat())*(weightMax-weightMin);
						}
					}
				}else{
					weights[i][j] = new float[0];
				}
			}
		}
	}
	/**
	 * Sets weights, weight must already calculated
	 * @param data
	 */
	public void setWeights(float[] data){
		int index = 0;
		for(int i = 0; i<weights.length; i++){
			for (int j = 0; j < weights[i].length; j++) {
				for (int k = 0; k < weights[i][j].length; k++) {
					weights[i][j][k] = data[index];
					index++;
				}
			}
		}
	}
	
	public float getWeightMax() {
		return weightMax;
	}
	public float getWeightMin() {
		return weightMin;
	}
	public int getNumberOfWeights(){
		int count = 0;
		for(int i = 0; i<weights.length; i++){
			for (int j = 0; j < weights[i].length; j++) {
				count+= weights[i][j].length;
			}
		}
		return count;
	}
	public float[]  getWeights(){
		float[] ret = new float[getNumberOfWeights()];
		int index = 0;
		for(int i = 0; i<weights.length; i++){
			for (int j = 0; j < weights[i].length; j++) {
				for (int k = 0; k < weights[i][j].length; k++) {
					ret[index] = weights[i][j][k];
					index++;
				}
			}
		}
		return ret;
	}
	/**
	 * Resolves value in output neurons for specific input
	 * @param input 
	 * @return output
	 */
	public float[] resolve(float[] input){
		if(input.length!=values[0].length){
			return null;
		}
		//sets inputs
		for(int i =0; i<input.length; i++){
			values[0][i] = input[i];
		}
		for(int i = 1; i<values.length; i++){
			//layer
			for(int j = 0; j<values[i].length; j++){
				//neuron
				float sum = 0;
				for(int k = 0; k<values[i-1].length; k++){
					sum += values[i-1][k]*weights[i-1][k][j];
				}
				values[i][j] = sigmoid2(sum);

			}
		}
		
		return values[values.length-1];
	}
	
	private float sigmoid(float x){
		float f =  (float) (1+ Math.pow(Math.E, -x)); 
		return 2.f/f-1;
	}
	private float sigmoid2(float x){
		
		float f =  (float) (1+ Math.pow(Math.E, -x)); 
		return 1.f/f;
	}

}

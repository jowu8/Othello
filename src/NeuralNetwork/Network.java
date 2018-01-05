/**
 * @authors Stephen C. & Jon Wu
 */
package NeuralNetwork;

/**
 * the neural network (NN) used to calculate the value of a specific gamestate
 *
 */
public class Network {

	final int InputNeuronCount=64;
	final int HiddenNeuronCount=42;
	final int HiddenLayerCount=1;



	Node[] InputLayer;
	Node[][] HiddenLayers;
	Node OutNode;

	Connection[] Connections;



	/**
	 * @author Jon Wu & Stephen Chern
	 * 
	 * creates the neural network and all the connections between input nodes to hidden nodes, and hidden nodes to output nodes
	 */
	public Network() {
		InputLayer=new Node[InputNeuronCount];

		//Input -> Hidden
		for(int i=0;i<InputNeuronCount;i++) {
			InputLayer[i]=new Node(HiddenNeuronCount);
			//InputLayer[i].x=100;
			//InputLayer[i].y=50+i*900/InputNeuronCount;
		}

		HiddenLayers=new Node[HiddenLayerCount][HiddenNeuronCount];

		//Hidden -> Hidden
		for(int k=0;k<HiddenLayerCount-1;k++) {
			for(int i=0;i<HiddenNeuronCount;i++) {
				HiddenLayers[k][i]=new Node(HiddenNeuronCount);
				//HiddenLayers[k][i].x=700;
				//HiddenLayers[k][i].y=100+i*800/HiddenNeuronCount;
			}
		}

		//Last Hidden -> Output
		for(int i=0;i<HiddenNeuronCount;i++) {
			HiddenLayers[HiddenLayerCount-1][i]=new Node(1);
			//HiddenLayers[HiddenLayerCount-1][i].x=950;
			//HiddenLayers[HiddenLayerCount-1][i].y=100+i*800/HiddenNeuronCount;
		}


		OutNode=new Node(0);
		//OutNode.x=1400;
		//OutNode.y=500;


		//Neurons=new Node[InputNeuronCount+HiddenNeuronCount*HiddenLayerCount+1];
		Connections=new Connection[InputNeuronCount*HiddenNeuronCount+HiddenNeuronCount*HiddenNeuronCount*(HiddenLayerCount-1)+HiddenNeuronCount];
		int count=0;
		for(int i=0;i<InputNeuronCount;i++) {
			for(int k=0;k<HiddenNeuronCount;k++) {
				Connection connection=InputLayer[i].addConnection(HiddenLayers[0][k]);
				Connections[count++]=connection;
			//	System.out.println("ADDING CONNECTION "+connection.getID());
			}
		}
		for(int i=0;i<HiddenLayerCount-1;i++) {
			for(int k=0;k<HiddenNeuronCount;k++) {
				for(int j=0;j<HiddenNeuronCount;j++) {
					Connection connection=HiddenLayers[i][k].addConnection(HiddenLayers[i+1][j]);
					Connections[count++]=connection;
			//		System.out.println("ADDING CONNECTION "+connection.getID());
				}
			}
		}
		for(int i=0;i<HiddenNeuronCount;i++) {
			Connection connection=HiddenLayers[HiddenLayerCount-1][i].addConnection(OutNode);
			Connections[count++]=connection;
			//System.out.println("ADDING CONNECTION "+connection.getID());
		}
	}

	/**
	 * sets the weights for all connections in the NN
	 */
	public void train(String[] weights) {
		for(int i=0;i<Connections.length;i++) {
			Connections[i].setWeight(Double.parseDouble(weights[i]));
		}
	}

	/**
	 * returns the final value of a specific gamestate
	 */
	public double calculate(int[] Inputs) {

		//Input -> Hidden
		for(int i=0;i<InputNeuronCount;i++) {
			InputLayer[i].clear();
		}

		//Hidden -> Hidden
		for(int i=0;i<HiddenLayerCount;i++) {
			for(int k=0;k<HiddenNeuronCount;k++) {
				HiddenLayers[i][k].clear();
			}
		}

		OutNode.clear();
		
		for(int i=0;i<InputNeuronCount;i++) {
			InputLayer[i].add(Inputs[i]);
		}
		
		//Input -> Hidden
		for(int i=0;i<InputNeuronCount;i++) {
			InputLayer[i].propagate();
		}

		//Hidden -> Hidden
		for(int i=0;i<HiddenLayerCount;i++) {
			for(int k=0;k<HiddenNeuronCount;k++) {
				HiddenLayers[i][k].propagate();
			}
		}
		

		return OutNode.getValue();
	}

}

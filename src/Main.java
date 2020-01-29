import Network.NeuralNetwork;

public class Main
{
	public static void main(String[] args)
	{
		double[] targets = new double[] {0.01, 0.99};
		NeuralNetwork test = new NeuralNetwork(new int[]{2,3,2}, 2);
		test.getOutput(new double[] {0.05, 0.1});

		test.calculateDeltas(targets);

		double learningRate = 0.5;
	}
}
import Network.Backpropagation;
import Network.NeuralNetwork;
import Utils.Parser;

import java.io.IOException;
import static java.lang.System.currentTimeMillis;


public class Main
{
	public static void main(String[] args) throws IOException
	{
		double[][] ints = Parser.getInputsArray("src/train.csv", 42001, 785);
		double[][] labels = Parser.getLabelsArray("src/train.csv", 42001);
		int[] HiddenLayers = new int[] {16, 16, 16, 16};

		System.out.println("Started training");
		long started = currentTimeMillis();
		NeuralNetwork trained = Backpropagation.getTrainedNetwork(ints, labels, HiddenLayers, true);
		trained.save("recognizer.nn");

		System.out.println("DONE in " + (currentTimeMillis() - started) + " ms");
	}
}
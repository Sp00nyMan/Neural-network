import Network.NeuralNetwork;
import Network.Node;

public class Backpropogation
{
	private static final double learningRate = 0.7;
	private static int maxEpochs = 100000;
	private static double minAccuracy = 0.00001;

	public static void setMaxEpochs(int newEpochs)
	{
		maxEpochs = newEpochs;
	}

	public static NeuralNetwork getTrainedNetwork(double[][] inputs, double[][] targetOutputs, int[] hiddenLayersInfo, boolean printInfo)
	{
		if (inputs.length != targetOutputs.length)
			throw new IllegalArgumentException("Length of the input set must be the same as outputs's");
		if (heterogeneous(inputs))
			throw new IllegalArgumentException("All inputs must be the same length");
		if (heterogeneous(targetOutputs))
			throw new IllegalArgumentException("All outputs must be the same length");

		int[] temp = new int[hiddenLayersInfo.length + 1];
		for (int i = 0; i < hiddenLayersInfo.length; i++)
			temp[i] = hiddenLayersInfo[i];
		temp[hiddenLayersInfo.length] = targetOutputs[0].length;

		NeuralNetwork ourBigBoy = new NeuralNetwork(temp, inputs[0].length);

		for (int iterator = 1; iterator <= maxEpochs; iterator++)
		{
			ourBigBoy.resetTotalError();
			for (int i = 0; i < inputs.length; i++)
			{
				ourBigBoy.getOutput(inputs[i]); //Get output for current input set
				ourBigBoy.calculateTotalError(targetOutputs[i]); //Add error for current output to the total error of network

				ourBigBoy.calculateDeltas(targetOutputs[i]); //Calculate deltas for each node
				adjustWeights(inputs[i], ourBigBoy); //Проблема в том, что будут изменяться веса, а дельты должны для всех датасетов считаться на одних и тех же весах
			}
			System.out.println("Error of " + iterator + " epoch is " + ourBigBoy.getTotalError());
			if (ourBigBoy.getTotalError() <= minAccuracy)
				break;
		}

		return ourBigBoy;
	}

	private static void adjustWeights(double[] input, NeuralNetwork ourBigBoy) //Deltas must be already calculated for this input set
	{
		for (int i = ourBigBoy.body.length - 1; i > 0; --i) //Цикл по слоям, начиная с последнего и до первого
		{
			for (Node node : ourBigBoy.body[i]) //Цикл по нейронам слоя
			{
				double[] weights = node.getWeights();
				for (int k = 0; k < weights.length; k++) //Цикл по весам нейрона
				{
					double affection = node.getDelta() * ourBigBoy.body[i - 1][k].getOutput() * learningRate;
					weights[k] -= affection;
				}
			}
		}

		for (Node node : ourBigBoy.body[0]) //Цикл по первому слою (не входному, его вообще не существует)
		{
			double[] weights = node.getWeights();
			for (int i = 0; i < weights.length; i++)
			{
				double affection = node.getDelta() * input[i] * learningRate;
				weights[i] -= affection;
			}
		}
	}

	private static boolean heterogeneous(double[][] array)
	{
		for (int i = 1; i < array.length; i++)
			if (array[i].length != array[i - 1].length)
				return true;
		return false;
	}
}

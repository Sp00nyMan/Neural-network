package Network;

public class Backpropagation
{
	public static final double learningRate = 1.05;
	private static int maxEpochs = 100000;
	private static double minAccuracy = 0.001;

	public static NeuralNetwork getTrainedNetwork(double[][] inputs, double[][] targetOutputs, int[] hiddenLayersInfo, boolean printInfo)
	{
		if (inputs.length != targetOutputs.length)
			throw new IllegalArgumentException("Length of the input set must be the same as outputs's");
		if (heterogeneous(inputs))
			throw new IllegalArgumentException("All inputs must be the same length");
		if (heterogeneous(targetOutputs))
			throw new IllegalArgumentException("All outputs must be the same length");

		int[] temp = new int[hiddenLayersInfo.length + 1];
		System.arraycopy(hiddenLayersInfo, 0, temp, 0, hiddenLayersInfo.length);
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
				adjustAffections(inputs[i], ourBigBoy);
			}
			ourBigBoy.adjustWeights();
			ourBigBoy.resetAffections();
			if(printInfo)
			{
				System.out.println("Error of " + iterator + " epoch is " + ourBigBoy.getTotalError());
			}
			if (ourBigBoy.getTotalError() <= minAccuracy)
			{
				if(!printInfo)
					System.out.println(iterator);
				break;
			}
		}

		return ourBigBoy;
	}

	private static void adjustAffections(double[] input, NeuralNetwork ourBigBoy) //Deltas must be already calculated for this input set
	{
		for (int i = ourBigBoy.body.length - 1; i > 0; --i) //Цикл по слоям, начиная с последнего и до первого
		{
			for (Node node : ourBigBoy.body[i]) //Цикл по нейронам слоя
			{
				double[] affections = node.getAffections();
				for (int k = 0; k < affections.length; k++) //Цикл по весам нейрона
				{
					affections[k] += node.getDelta() * ourBigBoy.body[i - 1][k].getOutput() * learningRate;
				}
			}
		}

		for (Node node : ourBigBoy.body[0]) //Цикл по первому слою (не входному, его вообще не существует)
		{
			double[] affections = node.getAffections();
			for (int i = 0; i < affections.length; i++)
			{
				affections[i] += node.getDelta() * input[i] * learningRate;
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

package Network;

import org.jetbrains.annotations.NotNull;

public class NeuralNetwork implements Cloneable
{
	public Node[][] body;
	private double[] output = null;

	double totalError;

	public NeuralNetwork clone()
	{
		try
		{
			super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		return new NeuralNetwork(this);
	}

	public NeuralNetwork(int[] hiddenLayersInfo, int inputsCount)
	{
		body = new Node[hiddenLayersInfo.length][];

		for (int i = 0; i < hiddenLayersInfo.length; i++)
		{
			body[i] = new Node[hiddenLayersInfo[i]];
			for (int j = 0; j < hiddenLayersInfo[i]; j++)
			{
				body[i][j] = new Node(inputsCount);
			}
			inputsCount = hiddenLayersInfo[i];
		}
	}

	NeuralNetwork(@NotNull final NeuralNetwork other)
	{
		body = new Node[other.body.length][];

		for (int i = 0; i < other.body.length; i++)
		{
			body[i] = new Node[other.body[i].length];

			for (int j = 0; j < other.body[i].length; j++)
				body[i][j] = new Node(other.body[i][j]);
		}

		output = other.output.clone();
	}

	public double[] getOutput(double[] input)
	{
		for (Node[] layer : body)
		{
			output = new double[layer.length];
			for (int i = 0; i < layer.length; i++)
			{
				try
				{
					output[i] = layer[i].activation(input);
				}
				catch (final IllegalArgumentException e)
				{
					System.out.println(e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
			input = output.clone();
		}

		return output;
	}

	double calculateTotalError(final double[] idealOutput)
	{
		if (output.length != idealOutput.length)
			throw new IllegalArgumentException("length of idealOutput array have to be the same as neural network's output layer length");

		for (int i = 0; i < output.length; i++)
		{
			totalError += 0.5 * Math.pow((idealOutput[i] - output[i]), 2);
		}

		return totalError;
	}

	public void calculateDeltas(double[] idealOutput)
	{
		calcOutputLayerDeltas(idealOutput);

		for (int i = body.length - 2; i >= 0; --i) //Цикл по слоям от предпоследнего к первому
		{
			for (int k = 0; k < body[i].length; k++) //Цикл по нейронам i-го слоя
			{
				double delta = 0;
				for (int j = 0; j < body[i + 1].length; j++) //Цикл по нейронам i+1-го слоя
				{
					delta += body[i + 1][j].getDelta() * body[i + 1][j].getWeights()[k];
				}
				delta *= body[i][k].getOutput() * (1 - body[i][k].getOutput());
				body[i][k].setDelta(delta);
			}
		}
	}

	private void calcOutputLayerDeltas(double[] targetOutput)
	{
		Node[] outputLayer = body[body.length - 1];

		for (int i = 0; i < outputLayer.length; i++)
		{
			double output = outputLayer[i].getOutput();
			double delta = (output - targetOutput[i]) * (1 - output) * output;
			outputLayer[i].setDelta(delta);
		}
	}
}

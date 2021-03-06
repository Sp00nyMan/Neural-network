package Network;

import Utils.Parser;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class NeuralNetwork implements Cloneable
{
	public Node[][] body;
	private double[] output = null;

	double totalError;

	public double getTotalError()
	{
		return totalError;
	}
	public void resetTotalError()
	{
		totalError = 0;
	}

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
		totalError = other.totalError;

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

	public void calculateTotalError(final double[] idealOutput)
	{
		if (output.length != idealOutput.length)
			throw new IllegalArgumentException("length of idealOutput array have to be the same as neural network's output layer length");

		for (int i = 0; i < output.length; i++)
		{
			totalError += 0.5 * Math.pow((idealOutput[i] - output[i]), 2);
		}

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

		if(targetOutput.length != outputLayer.length)
			throw new IllegalArgumentException();

		for (int i = 0; i < outputLayer.length; i++)
		{
			double output = outputLayer[i].getOutput();
			//TODO
			double delta = (output - targetOutput[i]) * (1 - output) * output; //Если output = 0, то delta всегда 0, вне зависимости от Target
			outputLayer[i].setDelta(delta);
		}
	}

	public void adjustWeights()
	{
		for (Node[] layer : body)
			for (Node node : layer)
			{
				double[] affections = node.getAffections();
				double[] weights = node.getWeights();
				for (int i = 0; i < weights.length; i++)
					weights[i] -= affections[i];
				node.increaseBias(-node.getDelta() * Backpropagation.learningRate);
			}
	}
	public void resetAffections()
	{
		for (Node[] layer : body)
			for (Node node : layer)
			{
				Arrays.fill(node.getAffections(), 0);
			}
	}

	public void save(String fileName) throws IOException
	{
		FileWriter file = new FileWriter(fileName);

		for (Node[] nodes : body)
		{
			file.write("," + nodes.length); //Write layer size
		}
		file.write(",\r\n");
		for (Node[] nodes : body)
		{
			for (Node node : nodes)
			{
				double[] weights = node.getWeights();
				for (double weight : weights)
				{
					file.write(weight + ",");
				}
				file.write(node.getBias() + ",\r\n");
			}
			file.write("\r\n");
		}
		file.close();
	}

	public void load(String fileName) throws IOException
	{
		BufferedReader file = new BufferedReader(new FileReader(fileName));

		ArrayList<Integer> layersInfo = Parser.getIntArray(file.readLine());

		body = new Node[layersInfo.size()][];

		for (int i = 0; i < layersInfo.size(); i++)
		{
			body[i] = new Node[layersInfo.get(i)];
			ArrayList<Double> weightsNbias;
			for (int j = 0; j < layersInfo.get(i); j++)
			{
				weightsNbias = Parser.getDoubleArray(file.readLine());
				body[i][j] = new Node(weightsNbias.subList(0, weightsNbias.size() - 1), weightsNbias.get(weightsNbias.size() - 1));
			}
			file.readLine(); //read "\r\n" between layers in file
		}

	}
}

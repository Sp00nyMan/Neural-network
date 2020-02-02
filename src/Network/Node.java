package Network;

import Utils.Randomizer;

import java.util.List;

public class Node
{
	private double[] weights;
	private double[] affections;
	private double bias;
	private double delta;
	private double output;

	public double[] getWeights()
	{
		return weights;
	}
	public double[] getAffections()
	{
		return affections;
	}

	public double getDelta()
	{
		return delta;
	}

	public void setDelta(double delta)
	{
		this.delta = delta;
	}


	public double getOutput()
	{
		return output;
	}

	private static double sigmoid(double rawInput)
	{
		return 1/(1 + Math.exp(-rawInput));
	}

	public Node(int inputsCount)
	{
		weights = new double[inputsCount];
		affections = new double[inputsCount];

		for (int i = 0; i < weights.length; i++)
		{
			weights[i] = Randomizer.getDouble(-1, 1);
		}

		bias = Randomizer.getDouble(-1,1);
	}

	public Node(final Node other)
	{
		weights = other.weights.clone();
		affections = other.affections.clone();
		output = other.output;
		bias = other.bias;
		delta = other.delta;
	}

	public Node(List<Double> weights, double bias)
	{
		this.weights = new double[weights.size()];
		for (int i = 0; i < weights.size(); i++)
			this.weights[i] = weights.get(i);

		this.bias = bias;
	}

	public double activation(final double[] inputs) throws IllegalArgumentException
	{
		if(inputs.length != weights.length)
			throw new IllegalArgumentException("Length of inputs array must be the same as weights array's");
		output = 0;
		for (int i = 0; i < inputs.length; i++)
		{
			output += inputs[i] * weights[i];
		}
		output += bias;

		return output = sigmoid(output + bias);
	}

	double getBias()
	{
		return bias;
	}
}

import Network.NeuralNetwork;

import static java.lang.System.currentTimeMillis;


public class Main
{
	public static void main(String[] args)
	{
		double[][] targets = new double[][] {
												new double[]{0},
												new double[]{1},
												new double[]{1},
											};
		double[][] inputs = new double[][] {
												new double[]{0, 0},
												new double[]{0, 1},
												new double[]{1, 0},
										};
		int[] hidden = new int[] {3, 2};

		long started = currentTimeMillis();
		NeuralNetwork test = Backpropogation.getTrainedNetwork(inputs, targets, hidden, true);

		System.out.println("DONE");
		System.out.println("Time spent: " + (currentTimeMillis() - started) + " ms");
		System.out.println("-----------------------------------");
		for (double v : test.getOutput(new double[] {0, 0}))
		{
			System.out.println(v);
		}
		System.out.println("-----------------------------------");
		for (double v : test.getOutput(new double[] {0, 1}))
		{
			System.out.println(v);
		}
		System.out.println("-----------------------------------");
		for (double v : test.getOutput(new double[] {1, 0}))
		{
			System.out.println(v);
		}
		System.out.println("-----------------------------------");
		for (double v : test.getOutput(new double[] {1, 1}))
		{
			System.out.println(v);
		}
	}
}
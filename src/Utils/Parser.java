package Utils;

import java.awt.geom.IllegalPathStateException;
import java.beans.IntrospectionException;
import java.io.*;
import java.util.Arrays;
import java.util.stream.Stream;

public class Parser
{

	public static double[][] getLabelsArray(String fileName, int rows) throws IOException
	{
		if (!fileName.contains(".csv"))
			throw new IllegalPathStateException();
		BufferedReader file = new BufferedReader(new FileReader(fileName));

		int[] labels = new int[rows - 1];

		file.readLine();
		for (int i = 0; i < rows - 1; i++)
		{
			try
			{
				labels[i] = Character.getNumericValue(file.read());
				if(labels[i] < 0)
                    System.out.println("ПИЗДЕЦ НАХУЙ БЛЯТЬ");
				file.readLine();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		double[][] labelsArray = new double[rows - 1][10];
        for (int i = 0; i < rows - 1; i++)
        {
            Arrays.fill(labelsArray[i], 0);
            labelsArray[i][labels[i]] = 1;
        }
		
		return labelsArray;
	}

	public static double[][] getInputsArray(String fileName, int rows, int columns) throws IOException
	{
		if (!fileName.contains(".csv")) //Checking for correct format
			throw new IllegalPathStateException();

		BufferedReader file = new BufferedReader(new FileReader(fileName));

		file.readLine(); //Skip header

		double[][] inputs = new double[rows - 1][columns - 1];

		for (int i = 0; i < rows - 1; i++)
		{
			String buffer = file.readLine() + ',';
			inputs[i] = getIntArray(buffer, columns - 1).clone(); //Get training set
		}

		return inputs;
	}


	private static double[] getIntArray(String string, int columns)
	{
		double[] array = new double[columns];

		int k = 0;
		StringBuffer buffer = new StringBuffer();
		for (int i = 2; i < string.length(); i++)
			if (Character.isDigit(string.charAt(i)))
				buffer.append(string.charAt(i));
			else if (buffer.length() != 0)
			{
				array[k++] = Integer.parseInt(buffer.toString());
				buffer.delete(0, buffer.length());
			}
		return array;
	}
}
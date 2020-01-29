package Utils;

import java.util.Random;

public class Randomizer
{
	public static double getDouble(double min, double max)
	{
		Random rnd = new Random();
		return rnd.nextDouble() * (max - min) + min;
	}
	public static  boolean getBool()
	{
		Random rnd = new Random();
		return rnd.nextBoolean();
	}
	public static int getInt(int max)
	{
		Random rnd = new Random();
		return rnd.nextInt(max);
	}
}

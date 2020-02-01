import Utils.Parser;
import java.io.IOException;


public class Main
{
	public static void main(String[] args)
	{
		int[] labels = null;
		try {
			 labels = Parser.getLabelsArray("src/train.csv", 42001);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int[][] inputs = null;

		try {
			inputs = Parser.getInputsArray("src/train.csv", 42001, 785);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int[] set : inputs) {
			for (int i : set) {
				System.out.print(i + " ");
			}
			System.out.println();
		}
	}
}
package Utils;

import java.awt.geom.IllegalPathStateException;
import java.io.*;

public class Parser {

    public static int[] getLabelsArray(String fileName, int rows) throws IOException {
        if (!fileName.contains(".csv"))
            throw new IllegalPathStateException();
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        if (file == null) {
            throw new FileNotFoundException();
        }
        int[] labels = new int[rows];

        file.readLine();
        for (int i = 0; i < rows; i++) {
            try {
                labels[i] = Character.getNumericValue(file.read());
                file.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return labels;
    }

    public static int[][] getInputsArray(String fileName, int rows, int columns) throws IOException {
        if (!fileName.contains(".csv")) //Checking for correct format
            throw new IllegalPathStateException();

        BufferedReader file = new BufferedReader(new FileReader(fileName));

        file.readLine(); //Skip header

        int[][] inputs = new int[rows - 1][columns];

        for (int i = 0; i < rows - 1; i++) {
            file.read(); //Skip label
            file.read(); //Skip comma after label
            for (int j = 0; j < columns; j++) {
                inputs[i][j] = getNextInt(file); //Get pixel value
            }
        }

        return inputs;
    }

    private static int getNextInt(BufferedReader file) throws IOException {
        StringBuffer buffer = new StringBuffer();

        char c;
        while (true) {
            c = (char) file.read();
            if(c == '\r')
                c = (char) file.read();
            if (c == '\n' || c == '\0' || c == ',') {
                try {
                    return Integer.parseInt(buffer.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            buffer.append(c);
        }
    }
}

import java.io.*;

public class CheckStringInFile {

    public static void main(String[] args) {
        String specificString = "moth";
        String filename = "C:\\Users\\ahma3\\IdeaProjects\\GoWell-new2\\src\\queries.txt";

        try {
            // Open the file in read mode
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            boolean isPresent = false;

            // Read the file line by line and check if the specific string is present
            while ((line = reader.readLine()) != null) {
                if (line.contains(specificString)) {
                    isPresent = true;
                    break;
                }
            }

            reader.close();

            if (isPresent) {
                System.out.println("The string is already in the file.");
            } else {
                // If the string is not present, proceed with insertion
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
                writer.write(specificString);
                writer.newLine();
                writer.close();
                System.out.println("The string has been inserted into the file.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
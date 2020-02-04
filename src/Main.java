//Ryan Connors
//Rulx Sainlus
//COMP399 Distributed Systems
//WordCount project


import java.io.*;
import java.nio.file.*;

public class Main {

    public static void main(String []args) {
        File[] files = getFiles();
        printFiles(files);


    }

    private static File[] getFiles(){
        Path folderPath = FileSystems.getDefault().getPath("txtFolder");
        File txtFolder = folderPath.toFile();
        return txtFolder.listFiles();
    }

    private static void printFiles(File[] files) {
        String line;
        for (File current : files) {
            try (BufferedReader out = new BufferedReader(new FileReader(current))) {
                while((line = out.readLine()) != null) {
                    System.out.println(line);
                }
            } catch(IOException ex) {
                System.err.println(ex);
            }
        }
    }
}

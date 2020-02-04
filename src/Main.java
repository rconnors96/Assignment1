//Ryan Connors
//Rulx Sainlus
//COMP399 Distributed Systems
//WordCount project


import java.io.*;
import java.nio.file.*;
import java.util.Hashtable;

public class Main {

    //Hashtable can be shared between multiple threads, HashMap can not.
    private static final Hashtable wordCount = new Hashtable();

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

    private static void countWords(File[] files) {
        String line;
        char curChar;


        for (File current : files) { //goes through each txt file in the directory
            try (BufferedReader out = new BufferedReader(new FileReader(current))) { //tries to create a BufferedReader object with the current file
                while((line = out.readLine()) != null) { //goes until there are no more lines in the file
                    String word = "";
                    for (int i = 0; i < line.length(); i++) { //goes until there are no more characters in the line
                        curChar = line.charAt(i); //sets the current character in the line to curChar
                        if (curChar != ' ') {
                            word += curChar; //START HERE, TRY STRINGBUILDER
                        }
                    }
                }
            } catch(IOException ex) {
                System.err.println(ex);
            }
        }
    }
}

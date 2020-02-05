import java.io.*;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class FileThread implements Runnable {

    //Hashtable can be shared between multiple threads, HashMap can not.
    volatile Map<String, Integer> wordHash = new ConcurrentHashMap<>();
    private File file;
    private Vector<String> words = new Vector<>();
    private Vector<String> uniqueWords = new Vector<>();

    public FileThread(File file) {
        this.file = file;
    }


    @Override
    public void run() {

        String line;
        char curChar;
        String curWord = "";

        try (BufferedReader out = new BufferedReader(new FileReader(file))) { //tries to create a BufferedReader object with the current file
            while((line = out.readLine()) != null) { //goes until there are no more lines in the file
                for (int i = 0; i < line.length(); i++) { //goes until there are no more characters in the line
                    curChar = line.charAt(i); //sets the current character in the line to curChar
                    if (curChar != ' ') {
                        curWord += curChar; //If the current character is not a space, it adds the character to a whole word String
                    } else {
                        if (!curWord.equals("")) { //makes sure word String is not empty
                            words.addElement(curWord.toLowerCase()); //Once the word has been completed (a space has been found) it adds the word to a Vector<String>
                        }
                        curWord = ""; //resets the current Word variable
                    }
                }
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }

        for (String current : words) { //goes through every word in the txt files
            if (!uniqueWords.contains(current)) { //if the current word has not yet been added to uniqueWords,
                uniqueWords.addElement(current); //it gets added
            }
        }

        for (String current : uniqueWords) { //goes through each unique word found
            wordHash.put(current, Collections.frequency(words, current));
            //the word is put as the key in the wordsHash table, and the number of times found in the files is used as the value
        }

        Vector<String> wordsInFile = new Vector<>();
        Set<String> keys = wordHash.keySet(); //makes set of just the keys
        File results = new File("results.txt"); //sets the result txt File object

        try (BufferedReader in = new BufferedReader(new FileReader(results))) {
            while ((line = in.readLine()) != null) {
                String currentWord = "";
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) != ':') {
                        currentWord += line.charAt(i);
                    }
                }
                wordsInFile.addElement(currentWord.toLowerCase());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //WRITES the data to the results file
        try (BufferedWriter out = new BufferedWriter(new FileWriter(results, true))) {
            for (String key : keys) {
                if (!wordsInFile.contains(key)) {
                    out.write(key.substring(0, 1).toUpperCase() + key.substring(1) + ":" + wordHash.get(key));
                    out.newLine();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}



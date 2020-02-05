import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class FileThread implements Runnable {

    //Hashtable can be shared between multiple threads, HashMap can not.
    private ConcurrentHashMap<String, Integer> wordHash = new ConcurrentHashMap<>();
    private File file;
    Semaphore sem;

    public FileThread(File file, Semaphore sem) {
        this.file = file;
        this.sem = sem;
    }


    @Override
    public void run() {
        Vector<String> allWords = getWords();
        try{
            sem.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        addWordsToHash(allWords);
        sem.release();

        try{
            sem.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        writeToFile();
        sem.release();
    }


    private Vector<String> getWords() {
        String line;
        char curChar;
        String curWord = "";
        Vector<String> words = new Vector<>();

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

        return words;
    }

    public synchronized void addWordsToHash(Vector<String> words) {

        Vector<String> uniqueWords = new Vector<>();
        for (String current : words) { //goes through every word in the txt files
            if (!uniqueWords.contains(current)) { //if the current word has not yet been added to uniqueWords,
                uniqueWords.addElement(current); //it gets added
            }
        }

        Set<String> hashKeys;
        int currentCount;
        for (String current : uniqueWords) { //goes through each unique word found
            hashKeys = wordHash.keySet();
            for (String key : hashKeys) {
                if (key.equals(current)) {
                    currentCount = wordHash.get(key);
                    wordHash.remove(key);
                    wordHash.put(current, (currentCount + Collections.frequency(words, current)));
                }
            }
            wordHash.put(current, Collections.frequency(words, current));

            //the word is put as the key in the wordsHash table, and the number of times found in the files is used as the value
        }
    }

    private void writeToFile() {

        Set<String> keys = wordHash.keySet(); //makes set of just the keys
        File results = new File("results.txt"); //sets the result txt File object

        //WRITES the data to the results file
        try (BufferedWriter out = new BufferedWriter(new FileWriter(results, true))) {
            for (String key : keys) {
                out.write(key.substring(0, 1).toUpperCase() + key.substring(1) + ":" + wordHash.get(key));
                out.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}



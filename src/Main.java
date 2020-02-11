//Ryan Connors
//Rulx Sainlus
//COMP399 Distributed Systems
//WordCount project


import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class Main {

    public static Map<String,Integer> wordsAndCounts = new ConcurrentHashMap<>();

    public static void main(String []args) {
        clearFile(); //clears data from results.txt
        File[] files = getFiles(); //generates the File objects representing the txt files
        Vector<Thread> threads = new Vector<>();

        for (File file : files) {
            Runnable run = new FileThread(file);
            threads.addElement(new Thread(run));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        Map<String,Integer> orderedWords = new TreeMap<>(wordsAndCounts);
        writeToFile(orderedWords);

    }


    static void writeToFile(Map orderedWords) {

        Set<String> keys = orderedWords.keySet(); //makes set of just the keys
        File results = new File("results.txt"); //sets the result txt File object
        int wordsAlreadyPrinted = 0;

        //WRITES the data to the results file
        try (BufferedWriter out = new BufferedWriter(new FileWriter(results, true))) {
            for (String key : keys) {
                out.write(key.substring(0, 1).toUpperCase() + key.substring(1) + ":" + orderedWords.get(key));
                wordsAlreadyPrinted++;
                if (wordsAlreadyPrinted < keys.size()) {
                    out.newLine();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    static void addWordsToMap(Vector<String> words) {

        Vector<String> uniqueWords = new Vector<>();
        for (String current : words) { //goes through every word in the txt files
            if (!uniqueWords.contains(current)) { //if the current word has not yet been added to uniqueWords,
                uniqueWords.addElement(current); //it gets added
            }
        }


        for (String current : uniqueWords) { //goes through each unique word found
            int currentCount = 0;
            if(wordsAndCounts.containsKey(current)) {
                currentCount = wordsAndCounts.get(current);
            }
            wordsAndCounts.put(current, (Collections.frequency(words, current) + currentCount));
            //the word is put as the key in the wordsHash table, and the number of times found in the files is used as the value
        }
    }


    /**Uses the txtFolder directory as a File object. It then returns a File[] containing the File object forms of each txt file
     *
     * @return File[]
     */
    static File[] getFiles(){
        File txtFolder = new File(".\\txtFolder");
        return txtFolder.listFiles();
    }


    /**Clears data from the results.txt file
     *
     */
    static void clearFile() {

        try (PrintWriter clear = new PrintWriter(new File("results.txt"))) {
            clear.print("");
        } catch(IOException ex) {
            ex.printStackTrace();
        }

    }


    /*
    /**Prints the contents of each file in a File[]
     *
     * @param files a File[] of the txt files to be printed
     */
    /*
    private static void printFiles(File[] files) {
        String line;
        for (File current : files) {
            try (BufferedReader out = new BufferedReader(new FileReader(current))) {
                while((line = out.readLine()) != null) {
                    System.out.println(line);
                }
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    */
}
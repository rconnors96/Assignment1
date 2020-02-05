//Ryan Connors
//Rulx Sainlus
//COMP399 Distributed Systems
//WordCount project


import java.io.*;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

public class Main {

    //Hashtable can be shared between multiple threads, HashMap can not.
    private static final Hashtable<String, Integer> wordHash = new Hashtable<>();

    public static void main(String []args) {
        File[] files = getFiles(); //generates the File objects representing the txt files
        countWords(listWords(files)); //obtains a Vector<String> of all the words in the files and passes it to be counted

        System.out.print(wordHash.toString()); //displays the hash table for proof that it works
    }

    /**Uses the txtFolder directory as a File object. It then returns a File[] containing the File object forms of each txt file
     *
     * @return File[]
     */
    private static File[] getFiles(){
        File txtFolder = new File(".\\txtFolder");
        return txtFolder.listFiles();
    }

    /**Prints the contents of each file in a File[]
     *
     * @param files a File[] of the txt files to be printed
     */
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

    /**Adds every word individually to a Vector<String> to be counted in a different method
     *
     * @param files a File[] of txt files containing words to be listed
     * @return Vector<String> words
     */
    private static Vector<String> listWords(File[] files) {
        String line;
        char curChar;
        String curWord = "";
        Vector<String> words = new Vector<>();


        for (File current : files) { //goes through each txt file in the directory
            try (BufferedReader out = new BufferedReader(new FileReader(current))) { //tries to create a BufferedReader object with the current file
                while((line = out.readLine()) != null) { //goes until there are no more lines in the file
                    for (int i = 0; i < line.length(); i++) { //goes until there are no more characters in the line
                        curChar = line.charAt(i); //sets the current character in the line to curChar
                        if (curChar != ' ') {
                            curWord += curChar; //If the current character is not a space, it adds the character to a whole word String
                        } else {
                            if (!curWord.equals("")) { //makes sure word String is not empty
                                words.addElement(curWord); //Once the word has been completed (a space has been found) it adds the word to a Vector<String>
                            }
                            curWord = ""; //resets the current Word variable
                        }
                    }
                }
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }

        return words;
    }

    /**Counts the frequency at which each word appears
     *
     * @param words the Vector<String> containing all the words to be counted
     */
    private static void countWords(Vector<String> words) {
        Vector<String> uniqueWords = new Vector<>();//holds the Strings of unique words

        for (String current : words) { //goes through every word in the txt files
            if (!uniqueWords.contains(current)) { //if the current word has not yet been added to uniqueWords,
                uniqueWords.addElement(current); //it gets added
            }
        }

        for (String current : uniqueWords) { //goes through each unique word found
            wordHash.put(current, Collections.frequency(words, current));
            //the word is put as the key in the wordsHash table, and the number of times found in the files is used as the value
        }
    }
}

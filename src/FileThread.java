//Ryan Connors
//Rulx Sainlus
//COMP399 Distributed Systems
//FileThread for word count program


import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class FileThread implements Runnable {

    private File file;

    public FileThread(File file) {
        this.file = file;
    }


    @Override
    public void run() {
        Vector<String> allWords = getWords();
        Main.addWordsToMap(allWords);
    }


    private Vector<String> getWords() {
        String line;
        char curChar;
        String curWord = "";
        Vector<String> words = new Vector<>();
        StringBuilder strb;

        try (BufferedReader out = new BufferedReader(new FileReader(file))) { //tries to create a BufferedReader object with the current file
            while((line = out.readLine()) != null) { //goes until there are no more lines in the file
                for (int i = 0; i < line.length(); i++) { //goes until there are no more characters in the line
                    curChar = line.charAt(i); //sets the current character in the line to curChar
                    if (curChar != ' ') {
                        strb = new StringBuilder(curWord);
                        strb.append(curChar);
                        curWord = strb.toString(); //If the current character is not a space, it adds the character to a whole word String
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
}

//Ryan Connors
//Rulx Sainlus
//COMP399 Distributed Systems
//WordCount project


import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String []args) {
        clearFile(); //clears data from results.txt
        File[] files = getFiles(); //generates the File objects representing the txt files
        Vector<Thread> threads = new Vector<>();
        Semaphore sem = new Semaphore(1);

        for (File file : files) {
            Runnable run = new FileThread(file, sem);
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


    }


    /**Uses the txtFolder directory as a File object. It then returns a File[] containing the File object forms of each txt file
     *
     * @return File[]
     */
    private static File[] getFiles(){
        File txtFolder = new File(".\\txtFolder");
        return txtFolder.listFiles();
    }


    /**Clears data from the results.txt file
     *
     */
    private static void clearFile() {

        try (PrintWriter clear = new PrintWriter(new File("results.txt"))) {
            clear.print("");
        } catch(IOException ex) {
            ex.printStackTrace();
        }

    }



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
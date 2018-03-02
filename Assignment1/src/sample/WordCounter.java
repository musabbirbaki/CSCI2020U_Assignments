package sample;

import java.io.File;
import java.util.*;

/**
 * This class has one main function
 *
 * countWords(String directory): this function takes a directory and counts
 * all the unique words in each file in that folder and saves it in the wordCounts
 * Map attribute.
 * */
public class WordCounter {

    protected Map<String,Integer> wordCounts;
    protected int numberOfFiles;


    public WordCounter() {
        wordCounts = new TreeMap<>();
        numberOfFiles = 0;
    }

    /**
     * Count words from files in the directory
     * */
    public void countWords(String directory){

        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        this.numberOfFiles = listOfFiles.length;
        System.out.println("Reading " + numberOfFiles + " files from: " + directory);

        int counter = 0;

        //loops through the number of files
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {//if file is a file and not a directory

                //System.out.println("File " + listOfFiles[i].getName());

                String filePathName = listOfFiles[i].getName();
                //System.out.println("File " + filePathName);

                try {

                    //File infile = new File(filePathName);
                    //fileExists = infile.exists();

                    //Scanner scanner = new Scanner(infile);
                    Scanner scanner = new Scanner(listOfFiles[i]);
                    while(scanner.hasNext()){
                        String nextWord = scanner.next();
                        //each word in a file
                        countWord(nextWord);
                        //words.add(nextWord);
                    }
                }catch(Exception e){

                    System.out.println("Failed to Open File: " + filePathName);
                    counter++;
                    //if(fileExists == false){
                    //System.out.println("File doesn't Exist in " + filePathName);
                    //}

                }


            }
        }

        //System.out.println("Number of files couldn't open " + counter);
    }

    public Map<String,Integer> getWordCounts(){
        return this.wordCounts;
    }

    public int getNumberOfFiles(){
        return this.numberOfFiles;
    }

    protected void countWord(String word) {
        if (wordCounts.containsKey(word)) {
            int oldCount = wordCounts.get(word);
            wordCounts.put(word, oldCount+1);
        } else {
            wordCounts.put(word, 1);
        }
    }


}

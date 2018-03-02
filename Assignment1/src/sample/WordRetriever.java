package sample;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


/**
 * This class has two main functions:
 *
 * getWordsFromFile(File infile): takes in a file parameter and
 * returns a Set of strings which are the unique words contained
 * int the file infile.
 *
 * getFilesFromDirectory(String directory): function takes in
 * a directory name as a parametor. This method returns an
 * array of files that are contained in that directory.
 * */
public class WordRetriever {

    /**
     * This function returns a Set of words from a file
     * */
    public static Set<String> getWordsFromFile(File infile){
        Set<String> words = new HashSet<>();
        Boolean fileExists = false;
        try {
            //File infile = new File(filePathName);
            fileExists = infile.exists();

            Scanner scanner = new Scanner(infile);
            while(scanner.hasNext()){
                String nextWord = scanner.next();
                words.add(nextWord);
            }
        }catch(Exception e){

            System.out.println("Failed to Open File.");
            if(fileExists == false){
                System.out.println("File doesn't Exist");
            }else{
                System.out.println(e);
            }

        }

        return words;
    }

    public static File[] getFilesFromDirectory(String directory){
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        return listOfFiles;
    }
}

package sample;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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

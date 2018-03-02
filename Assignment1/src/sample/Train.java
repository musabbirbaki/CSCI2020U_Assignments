package sample;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.io.File;

public class Train {

    /**
     * This function takes in the directory of train spam and train Ham and trains.
     * The training returns a map of probabilities - the probabilities of being spam such
     * that a word is given
     * */
    public static Map<String, Double> runTrain(String trainFolderDirectory){
        //get all spam and ham folder names in train folder
        ArrayList<String> spamFolders = new ArrayList<>();
        ArrayList<String> hamFolders = new ArrayList<>();
        File[] allFilesInTrainDir =  WordRetriever.getFilesFromDirectory(trainFolderDirectory);
        for (File f: allFilesInTrainDir){
            if(f.getName().toLowerCase().contains("spam")){
                spamFolders.add(f.getName());
            }else if(f.getName().toLowerCase().contains("ham")){
                hamFolders.add(f.getName());
            }
        }


        System.out.println("\nStarting Training.");
        //Ask user for file directory, ex given in listing 2
        //user will select the folder which contains the train and test folder
        //This variable will hold the word and [probability of spam, and probability of being ham]
        //collect data from train spam data

        //spam word mapping process below
        Map<String,Integer> trainSpamWordCounts = new TreeMap<>();
        int numberOfTrainSpamFiles = 0;

        //For every spam folder read the files and Populate the trainSpamWordsCounts map
        for (String name: spamFolders){
            String trainSpam = trainFolderDirectory + "/" +name;

            WordCounter trainSpamWordCounter = new WordCounter();
            trainSpamWordCounter.countWords(trainSpam);

            Map<String,Integer> temp = trainSpamWordCounter.getWordCounts();
            for(String key: temp.keySet()){
                trainSpamWordCounts.put(key, temp.get(key));
            }
            numberOfTrainSpamFiles += trainSpamWordCounter.getNumberOfFiles();
        }
        System.out.println("Read Total of " + numberOfTrainSpamFiles + " number of Spam Files.");
        System.out.println("Training Spam Files read successfully.\n");


        Map<String, ArrayList<Double>> probSpamHam = new TreeMap<>();

        //calculate the probability of Word such that file is Spam (pWS)
        double pWS;
        ArrayList<Double> i;
        for(String word: trainSpamWordCounts.keySet()){
            //get value of key word, which is the # of spam files containing the current word
            pWS = (double)trainSpamWordCounts.get(word) / (double)numberOfTrainSpamFiles;
            i = new ArrayList<>();
            i.add(pWS);
            probSpamHam.put(word, i);
        }

        //ham word mapping process below
        Map<String,Integer> trainHamWordCounts = new TreeMap<>();
        int numberOfTrainHamFiles = 0;

        //For every spam folder read the files and Populate the trainSpamWordsCounts map
        for (String name: hamFolders){
            String trainHam = trainFolderDirectory + "/" + name;
            WordCounterHam trainHamWordCounter = new WordCounterHam(probSpamHam.keySet());
            trainHamWordCounter.countWords(trainHam);

            Map<String,Integer> temp = trainHamWordCounter.getWordCounts();
            for(String key: temp.keySet()){
                trainHamWordCounts.put(key, temp.get(key));
            }
            numberOfTrainHamFiles += trainHamWordCounter.getNumberOfFiles();
        }
        System.out.println("Read Total of " + numberOfTrainHamFiles + " number of Ham Files.");
        System.out.println("Training Ham Files read successfully.\n");

        //calculate the probability of word occuring such that file is ham (pWH)
        double pWH;
        for(String word: trainHamWordCounts.keySet()){
            //get value of key word, which is the # of spam files containing the current word
            pWH = (double)trainHamWordCounts.get(word) / (double)numberOfTrainHamFiles;
            probSpamHam.get(word).add(pWH);
        }

        //Calculate the probability of Spam such that a word is given (pSW)
        //this calculation uses the Map(word, [probability spam, probability ham]) data
        //  word  , probability
        Map<String, Double> probSgivenWord = new TreeMap<>();//final set that is needed
        double pSW;
        for(String word: probSpamHam.keySet()){
            pSW = probSpamHam.get(word).get(0) / (probSpamHam.get(word).get(0) + probSpamHam.get(word).get(1));
            probSgivenWord.put(word, pSW);
        }

        return probSgivenWord;
    }
}

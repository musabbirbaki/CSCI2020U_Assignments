package sample;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Train {

    /**
     * This function takes in the directory of train spam and train Ham and trains.
     * The training returns a map of probabilities - the probabilities of being spam such
     * that a word is given
     * */
    public static Map<String, Double> runTrain(String trainFolderDirectory){
        String trainSpam = trainFolderDirectory + "/spam";
        String trainHam = trainFolderDirectory + "/ham";

        System.out.println("Starting...........");


        //Ask user for file directory, ex given in listing 2

        //user will select the folder which contains the train and test folder

        //This variable will hold the word and [probability of spam, and probability of being ham]
        Map<String, ArrayList<Double>> probSpamHam = new TreeMap<>();

        //collect data from train spam data
        WordCounter trainSpamWordCounter = new WordCounter();
        trainSpamWordCounter.countWords(trainSpam);
        Map<String,Integer> trainSpamWordCounts = trainSpamWordCounter.getWordCounts();//delete this
        int numberOfTrainSpamFiles = trainSpamWordCounter.getNumberOfFiles();
        System.out.println("Training Files read successfully.");

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
        trainSpamWordCounts = null;//assisting garbage collector


        //collect data from ham files
        WordCounterHam trainHamWordCounter = new WordCounterHam(probSpamHam.keySet());
        trainHamWordCounter.countWords(trainHam);
        Map<String,Integer> trainHamWordCounts = trainHamWordCounter.getWordCounts();//delete this
        int numberOfTrainHamFiles = trainHamWordCounter.getNumberOfFiles();
        System.out.println("Ham Files read successfully.");

        //calculate the probability of word occuring such that file is ham (pWH)
        double pWH;
        for(String word: trainHamWordCounts.keySet()){
            //get value of key word, which is the # of spam files containing the current word
            pWH = (double)trainHamWordCounts.get(word) / (double)numberOfTrainHamFiles;
            probSpamHam.get(word).add(pWH);
        }
        trainHamWordCounts = null;

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

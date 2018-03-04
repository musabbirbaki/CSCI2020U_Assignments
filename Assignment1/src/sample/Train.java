package sample;

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

        //spam word mapping process below
        Map<String,Integer> trainSpamWordCounts = new TreeMap<>();
        int numberOfTrainSpamFiles = 0;
        Map<String,Integer> trainHamWordCounts = new TreeMap<>();
        int numberOfTrainHamFiles = 0;


        //For every spam folder read the files and Populate the trainSpamWordsCounts map
        for (String name: spamFolders){
            String trainSpam = trainFolderDirectory + "/" +name;

            WordCounter trainSpamWordCounter = new WordCounter();
            trainSpamWordCounter.countWords(trainSpam);

            Map<String,Integer> temp = trainSpamWordCounter.getWordCounts();

            for(String key: temp.keySet()){
                //if key exists just add the temp.get(key)
                if(trainSpamWordCounts.containsKey(key)){
                    trainSpamWordCounts.put(key, trainSpamWordCounts.get(key) + temp.get(key));
                }else{
                    //else put new key
                    trainSpamWordCounts.put(key, temp.get(key));

                    //add word to ham aswel with zero counts for now
                    if(!trainHamWordCounts.containsKey(key)) {
                        trainHamWordCounts.put(key, 0);
                    }
                }
            }
            numberOfTrainSpamFiles += trainSpamWordCounter.getNumberOfFiles();
        }
        System.out.println("Read Total of " + numberOfTrainSpamFiles + " number of Spam Files.");
        System.out.println("Training Spam Files read successfully.\n");


        //For every spam folder read the files and Populate the trainSpamWordsCounts map
        for (String name: hamFolders){
            String trainHam = trainFolderDirectory + "/" + name;
            WordCounter trainHamWordCounter = new WordCounter();
            trainHamWordCounter.countWords(trainHam);

            Map<String,Integer> temp = trainHamWordCounter.getWordCounts();

            for(String key: temp.keySet()){

                //if key exists just add the temp.get(key)
                if(trainHamWordCounts.containsKey(key)){
                    trainHamWordCounts.put(key, trainHamWordCounts.get(key) + temp.get(key));
                }else {
                    //else put new key
                    trainHamWordCounts.put(key, temp.get(key));

                    if(!trainSpamWordCounts.containsKey(key)){
                        trainSpamWordCounts.put(key, 0);
                    }
                }
            }
            numberOfTrainHamFiles += trainHamWordCounter.getNumberOfFiles();
        }
        System.out.println("Read Total of " + numberOfTrainHamFiles + " number of Ham Files.");
        System.out.println("Training Ham Files read successfully.\n");


        /*
        This data structure holds a word as a key, the value is an array list with two elements
        example, (key: word, values: Word | Spam Probability, Word | Ham Probability)
        */
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


        //calculate the probability of word occuring such that file is ham (pWH)
        double pWH;
        for(String word: trainHamWordCounts.keySet()){
            //get value of key word, which is the # of spam files containing the current word
            pWH = (double)trainHamWordCounts.get(word) / (double)numberOfTrainHamFiles;
            probSpamHam.get(word).add(pWH);
        }


        //Calculate the probability of Spam such that a word is given (pSW)
        Map<String, Double> probSgivenWord = new TreeMap<>();//final set that is needed
        double pSW;
        for(String word: probSpamHam.keySet()){

            if(probSpamHam.get(word).get(0) !=0 && probSpamHam.get(word).get(1) !=0) {
                pSW = probSpamHam.get(word).get(0) / (probSpamHam.get(word).get(0) + probSpamHam.get(word).get(1));
                probSgivenWord.put(word, pSW);
            }
        }

        return probSgivenWord;
    }
}

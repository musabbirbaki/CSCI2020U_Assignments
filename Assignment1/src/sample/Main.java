package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();


        //DirectoryChooser directoryChooser = new DirectoryChooser();
        //directoryChooser.setInitialDirectory(new File("."));
        //File mainDirectory = directoryChooser.showDialog(primaryStage);
    }

    //private Map<String,Integer> wordCounts;

    public static void main(String[] args) {

        //launch(args);

        System.out.println("Starting...........");

        String trainFolderDirectory;
        String trainSpam;
        String trainHam;

        String testFolderDirectory;
        String testSpam;
        String testHam;

        //Ask user for file directory, ex given in listing 2

        //user will select the folder which contains the train and test folder

        //get Train folder directory
        //trainFolderDirectory = "/home/musabbir/Documents/CSCI2020TestData/data/test/ham/00001.1a31cc283af0060967a233d26548a6ce";
        trainFolderDirectory = "/home/musabbir/Documents/CSCI2020TestData/data/train";
        trainSpam = trainFolderDirectory + "/spam";
        trainHam = trainFolderDirectory + "/ham";

        //get Test folder directory
        testFolderDirectory = "/home/musabbir/Documents/CSCI2020TestData/data/test";
        testSpam = testFolderDirectory + "/spam";
        testHam = testFolderDirectory + "/ham";


        Map<String, ArrayList<Double>> probSpamHam = new TreeMap<>();


        //train spam data
        WordCounter trainSpamWordCounter = new WordCounter();
        trainSpamWordCounter.countWords(trainSpam);
        Map<String,Integer> trainSpamWordCounts = trainSpamWordCounter.getWordCounts();//delete this
        int numberOfTrainSpamFiles = trainSpamWordCounter.getNumberOfFiles();
        System.out.println("Files read successfully.");

        double pWS;

        ArrayList<Double> i;
        for(String word: trainSpamWordCounts.keySet()){
            //get value of key word, which is the # of spam files containing the current word
            pWS = (double)trainSpamWordCounts.get(word) / (double)numberOfTrainSpamFiles;

            i = new ArrayList<>();
            i.add(pWS);
            probSpamHam.put(word, i);
        }
        trainSpamWordCounts = null;


        /////////////////////////////////////////////////////
        //train ham data
        WordCounterHam trainHamWordCounter = new WordCounterHam(probSpamHam.keySet());
        trainHamWordCounter.countWords(trainHam);
        Map<String,Integer> trainHamWordCounts = trainHamWordCounter.getWordCounts();//delete this
        int numberOfTrainHamFiles = trainHamWordCounter.getNumberOfFiles();
        System.out.println("Files read successfully.");

        double pWH;
        for(String word: trainHamWordCounts.keySet()){
            //get value of key word, which is the # of spam files containing the current word
            pWH = (double)trainHamWordCounts.get(word) / (double)numberOfTrainHamFiles;
            probSpamHam.get(word).add(pWH);
        }
        trainHamWordCounts = null;

        System.out.println("Size of HamSpam: " + probSpamHam.keySet().size());

        //  word  , probability
        Map<String, Double> probSgivenWord = new TreeMap<>();//final set that is needed
        double pSW;
        for(String word: probSpamHam.keySet()){
            pSW = probSpamHam.get(word).get(0) / (probSpamHam.get(word).get(0) + probSpamHam.get(word).get(1));
            probSgivenWord.put(word, pSW);

            //System.out.println(probSgivenWord.get(word));
        }

//                int uuuuu = 0;
//        for(String s: probSpamHam.keySet()){
//            if(probSpamHam.get(s).get(1) > 0){
//                System.out.println(s + ":" + probSpamHam.get(s).get(0) + "--" + probSpamHam.get(s).get(1));
//                uuuuu++;
//            }
//        }

        //System.out.println(probSgivenWord.keySet().size());

        //Now i have prob of Spam such that a word

        System.out.println();
        System.out.println("------Starting Testing----------");

        ArrayList<TestFile> tFilesSpam = new ArrayList<>();
        ArrayList<TestFile> tFilesHam = new ArrayList<>();
        /////////Testing stage
        Set<String> testSpamWords;
        String actualClass = "Spam";
        File[] spamFiles = WordRetriever.getFilesFromDirectory(testSpam);
        for(File file: spamFiles){
            testSpamWords = WordRetriever.getWordsFromFile(file);

            //for every word calculate P(S|F)
            Double n = 0.0;
            for(String word: testSpamWords) {
                if(probSgivenWord.containsKey(word)) {
                    n += (Math.log(1.0 - probSgivenWord.get(word)) - Math.log(probSgivenWord.get(word)));
                }
            }
            Double probSpamSuchThatFile = 1 / (1 + Math.pow(Math.E, n));

            TestFile tFile = new TestFile(file.getName(), probSpamSuchThatFile, actualClass);
            tFilesSpam.add(tFile);
            //System.out.println(testSpamWords.size() + " Words, of : " + file.getName());
        }

        //System.out.println(tFiles.size() + " Test Spam Files");


        //for ham files
        Set<String> testHamWords;
        actualClass = "Ham";
        File[] hamFiles = WordRetriever.getFilesFromDirectory(testHam);
        for(File file: hamFiles){
            testHamWords = WordRetriever.getWordsFromFile(file);

            //for every word calculate P(S|F)
            Double n = 0.0;
            for(String word: testHamWords) {
                if(probSgivenWord.containsKey(word)) {
                    n += (Math.log(1.0 - probSgivenWord.get(word)) - Math.log(probSgivenWord.get(word)));
                }
            }
            Double probSpamSuchThatFile = 1 / (1 + Math.pow(Math.E, n));

            TestFile tFile = new TestFile(file.getName(), probSpamSuchThatFile, actualClass);
            tFilesHam.add(tFile);
            //System.out.println(testSpamWords.size() + " Words, of : " + file.getName());
        }



        ///Printing tfiles
        for(TestFile t: tFilesSpam){
            System.out.println("File:" + t.getFilename()
                    + "  SpamProbability:" + t.getSpamProbRounded()
                    + "  Actual:" + t.getActualClass());
        }
        for(TestFile t: tFilesHam){
            System.out.println("File:" + t.getFilename()
                    + "  SpamProbability:" + t.getSpamProbRounded()
                    + "  Actual:" + t.getActualClass());
        }


        //tFiles are an array list of TestFiles

        //calculate accuracy and precision
        int numTruePositives = 0; //Spam and got probability = 1, or Not spam got prob = 0
        int numTrueNegatives = 0;
        int numFalsePositives = 0;
        int numFiles = tFilesSpam.size() + tFilesHam.size();

        for(TestFile t: tFilesSpam){
            //calculate numTruePositives
            if(t.getSpamProbability() > 0.6){
                numTruePositives++;
            }else{
                numFalsePositives++;
            }
        }

        for(TestFile t: tFilesHam){
            //calculate numTruePositives
            if(t.getSpamProbability() < 0.6){
                numTruePositives++;
            }else{
                numTrueNegatives++;
            }
        }

        double accuracy = ((double)numTruePositives + (double)numTrueNegatives)/(double)numFiles;
        double precision = (double)numTruePositives / ((double)numFalsePositives + (double)numTruePositives);

        System.out.println("Accuracy: " + accuracy);
        System.out.println("Precision: " + precision);

        //your app runs training and test phases


        System.out.println("Completed.....");
        System.exit(0);
    }


}

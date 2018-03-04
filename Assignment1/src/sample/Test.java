package sample;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Test {

    private double Accuracy;
    private double Precision;
    private double Threshold;
    private ArrayList<TestFile> tFilesSpam;
    private ArrayList<TestFile> tFilesHam;

    private String testSpam;
    private String testHam;
    private Map<String, Double> probSgivenWord;


    public Test(String testFolderDirectory, Map<String, Double> probSgivenWord, double threshold){

        //initializing final array list of TestFiles
        this.tFilesSpam = new ArrayList<>();
        this.tFilesHam = new ArrayList<>();

        this.Accuracy = 0.0;
        this.Precision = 0.0;
        this.Threshold = threshold;

        this.testSpam = testFolderDirectory + "/spam";
        this.testHam = testFolderDirectory + "/ham";
        this.probSgivenWord = probSgivenWord;
    }

    /**
     * This fuction takes in the two directories of Spam, Ham files and a map of Spam such that a word
     * is given probability and runs the test.
     * */
    public void runTest(){
        System.out.println("----------Running Testing----------");


        //testing files from spam directory
        Set<String> testSpamWords;
        String actualClass = "Spam";
        File[] spamFiles = WordRetriever.getFilesFromDirectory(this.testSpam);
        for(File file: spamFiles){
            testSpamWords = WordRetriever.getWordsFromFile(file);

            //for every word calculate P(S|F)
            Double n = 0.0;
            for(String word: testSpamWords) {
                if(this.probSgivenWord.containsKey(word)) {
                    n += (Math.log(1.0 - this.probSgivenWord.get(word)) - Math.log(this.probSgivenWord.get(word)));
                }
            }
            Double probSpamSuchThatFile = 1 / (1 + Math.pow(Math.E, n));

            TestFile tFile = new TestFile(file.getName(), probSpamSuchThatFile, actualClass);
            this.tFilesSpam.add(tFile);
        }


        //testing files from Ham directory
        Set<String> testHamWords;
        actualClass = "Ham";
        File[] hamFiles = WordRetriever.getFilesFromDirectory(this.testHam);
        for(File file: hamFiles){
            testHamWords = WordRetriever.getWordsFromFile(file);

            //for every word calculate P(S|F)
            Double n = 0.0;
            for(String word: testHamWords) {
                if(this.probSgivenWord.containsKey(word)) {
                    n += (Math.log(1.0 - this.probSgivenWord.get(word)) - Math.log(this.probSgivenWord.get(word)));
                }
            }
            Double probSpamSuchThatFile = 1 / (1 + Math.pow(Math.E, n));

            TestFile tFile = new TestFile(file.getName(), probSpamSuchThatFile, actualClass);
            this.tFilesHam.add(tFile);
        }

        //calculate accuracy and precision
        calculatePrecisionAccuracy(this.Threshold); //threshold 0.7 seems to give best results
    }

    /**
     * This function calculates the precision and accurcy using the tFilesSpam and tFilesHam
     * */
    private void calculatePrecisionAccuracy(double threshold){
        int numTruePositives = 0; //Spam and got probability = 1, or Not spam got prob = 0
        int numTrueNegatives = 0;
        int numFalsePositives = 0;
        int numFalseNegatives = 0;
        int numFiles = this.tFilesSpam.size() + this.tFilesHam.size();


        for(TestFile t: this.tFilesSpam){
            if(t.getSpamProbabilityNotRounded() >= threshold){
                numTruePositives++;
            }else{
                numFalsePositives++;
            }
        }

        for(TestFile t: this.tFilesHam){
            if(t.getSpamProbabilityNotRounded() < threshold){
                numTrueNegatives++;
            }else{
                numFalseNegatives++;
            }
        }

        System.out.println("Number of Ham Files Tested: " + this.tFilesHam.size());
        System.out.println("Number of Spam Files Tested: " + this.tFilesSpam.size());
        System.out.println("Threshold: " + this.Threshold);
        System.out.println("Wrong Guess: " + (numFalseNegatives + numFalsePositives));
        System.out.println("Correct Guess: " + (numTrueNegatives + numTruePositives));
        System.out.println("-----------------");
        System.out.println("TP:" + numTruePositives + "\t\tFP:" + numFalsePositives);
        System.out.println("TN:" + numTrueNegatives + "\t\tFN:" + numFalseNegatives);
        System.out.println("-----------------");

        this.Accuracy = ((double)numTruePositives + (double)numTrueNegatives)/(double)numFiles;
        this.Precision = (double)numTruePositives / ((double)numFalsePositives + (double)numTruePositives);
    }

    public double getAccuracy() {
        return Accuracy;
    }

    public double getPrecision() {
        return Precision;
    }

    public ArrayList<TestFile> gettFilesHam() {
        return tFilesHam;
    }

    public ArrayList<TestFile> gettFilesSpam() {
        return tFilesSpam;
    }
}

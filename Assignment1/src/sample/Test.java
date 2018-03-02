package sample;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Test {

    private double Accuracy;
    private double Precision;
    private ArrayList<TestFile> tFilesSpam;
    private ArrayList<TestFile> tFilesHam;

    private String testSpam;
    private String testHam;
    private Map<String, Double> probSgivenWord;


    public Test(String testFolderDirectory, Map<String, Double> probSgivenWord){

        //initializing final array list of TestFiles
        this.tFilesSpam = new ArrayList<>();
        this.tFilesHam = new ArrayList<>();

        this.Accuracy = 0.0;
        this.Precision = 0.0;

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
        calculatePrecisionAccuracy();
    }

    /**
     * This function calculates the precision and accurcy using the tFilesSpam and tFilesHam
     * */
    private void calculatePrecisionAccuracy(){
        int numTruePositives = 0; //Spam and got probability = 1, or Not spam got prob = 0
        int numTrueNegatives = 0;
        int numFalsePositives = 0;
        int numFalseNegatives = 0;
        int numFiles = this.tFilesSpam.size() + this.tFilesHam.size();

        for(TestFile t: this.tFilesSpam){
            //calculate numTruePositives
            if(t.getSpamProbabilityNotRounded() > 0.6){
                numTruePositives++;
            }else{
                numFalsePositives++;
            }
        }

        for(TestFile t: this.tFilesHam){
            //calculate numTruePositives
            if(t.getSpamProbabilityNotRounded() < 0.6){
                numTrueNegatives++;
            }else{
                numFalseNegatives++;
            }
        }

        System.out.println(numFalseNegatives + numFalsePositives + " : WRONG");
        System.out.println(numTrueNegatives + numTruePositives + " : CORRECT");

        System.out.println(this.tFilesHam.size() + " : Number of Ham Files");
        System.out.println(this.tFilesSpam.size() + " : Number of Spam Files");

        this.Accuracy = ((double) numTruePositives + (double) numTrueNegatives)/((double) numTruePositives + (double) numTrueNegatives + (double) numFalsePositives + (double)numFalseNegatives);
        this.Precision = ((double) numTruePositives)/((double) numTruePositives + (double) numFalsePositives);
        //this.Accuracy = ((double)numTruePositives + (double)numTrueNegatives)/(double)numFiles;
        //this.Precision = (double)numTruePositives / ((double)numFalsePositives + (double)numTruePositives);
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

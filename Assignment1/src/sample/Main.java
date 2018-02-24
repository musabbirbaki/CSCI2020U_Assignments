package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
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

        String testFolderDirectory;


        //Ask user for file directory, ex given in listing 2

        //user will select the folder which contains the train and test folder

        //get Train folder directory
        trainFolderDirectory = "/home/musabbir/Documents/CSCI2020TestData/data/train";

        //get Test folder directory
        testFolderDirectory = "/home/musabbir/Documents/CSCI2020TestData/data/test";

        //run train on Train Directory
        Map<String, Double> probSgivenWord = Train.runTrain(trainFolderDirectory);


        Test test = new Test(testFolderDirectory, probSgivenWord);
        test.runTest();
        ArrayList<TestFile> tFilesHam = test.gettFilesHam();
        ArrayList<TestFile> tFilesSpam = test.gettFilesSpam();
        double accuracy = test.getAccuracy();
        double precision = test.getPrecision();

        //Printing tfiles
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

        System.out.println("Accuracy: " + accuracy);
        System.out.println("Precision: " + precision);
        System.out.println("Completed.....\n");
        System.exit(0);
    }


}

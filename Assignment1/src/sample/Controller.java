package sample;

import javafx.fxml.Initializable;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    private Stage stage;
    private String dataDirectory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Starting...........");
        String trainFolderDirectory;
        String testFolderDirectory;


        //String dataDirectory = getDataDirectory();
        setDataDirectory();
        System.out.println("Starting with: " + this.dataDirectory);


         //Ask user for file directory, ex given in listing 2

         //user will select the folder which contains the train and test folder

         //get Train folder directory
        trainFolderDirectory = dataDirectory + "/train";
         //trainFolderDirectory = "/home/musabbir/Documents/CSCI2020TestData/data/train";

         //get Test folder directory
        testFolderDirectory = dataDirectory + "/test";
        //testFolderDirectory = "/home/musabbir/Documents/CSCI2020TestData/data/test";

         //run train on Train Directory
         Map<String, Double> probSgivenWord = Train.runTrain(trainFolderDirectory);


         Test test = new Test(testFolderDirectory, probSgivenWord);
         test.runTest();
         ArrayList<TestFile> tFilesHam = test.gettFilesHam();
         ArrayList<TestFile> tFilesSpam = test.gettFilesSpam();
         double accuracy = test.getAccuracy();
         double precision = test.getPrecision();

         //ObservableList<TestFile> getAllFiles = DataSource.getAllFiles(tFilesHam,tFilesSpam);

         System.out.println("Accuracy: " + accuracy);
         System.out.println("Precision: " + precision);
         System.out.println("Completed.....\n");
         //System.exit(0);


    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    private void setDataDirectory(){

        try{
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File("."));
            File mainDirectory = directoryChooser.showDialog(this.stage);

            File[] files = mainDirectory.listFiles();

            boolean hasSpam = false;
            boolean hasHam = false;
            for(File f: files){
                if(f.isDirectory()){
                    if(f.getName().toLowerCase().contains("test")){
                        hasSpam = true;
                    }
                    if(f.getName().toLowerCase().contains("train")){
                        hasHam = true;
                    }
                }
            }

            this.dataDirectory = mainDirectory.getAbsolutePath();

            if(!hasSpam && !hasHam){
                System.out.println("Please Reselect data directory.");
            AlertBox.display("Error",
                    "This directory doesn't contain test and train files. \nPlease Reselect data directory.",
                    "Retry");
                setDataDirectory();
            }
        }catch(NullPointerException n){

            AlertBox.display("Null Pointer Error",
                    "The program faced a Null Pointer error. \n The program will close.",
                    "Close Program");
            System.exit(0);

        }catch(Exception e) {
            System.out.println(e);
            setDataDirectory();
        }

        System.out.println("Trying: " + dataDirectory);
    }
}

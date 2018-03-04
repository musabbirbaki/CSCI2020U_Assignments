package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Stage stage;
    private String dataDirectory;

    //declare table veiew
    @FXML private TableView<TestFile> tableView;

    //declare columns
    @FXML private TableColumn<TestFile, String> filenameColumn;
//    @FXML private TableColumn<TestFile, Double> spamProbabilityColumn;
    @FXML private TableColumn<TestFile, String> spamProbabilityColumn;
    @FXML private TableColumn<TestFile, String> actualClassColumn;

    @FXML private TextField Accuracy;
    @FXML private TextField Precision;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Starting Program\nWaiting for User to select Data directory.");
        String trainFolderDirectory;
        String testFolderDirectory;

        //user will select the folder which contains the train and test folder
        setDataDirectory();
        System.out.println("Chosen directory: " + this.dataDirectory);

        //get Train folder directory
        trainFolderDirectory = dataDirectory + "/train";

        //get Test folder directory
        testFolderDirectory = dataDirectory + "/test";

        //run train on Train Directory
        //can read multiple ham and spam folders
        Map<String, Double> probSgivenWord = Train.runTrain(trainFolderDirectory);

        //run test on Test Directory
        //only reads one folder for spam and ham each
        //Threshold: 0.7 means a file is only considered spam if the spam probability is greater than 0.7
        Test test = new Test(testFolderDirectory, probSgivenWord, 0.7);
        test.runTest();

        ArrayList<TestFile> tFilesHam = test.gettFilesHam();
        ArrayList<TestFile> tFilesSpam = test.gettFilesSpam();
        double accuracy = test.getAccuracy();
        double precision = test.getPrecision();

        //ObservableList<TestFile> getAllFiles = DataSource.getAllFiles(tFilesHam,tFilesSpam);
        showData(tFilesHam,tFilesSpam);

        this.Accuracy.setText(accuracy + "");
        this.Precision.setText(precision + "");

        System.out.println("Completed Testing\nShowing Results.\n");
        //System.exit(0);


    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * This function allows the user to select a proper "data" directory,
     * if the directory selected doesn't contain files "test" and "train"
     * the user has to retry selecting the directory until "test" and "train"
     * files have been found. If the user closes the directory selection
     * process. The program terminates using System.exit(0).
     */
    private void setDataDirectory() {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File("."));
            File mainDirectory = directoryChooser.showDialog(this.stage);

            File[] files = mainDirectory.listFiles();

            boolean hasSpam = false;
            boolean hasHam = false;
            for (File f : files) {
                if (f.isDirectory()) {
                    if (f.getName().toLowerCase().contains("test")) {
                        hasSpam = true;
                    }
                    if (f.getName().toLowerCase().contains("train")) {
                        hasHam = true;
                    }
                }
            }

            this.dataDirectory = mainDirectory.getAbsolutePath();

            if (!hasSpam && !hasHam) {
                System.out.println("Please Reselect data directory.");
                AlertBox.display("Error",
                        "This directory doesn't contain test and train files. \nPlease Reselect data directory.",
                        "Retry");
                setDataDirectory();
            }
        } catch (NullPointerException n) {

            AlertBox.display("Null Pointer Error",
                    "The program faced a Null Pointer error. \n The program will close.",
                    "Close Program");
            System.exit(0);

        } catch (Exception e) {
            System.out.println(e);
            setDataDirectory();
        }

        System.out.println("Trying Chosen directory: " + dataDirectory);
    }

    /**
     * This method sets the cell values and sets the items in Table view
     */
    private void showData(ArrayList<TestFile> tFilesHam, ArrayList<TestFile> tFilesSpam) {
        filenameColumn.setCellValueFactory(new PropertyValueFactory<TestFile, String>("filename"));
//        spamProbabilityColumn.setCellValueFactory(new PropertyValueFactory<TestFile, Double>("spamProbability"));
        actualClassColumn.setCellValueFactory(new PropertyValueFactory<TestFile, String>("actualClass"));
        spamProbabilityColumn.setCellValueFactory(new PropertyValueFactory<TestFile, String>("spamProbability"));


        tableView.setItems(DataSource.getAllFiles(tFilesHam, tFilesSpam));
    }
}

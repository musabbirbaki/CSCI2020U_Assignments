package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class DataSource {
    public static ObservableList<TestFile> getAllFiles(ArrayList<TestFile> tFilesHam, ArrayList<TestFile> tFilesSpam){
        ObservableList<TestFile> files = FXCollections.observableArrayList();

        //Printing tfiles
        for(TestFile t: tFilesSpam){
            files.add(t);
        }
        for(TestFile t: tFilesHam){
            files.add(t);
        }

        return files;
    }
}

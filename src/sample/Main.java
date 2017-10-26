package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.FileIO.FileIO;

public class Main extends Application {
    //TODO:изменение даты и графики
    //счета
    @Override
    public void start(Stage primaryStage) throws Exception{
        FileIO files = new FileIO("cache.txt");
        files.Init();
        Parent root = FXMLLoader.load(getClass().getResource("Forms/MainForm.fxml"));
        primaryStage.setTitle("BudgetApp");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();



    }


    public static void main(String[] args) {
        launch(args);
    }
}
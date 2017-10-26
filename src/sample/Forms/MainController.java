package sample.Forms;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainController {
    @FXML private Button btnIncome;
    @FXML private Button btnOutcome;
    @FXML private Button btnAccounts;
    @FXML private Button btnSettings;
    public void initialize() throws IOException {}


    private  Common common = new Common();

    public void  btnIncome_click() throws IOException {
        try {
            common.ShowForm("IncomeForm.fxml");
        }
        catch(Exception ex){
            common.ShowMessage(ex.getMessage());
        }
    }
    public void btnAccounts_click()throws IOException{
        try {
            common.ShowForm("AccountsForm.fxml");
        }
        catch(Exception ex){
            common.ShowMessage(ex.getMessage());
        }
    }
    public void btnOutCome_click()throws IOException{
        try {
            common.ShowForm("OutcomeForm.fxml");
        }
        catch(Exception ex){
            common.ShowMessage(ex.getMessage());
        }
    }

    public void btnSettings_click(){
        try {
            common.ShowForm("Settings.fxml");
        }
        catch(Exception ex){
            common.ShowMessage(ex.getMessage());
        }
    }
}
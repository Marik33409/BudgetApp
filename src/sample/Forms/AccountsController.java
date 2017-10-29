package sample.Forms;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by mark on 17.09.17.
 */

public class AccountsController implements Initializable {
    @FXML
    Button AddAcc;

    private  Common common = new Common();

    public void  btnAdd_click() throws IOException {
        try {
            common.ShowForm("AddAccountsForm.fxml");
        }
        catch(Exception ex){
            common.ShowMessage(ex.getMessage());
        }
    }







    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
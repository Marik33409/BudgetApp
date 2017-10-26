package sample.Forms;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import sample.FileIO.FileIO;

/**
 * Created by mark on 17.10.2017.
 */
public class SettingsFormController {
    @FXML
    private Button btnDoDefault;



    public void btnDoDefault_click(){
        Common common = new Common();
        try {
            FileIO io = new FileIO("cache.txt");
            io.DoDefault();
            io.SaveAll();
        } catch(Exception ex){
            common.ShowMessage(ex.getMessage());
        }
    }


}

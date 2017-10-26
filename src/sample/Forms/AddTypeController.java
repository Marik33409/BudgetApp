package sample.Forms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.FileIO.FileIO;
import sample.Models.TType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 30.09.2017.
 */
public class AddTypeController {
    @FXML
    Button addBtn;
    @FXML
    private javafx.scene.control.TextField txbValue;
    //-1 - none; 1-income ; 2-outcome
    private int type = -1;

    public void setType(int _type) {
        this.type = _type;
    }

    FileIO fileIO = new FileIO("cache.txt");
    Common common = new Common();

    private void AddOutcomeType() throws Exception {

        if (fileIO.outomeTypes.size() == 0)
            fileIO.outomeTypes.add(new TType(txbValue.getText()));
        else
            fileIO.outomeTypes.add(new TType(fileIO.outomeTypes.get(fileIO.outomeTypes.size() - 1).GetId() + 1, txbValue.getText()));
        fileIO.SaveAll();
        Stage stage = (Stage) addBtn.getScene().getWindow();
        stage.close();

    }

    private void AddIncomeTypes() throws Exception {

        if (fileIO.incomeTypes.size() == 0)
            fileIO.incomeTypes.add(new TType(txbValue.getText()));
        else
            fileIO.incomeTypes.add(new TType(fileIO.incomeTypes.get(fileIO.incomeTypes.size() - 1).GetId() + 1, txbValue.getText()));
        fileIO.SaveAll();
        Stage stage = (Stage) addBtn.getScene().getWindow();
        stage.close();

    }

    public void btnAdd_click() throws Exception {
        try {
            if (txbValue.getText().length() > 0) {
                switch (type){
                    case 1:
                        AddIncomeTypes();
                        break;
                    case 2:
                        AddOutcomeType();
                        break;
                    default:
                        break;
                }

            } else {
                throw new Exception("Название не может быть пустой строкой!");
            }
        } catch (Exception ex) {
            common.ShowMessage(ex.getMessage());
        }
    }
}

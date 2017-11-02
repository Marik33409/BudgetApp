package sample.Forms;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sample.FileIO.FileIO;
import sample.Models.Item;
import sample.Models.TType;
import javafx.scene.control.*;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by mark on 17.09.17.
 */

public class AccountsController implements Initializable {
    //<editor-fold desc="Controls">
    @FXML
    Button btnAdd;
    @FXML
    Button btnSubVal;
    @FXML
    Button btnAddVal;
    @FXML
    TextField txbName;
    @FXML
    TextField txbValue;
    @FXML
    TextField txbIncome;
    @FXML
    TextField txbOutcome;
    @FXML
    ListView lstAcc;
    //</editor-fold>

    //TODO: доделать работу со счетами
    //TODO:  повесить функции на кнопки
    //<editor-fold desc="Fields">
    private Common common = new Common();
    private FileIO io = new FileIO(common.GetFileName());
    //</editor-fold>

    //<editor-fold desc="Методы">
    private void Open() {
        try {
            io.OpenAccounts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FillList(){
        Open();
        common.FillLst(lstAcc,io.accounts);
    }
    //</editor-fold>


    public void btnAdd_click() {
        double val = Double.parseDouble(txbValue.getText());
        io.accounts.add(new Item(io.accounts.size() + 1, val, new Date(),new TType(1, "Банковский счет")));
        try {
            io.SaveAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FillList();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FillList();
    }
}
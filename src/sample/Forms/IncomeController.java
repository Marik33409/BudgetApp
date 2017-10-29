package sample.Forms;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import sample.FileIO.FileIO;
import sample.Models.Item;
import sample.Models.TType;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


//TODO доделать еласс FilIO для хранения всей информации

/**
 * Created by mark on 17.09.17.
 */
public class IncomeController implements Initializable {
    //<editor-fold desc="Элементы управления">
    @FXML
    private TextField txbValue;
    @FXML
    private ListView lstItems;
    @FXML
    private ComboBox cmbTypes;
    @FXML
    private Button btnTypeDelete;
    @FXML
    private DatePicker datePicker;
    //</editor-fold>

    //<editor-fold desc="Поля">
    private FileIO fileIO = new FileIO("cache.txt");
    private Common common = new Common();
    //</editor-fold>

    //<editor-fold desc="Методы">
    public void Load() {
        try {
            fileIO.Init();
            LoadItems();
        } catch (Exception ex) {
            common.ShowMessage(ex.getMessage());
        }
    }
    private static final LocalDate LOCAL_DATE (String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }


    private void AddValue() {
        try {
            double val = Double.parseDouble(txbValue.getText());
            TType type = (TType) cmbTypes.getValue();
            if (type == null || type.GetId() == fileIO.incomeTypes.size())
                throw new Exception("Выберите категорию");
            if (datePicker.getValue()==null)
                throw  new Exception("Выберите дату");

            System.out.println(datePicker.getValue());
            Date date = Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            //2017-10-30 need to convert->30.10.2017
            //yy-mm-dd -> dd.mm.yy
            //DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            //Date date = format.parse(datePicker.getValue());

           //  Date date=new Date();
            fileIO.incomes.add(new Item(fileIO.incomes.size() + 1, val, date, type));
            LoadItems();
            fileIO.SaveAll();
        } catch (Exception ex) {
            common.ShowMessage(ex.getMessage());
        }
    }

    private void DeleteValue() {
        try {
            Item item = (Item) lstItems.getSelectionModel().getSelectedItem();
            fileIO.incomes.remove(item);
            common.FillLst(lstItems, fileIO.incomes);
            fileIO.SaveAll();
        } catch (Exception ex) {
            common.ShowMessage(ex.getMessage());
        }
    }

    private void LoadItems() {
        try {
            common.FillLst(lstItems, fileIO.incomes);
        } catch (Exception ex) {
            common.ShowMessage(ex.getMessage());
        }
    }

    private void DeleteType() {
        TType type = (TType) cmbTypes.getValue();
        try {
            fileIO.incomeTypes.remove(type);
            int i = 0;
            for (TType tp : fileIO.incomeTypes) {
                tp.setId(i);
                i++;
            }
            fileIO.SaveAll();
            cmbTypes.setValue(null);
        } catch (Exception ex) {
            common.ShowMessage(ex.getMessage());
        }
    }

    private void LoadTypes() {
        try {
            fileIO.OpenTypes();
            common.FillCmb(cmbTypes, fileIO.types.get("incomeTypes"));
        } catch (Exception ex) {
            common.ShowMessage(ex.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="События">
    public void cmb_OnShow() throws Exception {
        LoadTypes();
    }

    public void btnAdd_click() {
        AddValue();
    }

    public void btnDelete_click() {
        DeleteValue();
    }

    public void btnTypeDelete_click() {
        DeleteType();
    }

    public void btnDiagrams_click() throws Exception {
        fileIO.SetTemp("income");
        fileIO.SaveAll();
        common.ShowForm("ChartsForm.fxml");
        //common.ShowChartForm("ChartsForm.fxml", fileIO.incomes,1);
    }

    public void datePicker_clicked() {

    }

    //</editor-fold>
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Load();
//        System.out.println(LocalDateTime);
        //Date date=new Date();
        datePicker.setValue(LocalDate.now());

        cmbTypes.valueProperty().addListener(new ChangeListener<TType>() {
            @Override
            public void changed(ObservableValue ov, TType t, TType t1) {
                TType type = (TType) cmbTypes.getValue();
                if (type != null && type.GetId() == fileIO.incomeTypes.size()) {
                    try {
                        common.ShowAddTypeForm("AddTypeForm.fxml", 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
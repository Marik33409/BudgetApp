package sample.Forms;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import sample.FileIO.FileIO;
import sample.Models.Item;

import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;


/**
 * Created by mark on 05.10.2017.
 */
public class ChartsController implements Initializable {
    @FXML
    AnchorPane pane;
    @FXML
    BarChart bchartDay;
    @FXML
    PieChart chartDay;
    @FXML
    PieChart chartMonth;
    @FXML
    PieChart chartYear;
    @FXML
    DatePicker datePicker;
    @FXML
    ChoiceBox chbMonth;
    @FXML
    ChoiceBox chbYear;
    @FXML
    ChoiceBox chbYear2;

    List<String> colors = new ArrayList<>();

    //<editor-fold desc="Fields">
    private CategoryAxis xAxis = new CategoryAxis();
    private NumberAxis yAxis = new NumberAxis();
    private int type = -1;
    private ArrayList<Item> items = new ArrayList<Item>();
    private FileIO io = new FileIO("cache.txt");
    //</editor-fold>

    //<editor-fold desc="Methods">

    /**
     * @param _items
     */
    public void SetItems(ArrayList<Item> _items) {
        this.items = _items;
    }

    /**
     * @param _type
     */
    public void SetType(int _type) {
        this.type = _type;
    }

    /**
     * @param date
     * @return
     */
    public LocalDate ConvertToLDate(Date date) {
        LocalDate res = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return res;
    }

    /**
     * @param dict
     * @return
     */
    public ObservableList<PieChart.Data> ConvertToChartData(Map<String, Item> dict) {
        ObservableList<PieChart.Data> res = FXCollections.observableArrayList();
        for (String key : dict.keySet())
            res.addAll(new PieChart.Data(dict.get(key).type.GetName() + "\n" + dict.get(key).value, dict.get(key).value));
        return res;
    }

    public void SyncColors(BarChart barChart, PieChart pieChart) {
        ArrayList<String> styles = new ArrayList<>();
        for (int i = 0; i < pieChart.getData().size(); i++) {
            styles.add(pieChart.getData().get(i).getNode().getStyle());
        }
        int i = 0;
        for (Object item : barChart.getData()) {
            ((XYChart.Series<String, Number>) item).getNode().setStyle(styles.get(i++));
        }
    }

    /**
     * Заполняет диаграммы
     */
    public void FillChart() {
        //ПОСТРОЕНИЕ КРУГОВОЙ ДИАГРАММЫ
        chartDay.getData().clear();
        Map<String, Item> dict = new HashMap<String, Item>();
        for (Item item : items) {
            if (datePicker.getValue() != null && datePicker.getValue().equals(ConvertToLDate(item.date))) {

                chartDay.setData(ConvertToChartData(dict));
                if (dict.get(item.type.GetName()) == null)
                    dict.put(item.type.GetName(), item);
                else
                    dict.get(item.type.GetName()).value += item.value;
            }
        }
        chartDay.setData(ConvertToChartData(dict));

        ArrayList<String> styles = new ArrayList<>();
        for (int i = 0; i < chartDay.getData().size(); i++) {
            styles.add(chartDay.getData().get(i).getNode().getStyle());
        }
        //ПОСТРОЕНИЕ СТОЛБЧАТОЙ ДИАГРАММЫ
        bchartDay.getData().clear();
        bchartDay.setTitle("Доходы");
        bchartDay.setBarGap(0);
        bchartDay.setCategoryGap(0);
        xAxis.setLabel("Value");
        yAxis.setLabel("Income");
        int i=0;
        for (String key : dict.keySet()) {
            XYChart.Series series = new XYChart.Series();
            XYChart.Data<String, Number> data = new XYChart.Data(dict.get(key).type.GetName(), dict.get(key).value);
            series.getData().add(data);

            bchartDay.getData().add(series);
            //брать цвета у круговой диаграммы
            data.getNode().setStyle(styles.get(i++));
        }


        //SyncColors(bchartDay,chartDay);


        //ПОСТРОЕНИЕ КРУГОВОЙ ДИАГРАММЫ НА МЕСЯЦ
        dict = new HashMap<String, Item>();
        for (Item item : items) {
            int month = Integer.valueOf((String) chbMonth.getValue());
            int year = Integer.valueOf((String) chbYear.getValue());
            if (ConvertToLDate(item.date).getMonthValue() == month && ConvertToLDate(item.date).getYear() == year) {
                if (dict.get(item.type.GetName()) == null)
                    dict.put(item.type.GetName(), item);
                else
                    dict.get(item.type.GetName()).value += item.value;
            }
        }
        chartMonth.setData(ConvertToChartData(dict));

        //ПОСТРОЕНИЕ КРУГОВОЙ ДИАГРАММЫ НА ГОД
        dict = new HashMap<String, Item>();
        for (Item item : items) {
            int year = Integer.valueOf((String) chbYear2.getValue());
            if (ConvertToLDate(item.date).getYear() == year) {
                if (dict.get(item.type.GetName()) == null)
                    dict.put(item.type.GetName(), item);
                else
                    dict.get(item.type.GetName()).value += item.value;
            }
        }
        chartYear.setData(ConvertToChartData(dict));
    }

    /**
     * УСТАНОВКА ДАТЫ НА МЕСЯЧНЫЕ ДИАГРАММЫ
     */
    public void FillChoiseBoxes() {
        ObservableList<String>
                months = FXCollections.observableArrayList(),
                years = FXCollections.observableArrayList();
        for (int i = 0; i < 12; i++)
            months.add(Integer.toString(i + 1));
        for (int i = 2017; i < 2020; i++)
            years.add(Integer.toString(i));
        chbMonth.setItems(months);
        chbMonth.setValue(months.get(0));
        chbYear.setItems(years);
        chbYear.setValue(years.get(0));
        chbYear2.setItems(years);
        chbYear2.setValue(years.get(0));

        chbMonth.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                int index = chbMonth.getSelectionModel().getSelectedIndex();
                chbMonth.getSelectionModel().select(index);
                FillChart();
            }
        });

        chbYear.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                int index = chbYear.getSelectionModel().getSelectedIndex();
                chbYear.getSelectionModel().select(index);
                FillChart();
            }
        });

        chbYear2.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                int index = chbYear2.getSelectionModel().getSelectedIndex();
                chbYear2.getSelectionModel().select(index);
                FillChart();
            }
        });

    }
    //</editor-fold>

    //<editor-fold desc="Events">
    public void datePicker_clicked() {
        FillChart();
    }

    public void chbMonth_clicked() {
        FillChart();
    }

    public void chbYear_clicked() {
        FillChart();
    }

    public void chbYear2_clicked() {
        FillChart();
    }
    //</editor-fold>

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //pane.getStylesheets().add(ChartsController.class.getResource("style.css").toExternalForm());
            if (io.GetTemp().equals("income")) {
                items = io.incomes;
            } else if (io.GetTemp().equals("outcome")) {
                items = io.outcomes;
            }
            datePicker.setValue(ConvertToLDate(new Date()));
            FillChoiseBoxes();
            FillChart();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

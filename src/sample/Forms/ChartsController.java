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

import static com.sun.javafx.tools.resource.DeployResource.Type.data;

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

    private CategoryAxis xAxis = new CategoryAxis();
    private NumberAxis yAxis = new NumberAxis();
    private int type = -1;
    private ArrayList<Item> items = new ArrayList<Item>();
    private FileIO io = new FileIO("cache.txt");

    public void SetItems(ArrayList<Item> _items) {
        this.items = _items;
    }

    public void SetType(int _type) {
        this.type = _type;
    }

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

    public LocalDate ConvertToLDate(Date date) {
        LocalDate res = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return res;
    }

    public ObservableList<PieChart.Data> ConvertToChartData(Map<String, Item> dict) {
        ObservableList<PieChart.Data> res = FXCollections.observableArrayList();
        for (String key : dict.keySet())
            res.addAll(new PieChart.Data(dict.get(key).type.GetName() + "\n" + dict.get(key).value, dict.get(key).value));
        return res;
    }


    public void FillChart() {
        colors.add("-fx-bar-fill: aqua;");
        colors.add("-fx-bar-fill: chartreuse;");
        colors.add("-fx-bar-fill: crimson;");
        colors.add("-fx-bar-fill: gold;");
        colors.add("-fx-bar-fill: fuchsia;");
        colors.add("-fx-bar-fill: green;");
        colors.add("-fx-bar-fill: deepskyblue;");
        colors.add("-fx-bar-fill: saddlebrown;");
        colors.add("-fx-bar-fill: slategrey;");
        colors.add("-fx-bar-fill: teal;");
        colors.add("-fx-bar-fill: chocolate;");

        //ПОСТРОЕНИЕ КРУГОВОЙ ДИАГРАММЫ
        chartDay.getData().clear();
        Map<String, Item> dict = new HashMap<String, Item>();
        for (Item item : items) {
            if (datePicker.getValue() != null && datePicker.getValue().equals(ConvertToLDate(item.date))) {
                if (dict.get(item.type.GetName()) == null)
                    dict.put(item.type.GetName(), item);
                else
                    dict.get(item.type.GetName()).value += item.value;
            }
        }
        chartDay.setData(ConvertToChartData(dict));
        for (int i = 0; i < dict.size(); i++) {
            chartDay.getData().get(i).getNode().setStyle(colors.get(i));
        }

        //ПОСТРОЕНИЕ СТОЛБЧАТОЙ ДИАГРАММЫ
//       BarChart<String,Number> bc = new BarChart(xAxis,yAxis);
        bchartDay.getData().clear();
        bchartDay.setTitle("Доходы");
        bchartDay.setBarGap(0);
        bchartDay.setCategoryGap(0);
        xAxis.setLabel("Value");
        yAxis.setLabel("Income");
        int i = 0;
        for (String key : dict.keySet()) {
            XYChart.Series series = new XYChart.Series();
            XYChart.Data<String, Number> data = new XYChart.Data<>(dict.get(key).type.GetName(), dict.get(key).value);
            //data.getNode().getStyleClass().add("-fx-bar-fill: aqua;");
            i++;
            series.getData().add(data);
            series.getData().add(new XYChart.Data(dict.get(key).type.GetName(), dict.get(key).value));
            bchartDay.getData().add(series);
        }






//        for (int i = 0; i < bchartDay.size(); i++) {
//            for (Node node : chart.lookupAll(".series" + i)) {
//                node.getStyleClass().remove("default-color" + (i));
//                node.getStyleClass().add("default-color" + (i ));
//            }
//        }
//      int i = 0;
//        for (Node node: bchartDay.getData()){
//            for (XYChart.Data<String, Number> item: (XYChart.Series)(node.getData())){
//                item.getNode().setStyle(colors.get(i));
//                i++;
//            }
//        }
//        for (int i = 0; i < serie.size(); i++) {
//            chartDay.getData().get(i).getNode().setStyle(colors.get(i));
//        }


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
//УСТАНОВКА ДАТЫ НА МЕСЯЧНЫЕ ДИАГРАММЫ
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
           // pane.getStylesheets().add(ChartsController.class.getResource("style.css").toExternalForm());
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

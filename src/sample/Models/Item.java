package sample.Models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.DoubleSummaryStatistics;

/**
 * Created by mark on 17.09.17.
 */

//enum Type {grocery,food,clothes,gaz,car,home,holiday,mobile,internet,other}

public class Item {
    int id;
    String name;
    public double value;
    public Date date;
    public TType type;

    public Item() {

    }

    public Item(String values[]) {
        try {
//            if (values.length < 5)
//                throw new Exception("Wrong values count");
            //"%d|%s|%f|%s|%s \n",id,name,value,date,type
            id = Integer.parseInt(values[0]);
            name = values[1];
            value = Double.parseDouble(values[2]);
            date = ConvertDate(values[3]);
            type = new TType(values[4]);
        } catch (Exception ex) {
            new Item();
        }
    }

    public Item(int _id, String _name, double _value, Date _date, TType _type) {
        id = _id;
        name = _name;
        value = _value;
        date = _date;
        type = _type;
    }

    public Item(int _id, double _value, Date _date, TType _type) {
        id = _id;
        name = "none";
        value = _value;
        date = _date;
        type = _type;
    }

    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    private String ConvertDate(Date date) {
        return format.format(date);
    }

    private Date ConvertDate(String str) throws Exception {
        return format.parse(str);
    }

    public String toStr() {
        String valueStr = String.format("%.2f", value).replace(',', '.');
        return String.format("%d;%s;%s;%s;%s\n", id, name, valueStr, ConvertDate(date), type);
    }

    @Override
    public String toString() {
        String valueStr = String.format("%.2f", value).replace(',', '.');
        return String.format("сумма:%s дата:%s категория:%s", valueStr, ConvertDate(date), type);
    }
}
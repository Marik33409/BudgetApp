package sample.FileIO;

import sample.Models.Item;
import sample.Models.TType;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mark on 24.09.2017.
 */
public class FileIO {


    //<editor-fold desc="Поля">
    private String fileName;
    private Boolean byDefault;
    //переменная для передачи данных между формами
    private String temp;
    public ArrayList<TType> incomeTypes, outomeTypes;
    public ArrayList<Item> incomes, outcomes;
    public HashMap<String, ArrayList<TType>> types;
    public HashMap<String, ArrayList<Item>> items;

    //</editor-fold>
    //<editor-fold desc="Конструкторы">
    public FileIO() {
        try {
            Init(this.fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //        items.entrySet("incomes") ;
        //        items.get("outcomes");
        //types.entrySet(new Map.Entry("incomeTypes", new ArrayList<TType>()));
        //types.get("outcomeTypes");
    }

    public FileIO(String _fileName) {
        try {
            Init(_fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //</editor-fold>
    //<editor-fold desc="Методы">

    public void Init() throws Exception {
        if (this.fileName != null)
            Init(this.fileName);
    }

    public void Init(String _fileName) throws Exception {
        fileName = _fileName;
        incomes = new ArrayList<>();
        outcomes = new ArrayList<>();
        incomeTypes = new ArrayList<>();
        outomeTypes = new ArrayList<>();
        types = new HashMap<String, ArrayList<TType>>() {{
            put("incomeTypes", incomeTypes);
            put("outcomeTypes", outomeTypes);
        }};
        items = new HashMap<String, ArrayList<Item>>() {{
            put("incomes", incomes);
            put("outcomes", outcomes);
        }};
        if (_fileName != null) {
            File f = new File(_fileName);
            if (f.exists() && !f.isDirectory()) {
                OpenAll();
            } else {
                SaveAll();
            }
        }
    }

    public void OpenAll() throws Exception {
        OpenAll(this.fileName);
    }

    public void OpenAll(String _fileName) throws Exception {
        FileReader reader = new FileReader(_fileName);
        BufferedReader bufferReader = new BufferedReader(reader);
        String line = "";
        String name = "";

        while ((line = bufferReader.readLine()) != null) {
            //=== разделители отделы
            if (line.contains("=")) {
                name = line.replace("=", "");
            } else {
                if (name.equals("temp")) {
                    this.temp = line;
                }
                if (name.equals("firstime")) {
                    if (Boolean.parseBoolean(line) == true) {
                        byDefault = false;
                        CreateTypes();
                        SaveAll();
                    }
                }
                if (items.get(name) != null) {
                    //"%d;%s;%f;%s;%s \n",id,name,value,date,type
                    String values[] = line.split(";");
                    Item temp = new Item(values);
                    items.get(name).add(new Item(values));
                }
                if (types.get(name) != null) {
                    String values[] = line.split(";");
                    Item temp = new Item(values);
                    types.get(name).add(new TType(values));
                }
            }
        }
        reader.close();
        bufferReader.close();
    }

    private void CreateTypes() {
        String inComeNames[] = {"Зарплата", "Бизнес", "Дивиденды", "Подарки", "Подработка"};
        String outComeNames[] = {"Продукты", "Обеды", "Одежда", "Спорт", "Бензин", "Авто", "Дом", "Досуг", "Связь", "Здоровье"};
        for (int i = 0; i < inComeNames.length; i++)
            incomeTypes.add(new TType(i, inComeNames[i]));
        for (int i = 0; i < outComeNames.length; i++)
            outomeTypes.add(new TType(i, outComeNames[i]));
    }

    public void OpenTypes() throws Exception {
        OpenTypes(this.fileName);
    }

    public void OpenTypes(String _fileName) throws Exception {
        FileReader reader = new FileReader(_fileName);
        BufferedReader bufferReader = new BufferedReader(reader);
        String line = "";
        String name = "";
        incomeTypes = new ArrayList<>();
        outomeTypes = new ArrayList<>();
        types = new HashMap<String, ArrayList<TType>>() {{
            put("incomeTypes", incomeTypes);
            put("outcomeTypes", outomeTypes);
        }};
        while ((line = bufferReader.readLine()) != null) {
            //=== разделители отделы
            if (line.contains("=")) {
                name = line.replace("=", "");
            } else {
                if (types.get(name) != null) {
                    String values[] = line.split(";");
                    Item temp = new Item(values);
                    types.get(name).add(new TType(values));
                }
            }
        }
        reader.close();
        bufferReader.close();
    }

    public void OpenItems() throws Exception {
        OpenItems(this.fileName);
    }

    public void OpenItems(String _fileName) throws Exception {
        FileReader reader = new FileReader(_fileName);
        BufferedReader bufferReader = new BufferedReader(reader);
        String line = "";
        String name = "";
        while ((line = bufferReader.readLine()) != null) {
            //=== разделители отделы
            if (line.contains("=")) {
                name = line.replace("=", "");
            } else {
                if (items.get(name) != null) {
                    //"%d;%s;%f;%s;%s \n",id,name,value,date,type
                    String values[] = line.split(";");
                    Item temp = new Item(values);
                    items.get(name).add(new Item(values));
                }
            }
        }
        reader.close();
        bufferReader.close();
    }

    public void SaveAll() throws Exception {
        SaveAll(this.fileName);
    }

    public void SaveAll(String _fileName) throws Exception {
        File f = new File(_fileName);
        ArrayList<String> names = new ArrayList<String>() {{
            add("===temp");
            add("===firstime");
            add("===incomes");
            add("===outcomes");
            add("===incomeTypes");
            add("===outcomeTypes");
        }};
        BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(_fileName), "UTF-8"));
        for (String name : names) {
            bufferWriter.write(name);
            bufferWriter.newLine();
            String currName = name.replace("=", "");
            if (currName.equals("temp")) {
                if (this.temp == null)
                    bufferWriter.write("" + "\n");
                else
                    bufferWriter.write(this.temp + "\n");
            }
            if (currName.equals("firstime")) {
                if (this.byDefault == null)
                    bufferWriter.write(String.valueOf(false) + "\n");
                else
                    bufferWriter.write(String.valueOf(this.byDefault) + "\n");
            }
            if (items.get(currName) != null) {
                for (Item item : items.get(currName))
                    bufferWriter.write(item.toStr());
            }
            if (types.get(currName) != null) {
                for (TType item : types.get(currName))
                    bufferWriter.write(item.toStr());
            }
        }
        bufferWriter.close();
    }

    public void DoDefault() {
        this.byDefault = true;
    }

    public void SetTemp(String value) {
        this.temp = value;
    }

    public String GetTemp() {
        return this.temp;
    }


    //</editor-fold>
    //<editor-fold desc="Старые методы">
//    public List<Item> Open(String _fileName) throws Exception {
//        List<Item> items = new ArrayList<Item>();
//        FileReader reader = new FileReader(_fileName);
//        BufferedReader bufferReader = new BufferedReader(reader);
//        String line = "";
//        while ((line = bufferReader.readLine()) != null) {
//            //"%d|%s|%f|%s|%s \n",id,name,value,date,type
//            String values[] = line.split(";");
//            Item temp = new Item(values);
//            items.add(new Item(values));
//        }
//        return items;
//    }
//
//    public List<TType> OpenTypes(String _fileName) throws Exception {
//        List<TType> items = new ArrayList<TType>();
//        FileReader reader = new FileReader(_fileName);
//        BufferedReader bufferReader = new BufferedReader(reader);
//        String line = "";
//        while ((line = bufferReader.readLine()) != null) {
//            //"%d|%s|%f|%s|%s \n",id,name,value,date,type
//            String values[] = line.split(";");
//            Item temp = new Item(values);
//            items.add(new TType(values));
//        }
//        return items;
//    }
//
//    public List<TType> OpenTypes() throws Exception {
//        return OpenTypes(this.fileName);
//    }
//
//    public List<Item> Open() throws Exception {
//        return Open(fileName);
//    }
//
//    public void Save(String _fileName, List<Item> items) throws Exception {
//
//        BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
//        for (Item item : items)
//            bufferWriter.write(item.toStr());
//        bufferWriter.close();
//    }
//
//    public void Save(List<Item> items) throws Exception {
//        Save(fileName, items);
//    }
//
//    public void SaveTypes(String _fileName, List<TType> items) throws Exception {
//        BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
//        for (TType item : items)
//            bufferWriter.write(item.toStr());
//        bufferWriter.close();
//    }
//
//    public void SaveTypes(List<TType> items) throws Exception {
//        SaveTypes(this.fileName, items);
//    }
    //</editor-fold>
}

package sample.FileIO;

import sample.Models.Item;
import sample.Models.TType;

import java.io.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mark on 24.09.2017.
 */
public class FileIO {
    //<editor-fold desc="Поля">
    private String fileName;
    /**
     * восстановить значения по умолчанию
     */
    private Boolean byDefault;
    /**
     * переменная для передачи данных между формами
     */
    private String temp;
    public ArrayList<TType> incomeTypes, outomeTypes;
    public ArrayList<Item> incomes, outcomes, accounts;
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

    //<editor-fold desc="Clear">

    /**
     * Очищает только типы
     */
    private void ClearTypes() {
        incomeTypes = new ArrayList<>();
        outomeTypes = new ArrayList<>();
        types = new HashMap<String, ArrayList<TType>>() {{
            put("incomeTypes", incomeTypes);
            put("outcomeTypes", outomeTypes);
        }};
    }

    /**
     * Очищает только доходы и расходы
     */
    private void ClearItems() {
        incomes = new ArrayList<>();
        outcomes = new ArrayList<>();
        accounts = new ArrayList<>();
        items = new HashMap<String, ArrayList<Item>>() {{
            put("incomes", incomes);
            put("outcomes", outcomes);
            put("accounts", accounts);
        }};
    }
    /**
     * Очищает все сохраненные элементы
     */
    private void ClearAll() {
        ClearTypes();
        ClearItems();
    }
    //</editor-fold>

    //<editor-fold desc="Init">

    /**
     * Создает типы для расходов и доходов
     */
    private void CreateTypes() {
        String inComeNames[] = {"Зарплата", "Бизнес", "Дивиденды", "Подарки", "Подработка"};
        String outComeNames[] = {"Продукты", "Обеды", "Одежда", "Спорт", "Бензин", "Авто", "Дом", "Досуг", "Связь", "Здоровье"};
        for (int i = 0; i < inComeNames.length; i++)
            incomeTypes.add(new TType(i, inComeNames[i]));
        for (int i = 0; i < outComeNames.length; i++)
            outomeTypes.add(new TType(i, outComeNames[i]));
    }

    /**
     * Инициирует все элементы @throws Exception
     */
    public void Init() throws Exception {
        if (this.fileName != null)
            Init(this.fileName);
    }

    public void Init(String _fileName) throws Exception {
        fileName = _fileName;
        ClearAll();
        if (_fileName != null) {
            File f = new File(_fileName);
            if (f.exists() && !f.isDirectory()) {
                OpenAll();
            } else {
                SaveAll();
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Open">
    public void OpenAll() throws Exception {
        OpenAll(this.fileName);
    }

    public void OpenAll(String _fileName) throws Exception {
        FileReader reader = new FileReader(_fileName);
        BufferedReader bufferReader = new BufferedReader(reader);
        String line = "", name = "";
        ClearAll();
        while ((line = bufferReader.readLine()) != null) {
            //=== разделители отделов
            if (line.contains("="))
                name = line.replace("=", "");
            else {
                if (name.equals("temp"))
                    this.temp = line;
                if (name.equals("firstime")) {
                    if (Boolean.parseBoolean(line) == true) {
                        byDefault = false;
                        CreateTypes();
                        SaveAll();
                    }
                }
            }
        }
        OpenItems();
        OpenTypes();
        reader.close();
        bufferReader.close();
    }

    public void OpenTypes() throws Exception {
        OpenTypes(this.fileName);
    }

    public void OpenTypes(String _fileName) throws Exception {
        FileReader reader = new FileReader(_fileName);
        BufferedReader bufferReader = new BufferedReader(reader);
        String line = "";
        String name = "";
        ClearTypes();
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
        ClearItems();
        while ((line = bufferReader.readLine()) != null) {
            //=== разделители отделы
            if (line.contains("=")) {
                name = line.replace("=", "");
            } else {
                if (items.get(name) != null) {
                    String values[] = line.split(";");
                    items.get(name).add(new Item(values));
                }
            }
        }
        reader.close();
        bufferReader.close();
    }

    public void OpenAccounts() throws Exception {
        OpenAccounts(this.fileName);
    }

    public void OpenAccounts(String _fileName) throws Exception {
        OpenItems(_fileName);
    }
    //</editor-fold>

    //<editor-fold desc="Save">
    public void SaveAll() throws Exception {
        SaveAll(this.fileName);
    }

    public void SaveAll(String _fileName) throws Exception {
        ArrayList<String> names = new ArrayList<String>() {{
            add("===temp");
            add("===firstime");
            add("===incomes");
            add("===outcomes");
            add("===incomeTypes");
            add("===outcomeTypes");
            add("===accounts");
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
    //</editor-fold>

    //<editor-fold desc="Rest">
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

    //</editor-fold>
}

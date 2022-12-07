package com.company.prodamgarage.customView;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.user.UserChanges;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Field;

public class TableOfChanges extends TableView<Pair<String, String>> {

    public TableOfChanges() {
        setSelectionModel(null);

        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public String translateNameChanges(String val) {
        return switch (val) {
            case "deltaAge" -> "Потеря лет";
            case "deltaCash" -> "Стоимость";
            case "deltaMoneyFlow" -> "Денежный поток";
            case "deltaExpenses" -> "Ежемесячные траты";
            case "deltaAssets" -> "Размер актива";
            case "deltaPassive" -> "Размер пассива";
            case "deltaFreeTime" -> "Требует времени";
            default -> "Неизвестный параметр";
        };
    }

    public String translateVal(Object val) {
        if (val instanceof Number num) {
            if (num.doubleValue() < 0) {
                return val.toString();
            }
            return "+" + val;
        }
        return val.toString();

    }

    public void setData(UserChanges changes) {
        if (changes != null) {
            ObservableList<Pair<String, String>> listOfChanges = FXCollections.observableArrayList();

            try {
                Field[] fields = changes.getClass().getDeclaredFields();
                for (Field f : fields) {
                    String name = f.getName();

                    if (name.contains("delta")) {
                        String val = translateVal(f.get(changes));
                        if (!val.equals("+0")){
                            listOfChanges.add(Pair.create(translateNameChanges(name), val));
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            setItems(listOfChanges);

            TableColumn<Pair<String, String>, String> nameColumn = new TableColumn<>("Параметр");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("key"));

            TableColumn<Pair<String, String>, String> valColumn = new TableColumn<>("Изменения");
            valColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
            // добавляем столбец

            getColumns().clear();
            getColumns().add(nameColumn);
            getColumns().add(valColumn);


            setFixedCellSize(25);
            prefHeightProperty().bind(fixedCellSizeProperty().multiply(Bindings.size(getItems()).add(1.3)));
            minHeightProperty().bind(prefHeightProperty());
            maxHeightProperty().bind(prefHeightProperty());
        }
    }
}

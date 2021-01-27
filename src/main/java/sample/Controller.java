package sample;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import core.DataBase;
import core.KeyValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Controller implements Initializable {
    private final ObservableList<KeyValue> list = FXCollections.observableArrayList();
    private File file;

    @FXML
    ToggleGroup radioSelector;

    @FXML
    TextField searchText;

    @FXML
    private Label fileNameLabel;

    @FXML
    private TableView<KeyValue> tableKeyValue;

    @FXML
    private TableColumn<KeyValue, String> keyName;

    @FXML
    private TableColumn<KeyValue, String> valueName;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        keyName.setCellValueFactory(new PropertyValueFactory<>("keyName"));
        valueName.setCellValueFactory(new PropertyValueFactory<>("valueName"));
        tableKeyValue.setItems(list);
    }

    public void openDatabase(final ActionEvent actionEvent) {
        tableKeyValue.getItems().clear();
        Stage stage;
        String fileName;
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        directoryChooser.setTitle("Select File");
        stage = new Stage();
        file = directoryChooser.showDialog(stage);
        if (file == null) {
            fileNameLabel.setText("No file selected");
            return;
        }
        try {
            final DataBase dataBase = new DataBase(file);
            final ArrayList<KeyValue> data = dataBase.GetData();
            for (final KeyValue keyVal: data) {
                this.list.add(keyVal);
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
        fileName = file.getName();
        fileNameLabel.setText(fileName);
    }

    public void searchValue(final ActionEvent actionEvent) {
        // Selecting value to search
        final RadioButton selectedRadioButton = (RadioButton) radioSelector.getSelectedToggle();
        final String toogleGroupValue = selectedRadioButton.getText();
        boolean valueSearch = false;
        if (toogleGroupValue.equals("Value")) {
            valueSearch = true;
        }
        final String text = searchText.getText();
        final DataBase dataBase = new DataBase(file);
        final ArrayList <KeyValue> data = dataBase.searchData(text, valueSearch);
        tableKeyValue.getItems().clear();
        if (data.size() > 0) {
            this.list.addAll(data);
        }

    }

    public void updateKeyValues(final ActionEvent actionEvent) {
        try {
            tableKeyValue.getItems().clear();
            final DataBase dataBase = new DataBase(file);
            final ArrayList<KeyValue> data = dataBase.GetData();
            for (final KeyValue keyVal: data) {
                this.list.add(keyVal);
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }


}

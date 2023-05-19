import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class CakeTable extends Application {
  private TableView<Cake> table;
  private ObservableList<Cake> data;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    stage.setTitle("Daftar Cake");

    table = new TableView<>();
    data = FXCollections.observableArrayList(getCakeData());

    TableColumn<Cake, Integer> colNo = new TableColumn<>("ID");
    colNo.setCellValueFactory(new PropertyValueFactory<>("idcake"));
    colNo.setCellFactory(column -> new TableCell<Cake, Integer>() {
      @Override
      protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setText(null);
        } else {
          setText(Integer.toString(getIndex() + 1) + ".");
        }
      }
    });

    TableColumn<Cake, String> colName = new TableColumn<>("Name");
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));

    TableColumn<Cake, String> colType = new TableColumn<>("Type");
    colType.setCellValueFactory(new PropertyValueFactory<>("type"));

    TableColumn<Cake, Integer> colPrice = new TableColumn<>("Price");
    colPrice.setCellValueFactory(new PropertyValueFactory<>("priceText"));

    TableColumn<Cake, Void> colUpdate = new TableColumn<>("");
    colUpdate.setCellFactory(column -> new TableCell<Cake, Void>() {
      private final Button updateButton = new Button("Update");

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
          setGraphic(null);
        } else {
          Cake cake = getTableView().getItems().get(getIndex());

          updateButton.setOnAction(event -> {
            CakeForm cakeForm = new CakeForm(cake);
            cakeForm.start(new Stage());
          });

          setGraphic(updateButton);
        }
      }
    });

    TableColumn<Cake, Void> colDelete = new TableColumn<>("");
    colDelete.setCellFactory(column -> new TableCell<Cake, Void>() {
      private final Button deleteButton = new Button("Hapus");

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
          setGraphic(null);
        } else {
          Cake cake = getTableView().getItems().get(getIndex());

          deleteButton.setOnAction(event -> {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Apakah anda yakin ingin menghapus cake ini?",
                ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
              int idcake = cake.getIdcake();
              String condition = "idcake = '" + idcake + "'";
              Database.delete("cake", condition);

              List<Cake> updatedData = getCakeData();
              data.setAll(updatedData);
            }
          });

          setGraphic(deleteButton);
        }
      }
    });

    table.getColumns().addAll(colNo, colName, colType, colPrice, colUpdate, colDelete);
    table.setItems(data);

    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.TOP_CENTER);
    gridPane.setHgap(10);
    gridPane.setVgap(10);

    Hyperlink backLink = new Hyperlink("← Kembali");
    backLink.setOnAction(e -> handleBackLink(stage));

    Label lblTitle = new Label("Daftar Cake");
    lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    GridPane.setHalignment(lblTitle, HPos.LEFT);

    Button btnAddCake = new Button("Tambah Cake Baru");
    btnAddCake.setOnAction(e -> handleAddCakeButton());
    GridPane.setHalignment(btnAddCake, HPos.RIGHT);

    // gridPane.add(backLink, 0, 0);
    gridPane.add(lblTitle, 0, 0);
    gridPane.add(btnAddCake, 1, 0);
    gridPane.add(table, 0, 1, 2, 1);

    VBox vbox = new VBox(backLink, gridPane);
    vbox.setPadding(new Insets(0, 20, 20, 20));

    Scene scene = new Scene(vbox, 400, 300);
    stage.setScene(scene);
    stage.show();
  }

  private ObservableList<Cake> getCakeData() {
    ObservableList<Cake> data = FXCollections.observableArrayList();

    String tableName = "cake";
    String[] columns = { "idcake", "name", "type", "price" };
    ResultSet rs = Database.select(tableName, columns, null);

    try {
      while (rs.next()) {
        int id = rs.getInt("idcake");
        String name = rs.getString("name");
        String type = rs.getString("type");
        int price = rs.getInt("price");

        Cake cake = new Cake(id, name, type, price);
        data.add(cake);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return data;
  }

  private void handleBackLink(Stage stage) {
    App app = new App();
    app.start(stage);
  }

  private void handleAddCakeButton() {
    CakeForm cakeForm = new CakeForm();
    cakeForm.start(new Stage());
  }
}

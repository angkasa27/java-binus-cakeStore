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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionFormBackup extends Application {
  private TableView<Cake> table;
  private ObservableList<Cake> data;
  private ArrayList<TransactionDetails> chart = new ArrayList<>();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    stage.setTitle("Cashier");

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
    colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    colPrice.setCellFactory(column -> new TableCell<Cake, Integer>() {
      @Override
      protected void updateItem(Integer price, boolean empty) {
        super.updateItem(price, empty);
        if (empty || price == null) {
          setText(null);
        } else {
          NumberFormat formatter = new DecimalFormat("#,###");
          setText("Rp" + formatter.format(price));
        }
      }
    });

    TableColumn<Cake, Void> colAdd = new TableColumn<>("");
    colAdd.setCellFactory(column -> new TableCell<Cake, Void>() {
      private final Button addButton = new Button("Tambah");

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
          setGraphic(null);
        } else {
          Cake cake = getTableView().getItems().get(getIndex());
          TransactionDetails newItem = new TransactionDetails(chart.size(), cake.getIdcake(), cake.getName(), 1,
              cake.getPrice());

          chart.add(newItem);

          setGraphic(addButton);
        }
      }
    });

    table.getColumns().addAll(colNo, colName, colType, colPrice, colAdd);
    table.setItems(data);
    table.setStyle("-fx-border-color: transparent; -fx-background-color: transparent");

    Hyperlink backLink = new Hyperlink("← Kembali");
    backLink.setOnAction(e -> handleBackLink(stage));

    VBox itemVbox = new VBox(backLink, table);
    itemVbox.setSpacing(10);

    Label lblCashier = new Label("Keranjang");
    lblCashier.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    VBox formVbox = new VBox(lblCashier);
    formVbox.setPadding(new Insets(20, 0, 0, 20));

    HBox hbox = new HBox(itemVbox, formVbox);
    hbox.setPadding(new Insets(0, 20, 20, 20));

    Scene scene = new Scene(hbox, 800, 400);
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

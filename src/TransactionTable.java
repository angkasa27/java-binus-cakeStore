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
import java.util.List;

public class TransactionTable extends Application {
  private TableView<Transaction> table;
  private ObservableList<Transaction> data;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    stage.setTitle("Riwayat Transaksi");

    table = new TableView<>();
    data = FXCollections.observableArrayList(getTransactionData());

    TableColumn<Transaction, Integer> colTime = new TableColumn<>("time");
    colTime.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

    TableColumn<Transaction, String> colName = new TableColumn<>("cake");
    colName.setCellValueFactory(new PropertyValueFactory<>("cakeName"));

    TableColumn<Transaction, String> colQty = new TableColumn<>("qty");
    colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

    TableColumn<Transaction, String> colPrice = new TableColumn<>("price");
    colPrice.setCellValueFactory(new PropertyValueFactory<>("priceText"));

    TableColumn<Transaction, String> colCustomer = new TableColumn<>("customer");
    colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));

    TableColumn<Transaction, Void> colDelete = new TableColumn<>("");
    colDelete.setCellFactory(column -> new TableCell<Transaction, Void>() {
      private final Button deleteButton = new Button("Hapus");

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
          setGraphic(null);
        } else {
          Transaction transaction = getTableView().getItems().get(getIndex());

          deleteButton.setOnAction(event -> {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Apakah anda yakin ingin menghapus customer ini?",
                ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
              int idtransactionDetails = transaction.getIdtransactionDetails();
              String condition = "idtransactionDetails = '" + idtransactionDetails + "'";
              Database.delete("transactionDetails", condition);

              List<Transaction> updatedData = getTransactionData();
              data.setAll(updatedData);
            }
          });

          setGraphic(deleteButton);
        }
      }
    });

    table.getColumns().addAll(colTime, colName, colQty, colPrice, colCustomer, colDelete);
    table.setItems(data);

    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.TOP_CENTER);
    gridPane.setHgap(10);
    gridPane.setVgap(10);

    Hyperlink backLink = new Hyperlink("← Kembali");
    backLink.setOnAction(e -> handleBackLink(stage));

    Label lblTitle = new Label("Riwayat Transaksi");
    lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    GridPane.setHalignment(lblTitle, HPos.LEFT);

    gridPane.add(lblTitle, 0, 0);
    gridPane.add(table, 0, 1, 2, 1);

    VBox vbox = new VBox(backLink, gridPane);
    vbox.setPadding(new Insets(0, 20, 20, 20));

    Scene scene = new Scene(vbox, 500, 400);
    stage.setScene(scene);
    stage.show();
  }

  private ObservableList<Transaction> getTransactionData() {
    ObservableList<Transaction> data = FXCollections.observableArrayList();

    String tableName = "transactionDetails td JOIN transaction t ON td.transaction_idtransaction = t.idtransaction JOIN cake c ON td.cake_idcake = c.idcake JOIN customer cu ON t.customer_idcustomer = cu.idcustomer";
    String[] columns = {
        "td.idtransactionDetails, t.idtransaction, t.createdAt, c.name AS cake_name, td.qty, td.price, cu.name AS customer_name" };
    ResultSet rs = Database.select(tableName, columns, null);

    try {
      while (rs.next()) {
        int idtransactionDetails = rs.getInt("idtransactionDetails");
        int idtransaction = rs.getInt("idtransaction");
        String createdAt = rs.getString("createdAt");
        String cakeName = rs.getString("cake_name");
        int qty = rs.getInt("qty");
        int price = rs.getInt("price");
        String customerName = rs.getString("customer_name");

        Transaction transaction = new Transaction(idtransactionDetails, idtransaction, createdAt, cakeName, qty, price,
            customerName);
        data.add(transaction);
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
}

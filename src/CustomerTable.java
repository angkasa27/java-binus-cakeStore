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

public class CustomerTable extends Application {
  private TableView<Customer> table;
  private ObservableList<Customer> data;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    stage.setTitle("Daftar Customer");

    table = new TableView<>();
    data = FXCollections.observableArrayList(getCustomerData());

    TableColumn<Customer, Integer> colNo = new TableColumn<>("ID");
    colNo.setCellValueFactory(new PropertyValueFactory<>("idcustomer"));
    colNo.setCellFactory(column -> new TableCell<Customer, Integer>() {
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

    TableColumn<Customer, String> colName = new TableColumn<>("Name");
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));

    TableColumn<Customer, Void> colUpdate = new TableColumn<>("");
    colUpdate.setCellFactory(column -> new TableCell<Customer, Void>() {
      private final Button updateButton = new Button("Edit");

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
          setGraphic(null);
        } else {
          Customer customer = getTableView().getItems().get(getIndex());

          updateButton.setOnAction(event -> {
            CustomerForm customerForm = new CustomerForm(customer);
            customerForm.start(new Stage());
          });

          setGraphic(updateButton);
        }
      }
    });

    TableColumn<Customer, Void> colDelete = new TableColumn<>("");
    colDelete.setCellFactory(column -> new TableCell<Customer, Void>() {
      private final Button deleteButton = new Button("Hapus");

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
          setGraphic(null);
        } else {
          Customer customer = getTableView().getItems().get(getIndex());

          deleteButton.setOnAction(event -> {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Apakah anda yakin ingin menghapus customer ini?",
                ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
              int idcustomer = customer.getIdcustomer();
              String condition = "idcustomer = '" + idcustomer + "'";
              Database.delete("customer", condition);

              List<Customer> updatedData = getCustomerData();
              data.setAll(updatedData);
            }
          });

          setGraphic(deleteButton);
        }
      }
    });

    table.getColumns().addAll(colNo, colName, colUpdate, colDelete);
    table.setItems(data);

    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.TOP_CENTER);
    gridPane.setHgap(10);
    gridPane.setVgap(10);

    Hyperlink backLink = new Hyperlink("← Kembali");
    backLink.setOnAction(e -> handleBackLink(stage));

    Label lblTitle = new Label("Daftar Customer");
    lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    GridPane.setHalignment(lblTitle, HPos.LEFT);

    Button btnAddCustomer = new Button("Tambah Customer Baru");
    btnAddCustomer.setOnAction(e -> handleAddCustomerButton());
    GridPane.setHalignment(btnAddCustomer, HPos.RIGHT);

    // gridPane.add(backLink, 0, 0);
    gridPane.add(lblTitle, 0, 0);
    gridPane.add(btnAddCustomer, 1, 0);
    gridPane.add(table, 0, 1, 2, 1);

    VBox vbox = new VBox(backLink, gridPane);
    vbox.setPadding(new Insets(0, 20, 20, 20));

    Scene scene = new Scene(vbox, 400, 300);
    stage.setScene(scene);
    stage.show();
  }

  private ObservableList<Customer> getCustomerData() {
    ObservableList<Customer> data = FXCollections.observableArrayList();

    String tableName = "customer";
    String[] columns = { "idcustomer", "name" };
    ResultSet rs = Database.select(tableName, columns, null);

    try {
      while (rs.next()) {
        int id = rs.getInt("idcustomer");
        String name = rs.getString("name");

        Customer customer = new Customer(id, name);
        data.add(customer);
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

  private void handleAddCustomerButton() {
    CustomerForm customerForm = new CustomerForm();
    customerForm.start(new Stage());
  }
}

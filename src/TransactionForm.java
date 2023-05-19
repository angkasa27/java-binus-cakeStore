import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TransactionForm extends Application {
  private ObservableList<TransactionDetails> data = FXCollections.observableArrayList();

  private ArrayList<Customer> customers = new ArrayList<>();
  private ArrayList<Cake> cakes = new ArrayList<>();

  ComboBox<Customer> dropdownCustomer = new ComboBox<>();

  private Label totalPriceLabel = new Label("Rp0");
  private int totalPrice;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    stage.setTitle("Kasir");

    Hyperlink backLink = new Hyperlink("← Kembali");
    backLink.setOnAction(e -> handleBackLink(stage));

    updateCustomerList();
    updateCakeList();

    dropdownCustomer.setItems(FXCollections.observableArrayList(customers));
    dropdownCustomer.setPromptText("Pilih customer");

    dropdownCustomer.setCellFactory(param -> new ListCell<Customer>() {
      @Override
      protected void updateItem(Customer item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          setText(item.getName());
        }
      }
    });

    dropdownCustomer.setConverter(new StringConverter<Customer>() {
      @Override
      public String toString(Customer customer) {
        if (customer == null) {
          return null;
        } else {
          return customer.getName();
        }
      }

      @Override
      public Customer fromString(String string) {
        return null;
      }
    });

    Label lblTitle = new Label("Keranjang");
    lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    GridPane.setHalignment(lblTitle, HPos.LEFT);

    Button btnAddTransactionDetails = new Button("Tambah");
    btnAddTransactionDetails.setOnAction(e -> PopUpFormTransaction(new Stage()));
    GridPane.setHalignment(btnAddTransactionDetails, HPos.RIGHT);

    TableView<TransactionDetails> table = new TableView<>();

    TableColumn<TransactionDetails, String> colName = new TableColumn<>("Nama");
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colName.setPrefWidth(150);

    TableColumn<TransactionDetails, Integer> colQty = new TableColumn<>("Qty");
    colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    colQty.setPrefWidth(40);

    TableColumn<TransactionDetails, String> colPrice = new TableColumn<>("Harga");
    colPrice.setCellValueFactory(new PropertyValueFactory<>("totalPriceText"));

    TableColumn<TransactionDetails, Void> colDelete = new TableColumn<>("");
    colDelete.setCellFactory(column -> new TableCell<TransactionDetails, Void>() {
      private final Button deleteButton = new Button("Hapus");

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
          setGraphic(null);
        } else {
          TransactionDetails transactionDetails = getTableView().getItems().get(getIndex());
          deleteButton.setOnAction(event -> removeItem(transactionDetails));
          setGraphic(deleteButton);
        }
      }
    });

    table.getColumns().addAll(colName, colQty, colPrice, colDelete);
    table.setItems(data);

    Button btnSaveTransaction = new Button("Simpan");
    btnSaveTransaction.setOnAction(e -> {
      Customer selectedCustomer = dropdownCustomer.getSelectionModel().getSelectedItem();
      String errorMessage = "";

      if (selectedCustomer == null)
        errorMessage += "Customer harus diisi\n";

      if (errorMessage.isEmpty())
        handleSubmitButton(stage, selectedCustomer);
      else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Gagal Menyimpan");
        alert.setHeaderText("Gagal Menyimpan");
        alert.setContentText(errorMessage);
        alert.showAndWait();
        return;
      }
    });
    GridPane.setHalignment(btnSaveTransaction, HPos.RIGHT);

    Button btnReset = new Button("Reset");
    btnReset.setOnAction(e -> resetData());

    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.TOP_CENTER);
    gridPane.setHgap(10);
    gridPane.setVgap(10);

    Label lblPrice = new Label("Total");
    lblPrice.setStyle("-fx-font-weight: bold;");
    GridPane.setHalignment(lblPrice, HPos.LEFT);

    totalPriceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    GridPane.setHalignment(totalPriceLabel, HPos.RIGHT);
    updateTotalPrice();

    gridPane.add(dropdownCustomer, 0, 0);
    gridPane.add(lblTitle, 0, 1);
    gridPane.add(btnAddTransactionDetails, 1, 1);
    gridPane.add(table, 0, 2, 2, 1);
    gridPane.add(lblPrice, 0, 3);
    gridPane.add(totalPriceLabel, 1, 3);
    gridPane.add(btnSaveTransaction, 1, 4);
    gridPane.add(btnReset, 0, 4);

    VBox vbox = new VBox(backLink, gridPane);
    vbox.setPadding(new Insets(0, 20, 20, 20));

    Scene scene = new Scene(vbox, 400, 300);
    stage.setScene(scene);
    stage.show();
  }

  private void handleBackLink(Stage stage) {
    App app = new App();
    app.start(stage);
  }

  private void addItem(Cake item, int qty) {
    boolean isExisting = false;
    int idcake = item.getIdcake();
    for (int i = 0; i < data.size(); i++) {
      TransactionDetails transactionDetails = data.get(i);
      if (transactionDetails.getIdcake() == idcake) {
        transactionDetails.setQty(transactionDetails.getQty() + qty);
        isExisting = true;
        break;
      }
    }

    if (!isExisting) {
      TransactionDetails newTransactionDetails = new TransactionDetails(data.size(), item.getIdcake(),
          item.getName(),
          qty, item.getPrice());
      data.add(newTransactionDetails);
    }

    updateTotalPrice();
  }

  private void removeItem(TransactionDetails item) {
    data.remove(item);
    updateTotalPrice();
  }

  private void resetData() {
    data.clear();
    dropdownCustomer.setValue(null);
    updateTotalPrice();
  }

  private void updateCustomerList() {
    String tableName = "customer";
    String[] columns = { "idcustomer", "name" };
    ResultSet rs = Database.select(tableName, columns, null);

    try {
      while (rs.next()) {
        int id = rs.getInt("idcustomer");
        String name = rs.getString("name");

        customers.add(new Customer(id, name));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  private void updateCakeList() {
    String tableName = "cake";
    String[] columns = { "idcake", "name", "type", "price" };
    ResultSet rs = Database.select(tableName, columns, null);

    try {
      while (rs.next()) {
        int id = rs.getInt("idcake");
        String name = rs.getString("name");
        String type = rs.getString("type");
        int price = rs.getInt("price");

        cakes.add(new Cake(id, name, type, price));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  private void handleSubmitButton(Stage stage, Customer customer) {
    LocalDateTime currentTime = LocalDateTime.now();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDateTime = currentTime.format(formatter);
    int idcustomer = customer.getIdcustomer();

    String[] transactioncolumns = { "createdAt", "customer_idcustomer" };
    Object[] transactionvalues = { formattedDateTime, idcustomer };

    int idtransaction = Database.insert("transaction", transactioncolumns, transactionvalues);

    String[] columns = { "qty", "price", "transaction_idtransaction", "cake_idcake" };

    for (int i = 0; i < data.size(); i++) {
      TransactionDetails transactionDetails = data.get(i);
      int qty = transactionDetails.getQty();
      int price = transactionDetails.getTotalPrice();
      int idcake = transactionDetails.getIdcake();
      Object[] values = { qty, price, idtransaction, idcake };
      Database.insert("transactionDetails", columns, values);
    }

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Berhasil Disimpan!");
    alert.setHeaderText("Transaksi berhasil disimpan.");
    alert.showAndWait();
    resetData();
  }

  private void updateTotalPrice() {
    totalPrice = data.stream().mapToInt(TransactionDetails::getTotalPrice).sum();
    if (totalPrice == 0) {
      totalPriceLabel.setText("Rp0");
    } else {
      NumberFormat formatter = new DecimalFormat("#,###");

      totalPriceLabel.setText("Rp" + formatter.format(totalPrice));
    }
  }

  private void PopUpFormTransaction(Stage stage) {
    stage.setTitle("Tambah");

    Label lblSelectCake = new Label("Cake:");

    ComboBox<Cake> dropdown = new ComboBox<>();
    dropdown.setItems(FXCollections.observableArrayList(cakes));
    dropdown.setPromptText("Pilih Cake");

    dropdown.setCellFactory(param -> new ListCell<Cake>() {
      @Override
      protected void updateItem(Cake item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          setText(item.getName());
        }
      }
    });

    dropdown.setConverter(new StringConverter<Cake>() {
      @Override
      public String toString(Cake cake) {
        if (cake == null) {
          return null;
        } else {
          return cake.getName();
        }
      }

      @Override
      public Cake fromString(String string) {
        return null;
      }
    });

    Label lblQty = new Label("Qty:");
    TextField txtQty = new TextField();

    Button btnBack = new Button("Batal");
    btnBack.setOnAction(e -> stage.close());

    Button btnSave = new Button("Tambah");
    btnSave.setOnAction(e -> {
      Cake selectedCake = dropdown.getSelectionModel().getSelectedItem();
      String qtyText = txtQty.getText();
      String errorMessage = "";

      if (selectedCake == null)
        errorMessage += "Cake harus diisi\n";

      int qty = 0;
      if (qtyText.isEmpty())
        errorMessage += "Qty harus diisi\n";
      else if (!qtyText.matches("[0-9]+"))
        errorMessage += "Qty harus berisi angka saja\n";
      else {
        qty = Integer.parseInt(qtyText);
        if (qty < 1)
          errorMessage += "Qty tidak boleh kosong\n";
      }

      if (errorMessage.isEmpty()) {

        addItem(selectedCake, qty);
        stage.close();
      } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Gagal Menyimpan");
        alert.setHeaderText("Gagal Menyimpan");
        alert.setContentText(errorMessage);
        alert.showAndWait();
        return;
      }
    });

    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setHgap(10);
    gridPane.setVgap(10);

    gridPane.add(lblSelectCake, 0, 0);
    gridPane.add(dropdown, 1, 0);
    gridPane.add(lblQty, 0, 1);
    gridPane.add(txtQty, 1, 1);
    gridPane.add(btnBack, 0, 2);
    gridPane.add(btnSave, 1, 2);

    gridPane.setPadding(new Insets(20));

    Scene scene = new Scene(gridPane);
    stage.setScene(scene);
    stage.show();
  }
}

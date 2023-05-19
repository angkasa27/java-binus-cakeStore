import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CustomerForm extends Application {
  private TextField txtName;
  private Customer initialCustomer = new Customer();

  public static void main(String[] args) {
    launch(args);
  }

  public CustomerForm() {
    // Default constructor
  }

  public CustomerForm(Customer initialCustomer) {
    this.initialCustomer = initialCustomer;
  }

  @Override
  public void start(Stage stage) {

    GridPane gridPane = new GridPane();
    gridPane.setPadding(new Insets(20));
    gridPane.setHgap(10);
    gridPane.setVgap(10);

    Label lblName = new Label("Name:");
    GridPane.setConstraints(lblName, 0, 0);
    txtName = new TextField();
    GridPane.setConstraints(txtName, 1, 0);
    gridPane.getChildren().addAll(lblName, txtName);

    Button btnBack = new Button("Batal");
    btnBack.setOnAction(e -> handleBackButton(stage));
    GridPane.setConstraints(btnBack, 0, 1);
    gridPane.getChildren().add(btnBack);

    Button btnSubmit = new Button("Simpan");
    btnSubmit.setOnAction(e -> handleSubmitButton(stage));
    GridPane.setConstraints(btnSubmit, 1, 1);
    gridPane.getChildren().add(btnSubmit);

    // Jika ada data awal, setel nilai teks pada input fields
    if (initialCustomer.getIdcustomer() != 0) {
      stage.setTitle("Edit Customer");
      txtName.setText(initialCustomer.getName());
    } else {
      stage.setTitle("Tambah Customer Baru");
    }

    Scene scene = new Scene(gridPane, 250, 100);
    stage.setScene(scene);
    stage.showAndWait();
  }

  private void handleBackButton(Stage stage) {
    stage.close();
  }

  private void handleSubmitButton(Stage stage) {
    String name = txtName.getText();
    String errorMessage = "";

    if (name.isEmpty())
      errorMessage += "Nama harus diisi\n";
    else if (name.length() > 45)
      errorMessage += "Nama maksimal terdiri dari 45 karakter\n";

    if (errorMessage.isEmpty()) {
      String tableName = "customer";
      String[] columns = { "name" };
      Object[] values = { name };

      int idcustomer = initialCustomer.getIdcustomer();
      if (idcustomer != 0) {
        String condition = "idcustomer = '" + idcustomer + "'";
        Database.update(tableName, columns, values, condition);
        System.out.println("Data updated successfully!");
      } else {
        Database.insert(tableName, columns, values);
        System.out.println("Data inserted successfully!");
      }
      stage.close();
    } else {
      System.out.println(errorMessage);

      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Gagal Menyimpan");
      alert.setHeaderText("Gagal Menyimpan");
      alert.setContentText(errorMessage);
      alert.showAndWait();
      return;
    }
  }
}

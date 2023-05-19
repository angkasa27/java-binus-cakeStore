import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CakeForm extends Application {
  private TextField txtName;
  private TextField txtType;
  private TextField txtPrice;
  private Cake initialCake = new Cake();

  public static void main(String[] args) {
    launch(args);
  }

  public CakeForm() {
    // Default constructor
  }

  public CakeForm(Cake initialCake) {
    this.initialCake = initialCake;
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

    Label lblType = new Label("Type:");
    GridPane.setConstraints(lblType, 0, 1);
    txtType = new TextField();
    GridPane.setConstraints(txtType, 1, 1);
    gridPane.getChildren().addAll(lblType, txtType);

    Label lblPrice = new Label("Price:");
    GridPane.setConstraints(lblPrice, 0, 2);
    txtPrice = new TextField();
    GridPane.setConstraints(txtPrice, 1, 2);
    gridPane.getChildren().addAll(lblPrice, txtPrice);

    Button btnBack = new Button("Batal");
    btnBack.setOnAction(e -> handleBackButton(stage));
    GridPane.setConstraints(btnBack, 0, 3);
    gridPane.getChildren().add(btnBack);

    Button btnSubmit = new Button("Simpan");
    btnSubmit.setOnAction(e -> handleSubmitButton(stage));
    GridPane.setConstraints(btnSubmit, 1, 3);
    gridPane.getChildren().add(btnSubmit);

    // Jika ada data awal, setel nilai teks pada input fields
    if (initialCake.getIdcake() != 0) {
      stage.setTitle("Edit Cake");
      txtName.setText(initialCake.getName());
      txtType.setText(initialCake.getType());
      txtPrice.setText(String.valueOf(initialCake.getPrice()));
    } else {
      stage.setTitle("Tambah Cake Baru");
    }

    Scene scene = new Scene(gridPane, 250, 170);
    stage.setScene(scene);
    stage.showAndWait();
  }

  private void handleBackButton(Stage stage) {
    stage.close();
  }

  private void handleSubmitButton(Stage stage) {
    String name = txtName.getText();
    String type = txtType.getText();
    String priceText = txtPrice.getText();
    String errorMessage = "";

    if (name.isEmpty())
      errorMessage += "Nama harus diisi\n";
    else if (name.length() > 45)
      errorMessage += "Nama maksimal terdiri dari 45 karakter\n";

    if (type.isEmpty())
      errorMessage += "Tipe harus diisi\n";
    else if (type.length() > 45)
      errorMessage += "Tipe maksimal terdiri dari 45 karakter\n";

    int price = 0;
    if (priceText.isEmpty())
      errorMessage += "Harga harus diisi\n";
    else if (!priceText.matches("[0-9]+"))
      errorMessage += "Harga harus berisi angka saja\n";
    else {
      price = Integer.parseInt(priceText);
      if (price < 1)
        errorMessage += "Harga tidak boleh kosong\n";
    }

    if (errorMessage.isEmpty()) {
      String tableName = "cake";
      String[] columns = { "name", "type", "price" };
      Object[] values = { name, type, price };

      int idcake = initialCake.getIdcake();
      if (idcake != 0) {
        String condition = "idcake = '" + idcake + "'";
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

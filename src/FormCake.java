import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FormCake extends Application {
  private Database db;
  private TextField txtName;
  private TextField txtType;
  private TextField txtPrice;
  private Cake initialCake;

  public static void main(String[] args) {
    launch(args);
  }

  public FormCake() {
    // Default constructor
  }

  public FormCake(Cake initialCake) {
    this.initialCake = initialCake;
  }

  @Override
  public void start(Stage stage) {
    stage.setTitle("Form Input Example");

    db = new Database();

    VBox vbox = new VBox();
    vbox.setPadding(new Insets(20));
    vbox.setSpacing(10);

    Label lblTitle = new Label("Form Input Example");
    lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    vbox.getChildren().add(lblTitle);

    Label lblName = new Label("Name:");
    txtName = new TextField();
    vbox.getChildren().addAll(lblName, txtName);

    Label lblType = new Label("Type:");
    txtType = new TextField();
    vbox.getChildren().addAll(lblType, txtType);

    Label lblPrice = new Label("Price:");
    txtPrice = new TextField();
    vbox.getChildren().addAll(lblPrice, txtPrice);

    Button btnSubmit = new Button("Submit");
    btnSubmit.setOnAction(e -> handleSubmitButton());
    vbox.getChildren().add(btnSubmit);

    // Jika ada data awal, setel nilai teks pada input fields
    if (initialCake != null) {
      txtName.setText(initialCake.getName());
      txtType.setText(initialCake.getType());
      txtPrice.setText(String.valueOf(initialCake.getPrice()));
    }

    Scene scene = new Scene(vbox, 400, 300);
    stage.setScene(scene);
    stage.show();
  }

  private void handleSubmitButton() {
    String name = txtName.getText();
    String type = txtType.getText();
    int price = Integer.parseInt(txtPrice.getText());

    String tableName = "cake";
    String[] columns = { "name", "type", "price" };
    Object[] values = { name, type, price };

    // Periksa apakah data dengan nama tertentu sudah ada dalam database
    // String condition = "name = '" + name + "'";
    // if (db.select(tableName, columns, condition).next()) {
    // // Jika ada, lakukan pembaruan data
    // db.update(tableName, columns, values, condition);
    // System.out.println("Data updated successfully!");
    // } else {
    // Jika tidak ada, lakukan penambahan data baru
    db.insert(tableName, columns, values);
    System.out.println("Data inserted successfully!");
    // }
  }
}

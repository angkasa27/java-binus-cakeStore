import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
  private Button viewCakeButton;
  private Button addCustomerButton;
  private Button cashierSystemButton;
  private Button transactionHistoryButton;
  private Button exitButton;

  public void start(Stage stage) {
    stage.setTitle("Cake Shop");

    Label lblTitle = new Label("Selamat Datang!");
    lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

    viewCakeButton = new Button("Daftar Cake");
    viewCakeButton.setOnAction(e -> {
      CakeTable cakeTable = new CakeTable();
      cakeTable.start(stage);
    });

    addCustomerButton = new Button("Daftar Customer");
    addCustomerButton.setOnAction(e -> {
      CustomerTable customerTable = new CustomerTable();
      customerTable.start(stage);
    });

    cashierSystemButton = new Button("Transaksi Baru");
    cashierSystemButton.setOnAction(e -> {
      TransactionForm transactionForm = new TransactionForm();
      transactionForm.start(stage);
    });

    transactionHistoryButton = new Button("Riwayat Transaksi");
    transactionHistoryButton.setOnAction(e -> {
      TransactionTable transactionTable = new TransactionTable();
      transactionTable.start(stage);
    });

    exitButton = new Button("Keluar");
    exitButton.setOnAction(e -> Platform.exit());

    VBox dashboardLayout = new VBox(10);
    dashboardLayout.setAlignment(Pos.CENTER);
    dashboardLayout.getChildren().addAll(lblTitle, viewCakeButton, addCustomerButton, cashierSystemButton,
        transactionHistoryButton, exitButton);

    Scene scene = new Scene(dashboardLayout, 400, 300);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
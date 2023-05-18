import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
  private Button addCakeButton;
  private Button exitButton;

  public void start(Stage stage) {
    stage.setTitle("Cake Shop");

    Label lblTitle = new Label("Selamat Datang!");
    lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

    addCakeButton = new Button("Tambah Kue Baru");
    addCakeButton.setOnAction(e -> {
      FormCake formCake = new FormCake();
      formCake.start(new Stage());
    });

    exitButton = new Button("Keluar");
    exitButton.setOnAction(e -> Platform.exit());

    VBox dashboardLayout = new VBox(10);
    dashboardLayout.setAlignment(Pos.CENTER);
    dashboardLayout.getChildren().addAll(lblTitle, addCakeButton, exitButton);

    Scene scene = new Scene(dashboardLayout, 400, 300);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
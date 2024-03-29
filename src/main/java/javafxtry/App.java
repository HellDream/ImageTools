package javafxtry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafxtry.imgbackends.utils.Constants;
import javafxtry.imgfrontends.StageManager;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    public static Stage stage;
    public static FXMLLoader loader;
    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("primary.fxml"));
        loader = fxmlLoader;
        stage.setOnCloseRequest(e->closeApp(fxmlLoader));
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
        StageManager.STAGE.put("PrimaryStage", stage);
    }

    private void closeApp(FXMLLoader fxmlLoader) {
        PrimaryController controller = fxmlLoader.getController();
        controller.closeImage();
    }

    public static void main(String[] args) {
        Constants.initialize("D:\\ImageMagick-7.0.8-Q16");
        launch();
    }
}
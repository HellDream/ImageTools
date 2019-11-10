package javafxtry;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ImageTextController implements Initializable {
    public ImageView textImageView;
    public StackPane imageStackPane;


    @FXML
    private Text text;
    public void textAboard(MouseEvent event) {

    }

    public void textSave(MouseEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        text = new Text();
        imageStackPane.getChildren().add(text);
        StackPane.setAlignment(text, Pos.CENTER);
    }
}

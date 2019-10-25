package javafxtry;


import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class ImageCutController implements Initializable {

    public ImageView cutMainImage;
//    public Group group;
    public Pane imagePane;
    private Rectangle rectangle;
    private Rectangle rectangleView;
    private Rectangle shadeRectangle;

    public void setImage(Image image) {
        cutMainImage.setImage(image);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        shadeRectangle = new Rectangle();
        imagePane.getChildren().add(shadeRectangle);
    }

    public void shadeImage() {
        System.out.println(cutMainImage.toString());
        shadeRectangle.setHeight(cutMainImage.getFitHeight());
        shadeRectangle.setWidth(cutMainImage.getFitWidth());
        shadeRectangle.setX(cutMainImage.getX());
        shadeRectangle.setY(cutMainImage.getY());
        shadeRectangle.setLayoutX(cutMainImage.getLayoutX());
        shadeRectangle.setLayoutY(cutMainImage.getLayoutY());
        shadeRectangle.setFill(Color.BLACK);
    }


}

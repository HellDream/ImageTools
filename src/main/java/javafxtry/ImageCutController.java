package javafxtry;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.net.URL;
import java.util.ResourceBundle;

public class ImageCutController implements Initializable {

    public ImageView cutMainImage;
//    public Group group;
    public Pane imagePane;
    @FXML
    private Rectangle rectangleView;
    @FXML
    private Rectangle shadeRectangle;
    @FXML
    private Shape shadowShape;

    public void setImage(Image image) {
        cutMainImage.setImage(image);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CropHandler handler = new CropHandler();
        shadeRectangle = new Rectangle();
        rectangleView = new Rectangle();
        imagePane.addEventFilter(MouseEvent.MOUSE_PRESSED, handler.onPress());
        imagePane.addEventFilter(MouseEvent.MOUSE_DRAGGED, handler.onDrag());
        imagePane.addEventFilter(MouseEvent.MOUSE_RELEASED, handler.onRelease());
        imagePane.getChildren().add(rectangleView);
    }

    public void shadeImage() {
        System.out.println(cutMainImage.toString());
        shadeRectangle.setHeight(cutMainImage.getFitHeight());
        shadeRectangle.setWidth(cutMainImage.getFitWidth());
        shadeRectangle.setX(cutMainImage.getX());
        shadeRectangle.setY(cutMainImage.getY());
        shadeRectangle.setLayoutX(cutMainImage.getLayoutX());
        shadeRectangle.setLayoutY(cutMainImage.getLayoutY());
        shadeRectangle.setFill(Color.TRANSPARENT);
        shadowShape = Shape.subtract(shadeRectangle, new Rectangle());
        shadowShape.setFill(Color.GRAY);
        shadowShape.setOpacity(0.6);
        imagePane.getChildren().add(shadowShape);
//        shadeRectangle.setOpacity(0.6);
    }

    public void cropAboard(MouseEvent event) {

    }

    public void cropSave(MouseEvent event) {

    }


    class CropHandler{
        double startX;
        double startY;
        double endX;
        double endY;
        boolean pressed = false;

        public EventHandler<MouseEvent> onPress(){

            return e -> {
                startX = e.getX();
                startY = e.getY();
                System.out.println(startX+"     "+startY);
                pressed = true;
            };

        }

        public EventHandler<MouseEvent> onDrag(){
            return e->{
                rectangleView.setX(startX>=0?startX : cutMainImage.getX());
                rectangleView.setY(startY>=0? startY: cutMainImage.getY());
                if(Math.abs(startX-e.getX())<=shadeRectangle.getWidth()){
                    rectangleView.setWidth(Math.abs(startX-e.getX()));
                }else{
                    System.out.println(e.getX());
                    rectangleView.setWidth(shadeRectangle.getWidth());
                }
                if(Math.abs(startY-e.getY())<=shadeRectangle.getHeight()){
                    rectangleView.setHeight(Math.abs(startY-e.getY()));
                }else{
                    System.out.println(e.getY());
                    rectangleView.setHeight(shadeRectangle.getHeight());
                }
                rectangleView.setFill(Color.TRANSPARENT);
                drawShape();
            };
        }

        public EventHandler<MouseEvent> onRelease(){
            return e -> {
                endX = e.getX();
                endY = e.getY();
                pressed = false;
            };
        }
    }

    private void drawShape(){
        imagePane.getChildren().remove(shadowShape);
        shadowShape = Shape.subtract(shadeRectangle, rectangleView);
        shadowShape.setFill(Color.GRAY);
        shadowShape.setOpacity(0.6);
        imagePane.getChildren().add(shadowShape);
    }
}

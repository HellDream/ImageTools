package javafxtry;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafxtry.imgbackends.operations.ImageManager;
import javafxtry.imgfrontends.FrontEndImageManager;
import javafxtry.imgfrontends.StageManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafxtry.imgfrontends.FrontEndImageManager.locateImg;


public class ImageCutController implements Initializable, ImageCommand {

    public ImageView cutMainImage;
    public Pane imagePane;
    @FXML
    private Rectangle rectangleView;
    @FXML
    private Rectangle shadeRectangle;
    @FXML
    private Shape shadowShape;
    private Image originImage;
    private FrontEndImageManager frontEndManager = FrontEndImageManager.getInstance();

    public void setImage(Image image) {
        cutMainImage.setImage(image);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Pair<String, Image> topImage = frontEndManager.getTopImage();
        originImage = topImage.getValue();
        cutMainImage.setImage(topImage.getValue());
        locateImg(cutMainImage);
        shadeRectangle = new Rectangle();
//        shadeImage();
        CropHandler handler = new CropHandler();
        rectangleView = new Rectangle();
        imagePane.addEventFilter(MouseEvent.MOUSE_PRESSED, handler.onPress());
        imagePane.addEventFilter(MouseEvent.MOUSE_DRAGGED, handler.onDrag());
        imagePane.addEventFilter(MouseEvent.MOUSE_RELEASED, handler.onRelease());
        imagePane.getChildren().add(rectangleView);
    }

    public void initial() {
        imagePane.getChildren().remove(shadowShape);
        shadeRectangle = new Rectangle();
        rectangleView = new Rectangle();
    }

    public void shadeImage() {
        shadeRectangle.setHeight(cutMainImage.getFitHeight());
        shadeRectangle.setWidth(cutMainImage.getFitWidth());
        System.out.println("shade rectangle width:" + shadeRectangle.getWidth());
        shadeRectangle.setX(cutMainImage.getX());
        shadeRectangle.setY(cutMainImage.getY());
        shadeRectangle.setLayoutX(cutMainImage.getLayoutX());
        shadeRectangle.setLayoutY(cutMainImage.getLayoutY());
        shadeRectangle.setFill(Color.TRANSPARENT);
        imagePane.getChildren().remove(shadowShape);
        shadowShape = Shape.subtract(shadeRectangle, new Rectangle());
        shadowShape.setFill(Color.GRAY);
        shadowShape.setOpacity(0.6);
        imagePane.getChildren().add(shadowShape);
    }

    public void cropAboard(MouseEvent event) {
        if (originImage != null) {
            while (!frontEndManager.getTopImage().getValue().equals(originImage)) {
                frontEndManager.remove();
            }
            cutMainImage.setImage(originImage);
            locateImg(cutMainImage);
        }

    }

    public void cropSave(MouseEvent event) {
        Pair<String, Image> topImage = frontEndManager.getTopImage();
        PrimaryController controller = (PrimaryController) StageManager.CONTROLLER.get("PrimaryController");
        if (!topImage.getValue().equals(originImage)) {
            frontEndManager.removeTo(originImage);
            frontEndManager.add(topImage.getKey(), topImage.getValue());
            controller.mainImage.setImage(topImage.getValue());
            controller.setSave(false);
        }
        Stage currentStage = StageManager.STAGE.get("ImageCutStage");
        currentStage.close();
    }

    @Override
    public void redo() {

    }

    @Override
    public void undo() {

    }


    class CropHandler {
        double mousePressX;
        double mousePressY;
        double mouseReleaseX;
        double mouseReleaseY;
        boolean pressed = false;

        public EventHandler<MouseEvent> onPress() {
            return e -> {
                shadeImage();
                mousePressX = e.getX() < cutMainImage.getLayoutX() + cutMainImage.getX() ?
                        cutMainImage.getLayoutX() + cutMainImage.getX() : e.getX();
                mousePressY = e.getY() - imagePane.getLayoutY() < cutMainImage.getLayoutY() - imagePane.getLayoutY() ?
                        cutMainImage.getLayoutY() - imagePane.getLayoutY() : e.getY() - imagePane.getLayoutY();
                pressed = true;
            };
        }

        public EventHandler<MouseEvent> onDrag() {
            return e -> {
                double currentX = getCurrentX(e);
                double currentY = getCurrentY(e);
                double width = Math.abs(currentX - mousePressX);
                if (width > cutMainImage.getFitWidth())
                    width = cutMainImage.getFitWidth();
                // Y begins in -14
                double height = Math.abs(currentY - mousePressY + cutMainImage.getLayoutY());
                if (height > cutMainImage.getFitHeight()) {
                    height = cutMainImage.getFitHeight();
                }
                setRectangleView(currentX, currentY, width, height);
                rectangleView.setFill(Color.TRANSPARENT);
                drawShape();
            };
        }

        public EventHandler<MouseEvent> onRelease() {
            return e -> {
                mouseReleaseX = e.getX() < cutMainImage.getLayoutX() + cutMainImage.getX() ?
                        cutMainImage.getLayoutX() + cutMainImage.getX() : e.getX();
                mouseReleaseY = e.getY() - imagePane.getLayoutY() < cutMainImage.getLayoutY() - imagePane.getLayoutY() ?
                        cutMainImage.getLayoutY() - imagePane.getLayoutY() : e.getY() - imagePane.getLayoutY();

                if (mousePressX == mouseReleaseX || mousePressX == mouseReleaseY) {
                    initial();
                    return;
                }
                double relateX = rectangleView.getX() - cutMainImage.getLayoutX() - cutMainImage.getX();
                double relateY = rectangleView.getY() + imagePane.getLayoutY() - cutMainImage.getLayoutY();
                ImageManager manager = ImageManager.getInstance();
                double ratio = cutMainImage.getFitHeight() / cutMainImage.getImage().getHeight();
                int actualX = (int) (relateX / ratio);
                int actualY = (int) (relateY / ratio);
                int actualW = (int) (rectangleView.getWidth() / ratio);
                int actualH = (int) (rectangleView.getHeight() / ratio);
                Pair<String, Image> topImage = frontEndManager.getTopImage();
                String newImagePath = manager.cutImage(
                        topImage.getKey(),
                        actualX, actualY, actualW, actualH);
                Image newImage = null;
                try {
                    newImage = new Image(new FileInputStream(newImagePath));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                frontEndManager.add(newImagePath, newImage);
                cutMainImage.setImage(newImage);
                locateImg(cutMainImage);
                initial();
            };
        }

        private double getCurrentX(MouseEvent e) {
            double currentX = e.getX();
            // X is beyond the left of the image
            if (currentX < cutMainImage.getLayoutX() + cutMainImage.getX()) {
                currentX = cutMainImage.getLayoutX() + cutMainImage.getX();
                // X is beyond the right of the image
            } else if (currentX > cutMainImage.getLayoutX() + cutMainImage.getX() + cutMainImage.getFitWidth()) {

                currentX = cutMainImage.getLayoutX() + cutMainImage.getX() + cutMainImage.getFitWidth();
            }
            return currentX;
        }

        private double getCurrentY(MouseEvent e) {
            double currentY = e.getY() - imagePane.getLayoutY();
            // Y is upper the image;
            if (currentY < cutMainImage.getLayoutY() - imagePane.getLayoutY()) {
                currentY = cutMainImage.getLayoutY() - imagePane.getLayoutY();
                // Y is under the image;
            } else if (currentY > cutMainImage.getLayoutY() - imagePane.getLayoutY() + cutMainImage.getFitHeight()) {
                currentY = cutMainImage.getLayoutY() - imagePane.getLayoutY() + cutMainImage.getFitHeight();
            }
            return currentY;
        }

        /**
         * @description: set rectangle view coordinate and size
         * @param: currentX, currentY, width height
         */
        private void setRectangleView(double currentX, double currentY, double width, double height) {
            rectangleView.setHeight(height);
            rectangleView.setWidth(width);
            if (currentX < mousePressX && currentY < mousePressY) {
                // the current mouse pos is on the left top of mouse press
                rectangleView.setX(currentX);
                rectangleView.setY(currentY);
            } else if (currentX < mousePressX) {
                // current mouse pos is on the left bottom of mouse press
                rectangleView.setX(currentX);
                rectangleView.setY(mousePressY);
                //current mouse pos is on the right top of the mouse press
            } else if (currentY < mousePressY) {
                rectangleView.setX(mousePressX);
                rectangleView.setY(currentY);
            } else {
                // current mouse pos is on the right bottom of the mouse press
                rectangleView.setX(mousePressX);
                rectangleView.setY(mousePressY);
            }
        }
    }

    private void drawShape() {
        imagePane.getChildren().remove(shadowShape);
        shadowShape = Shape.subtract(shadeRectangle, rectangleView);
        shadowShape.setFill(Color.GRAY);
        shadowShape.setOpacity(0.6);
        imagePane.getChildren().add(shadowShape);
    }
}
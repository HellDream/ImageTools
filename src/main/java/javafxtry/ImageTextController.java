package javafxtry;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafxtry.imgbackends.operations.ImageManager;
import javafxtry.imgbackends.utils.Constants;
import javafxtry.imgfrontends.FrontEndImageManager;
import javafxtry.imgfrontends.StageManager;
import javafxtry.models.TextModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafxtry.imgbackends.utils.Constants.FONT_MAP;
import static javafxtry.imgfrontends.FrontEndImageManager.locateImg;

/**
 * @ClassName ImageTextController
 * @Description controller for adding text to image
 * @Author Zhenyu YE
 * @Date 2019/12/12 21:09
 * @Version 1.0
 **/
public class ImageTextController implements Initializable {
    public ImageView textImageView;
    public Pane imagePane;
    private FrontEndImageManager frontEndManager = FrontEndImageManager.getInstance();
    private ImageManager backendManager = ImageManager.getInstance();
    private Image originImage;
    private boolean modify = false;


    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public TextModel getTextModel() {
        return textModel;
    }

    public void setTextModel(TextModel textModel) {
        this.textModel = textModel;
        if (textModel != null) {
            Font font = new Font(textModel.getFontFamily(), textModel.getFontSize());
            text.setFont(font);
            text.setText(textModel.getText());
            text.setFill(textModel.getTextColor());
            modify = true;
        }
    }

    private TextModel textModel;
    @FXML
    private Text text;

    public void textAboard(MouseEvent event) {
        if (originImage != null) {
            while (!frontEndManager.getTopImage().getValue().equals(originImage)) {
                frontEndManager.remove();
            }
            textImageView.setImage(originImage);
            text.setText("Please Double Click Here...");
            textModel = null;
            text.setFont(Font.font("Verdana", FontPosture.ITALIC, 20));
            text.setFill(Color.BLACK);
            locateImg(textImageView);
            locateText();
        }

    }

    public void textSave(MouseEvent event) throws Exception {
        if (modify) {
            double relateX = text.getX() - textImageView.getX();
            double relateY = text.getY() - textImageView.getY();
            double ratio = textImageView.getFitHeight() / textImageView.getImage().getHeight();
            int actualX = (int) (relateX / ratio);
            int actualY = (int) (relateY / ratio);
            TextModel model = new TextModel();
            model.setFontSize(text.getFont().getSize());
            model.setFontFamily(text.getFont().getFamily());
            model.setText(text.getText());
            model.setTextColor((Color) text.getFill());
            String color = model.getRGBAColor();
            Pair<String, Image> topImage = frontEndManager.getTopImage();
            String font = FONT_MAP.get(model.getFontFamily());
            if (font == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Current Text font does not exist");
                alert.setHeaderText("Information");
                alert.showAndWait();
                return;
            }
            String newImagePath = backendManager.addText(topImage.getKey(), model.getText(), FONT_MAP.get(model.getFontFamily()), actualX, actualY, (int) (model.getFontSize() / ratio), color);
            Image newImage = null;
            try {
                newImage = new Image(new FileInputStream(newImagePath));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            frontEndManager.add(newImagePath, newImage);
            PrimaryController controller = (PrimaryController) StageManager.CONTROLLER.get("PrimaryController");
            controller.mainImage.setImage(newImage);
            controller.setSave(false);
            Stage currentStage = StageManager.STAGE.get("ImageTextStage");
            currentStage.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Pair<String, Image> topImage = frontEndManager.getTopImage();
        originImage = topImage.getValue();
        textImageView.setImage(topImage.getValue());
        locateImg(textImageView);
        text = new Text("Please Double Click Here...");
        textModel = null;
        text.setFont(Font.font("Verdana", FontPosture.ITALIC, 20));
        TextHandler handler = new TextHandler();
        imagePane.addEventHandler(MouseEvent.MOUSE_PRESSED, handler.onPress());
        imagePane.addEventHandler(MouseEvent.MOUSE_DRAGGED, handler.moveTextField());
        text.addEventHandler(MouseEvent.MOUSE_CLICKED, handler.updateText());
        imagePane.getChildren().add(text);
        locateText();

    }

    private void locateText() {
        text.setLayoutX(textImageView.getLayoutX());
        text.setLayoutY(textImageView.getLayoutY());
        text.setX(textImageView.getX() + textImageView.getFitWidth() / 3);
        text.setY(textImageView.getY() + textImageView.getFitHeight() / 3);
    }

    class TextHandler {
        private double mouseCurrentX;
        private double mouseCurrentY;

        public EventHandler<MouseEvent> moveTextField() {
            return e -> {
                double posX = getX(e) - mouseCurrentX;
                double posY = getY(e) - mouseCurrentY;
                mouseCurrentX = getX(e);
                mouseCurrentY = getY(e);
                if (text.getX() + posX < textImageView.getX()) {
                    text.setX(textImageView.getX());
                } else if (text.getX() + posX > textImageView.getX() + textImageView.getFitWidth()) {
                    text.setX(textImageView.getX() + textImageView.getFitWidth());
                } else text.setX(text.getX() + posX);
                if (text.getY() + posY < textImageView.getY()) {
                    text.setY(textImageView.getY());
                } else if (text.getY() + posY > textImageView.getY() + textImageView.getFitHeight()) {
                    text.setY(textImageView.getY() + textImageView.getFitHeight());
                } else text.setY(text.getY() + posY);
            };
        }

        private double getX(MouseEvent e) {
            if (e.getX() > textImageView.getLayoutX() + textImageView.getX() + textImageView.getFitWidth()) {
                return textImageView.getLayoutX() + textImageView.getX() + textImageView.getFitWidth();
            }
            return e.getX();
        }

        private double getY(MouseEvent e) {
            if (e.getY() > textImageView.getLayoutY() + textImageView.getY() + textImageView.getFitHeight()) {
                return textImageView.getLayoutY() + textImageView.getY() + textImageView.getFitHeight();
            }
            return e.getY();
        }

        public EventHandler<MouseEvent> onPress() {
            return e -> {
                mouseCurrentX = e.getX() < textImageView.getLayoutX() + textImageView.getX() ?
                        textImageView.getLayoutX() + textImageView.getX() : e.getX();
                mouseCurrentY = e.getY() < textImageView.getLayoutY() + textImageView.getY() ?
                        textImageView.getLayoutY() + textImageView.getY() : e.getY();
            };
        }

        public EventHandler<MouseEvent> updateText() {
            return e -> {
                if (e.getClickCount() == 2) {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("textField.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (scene == null) return;
                    stage.setScene(scene);
                    stage.initOwner(App.stage);
                    StageManager.STAGE.put("TextFieldStage", stage);
                    StageManager.CONTROLLER.put("ImageTextController", ImageTextController.this);
                    stage.show();
                    stage.setOnCloseRequest(ee -> {
                        StageManager.STAGE.remove("TextFieldStage");
                        StageManager.CONTROLLER.remove("ImageTextController");

                    });

                }
            };
        }
    }
}

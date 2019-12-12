package javafxtry;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafxtry.imgbackends.utils.Constants;
import javafxtry.imgfrontends.StageManager;
import javafxtry.models.TextModel;

import java.net.URL;
import java.util.ResourceBundle;

public class TextFieldController implements Initializable {
    public Slider fontSize;
    public TextField textField;
    public ComboBox font;
    public ColorPicker fontColor;
    public Button submitBtn;
    public Button cancelBtn;

    public void submit(ActionEvent event) {
        Color color = fontColor.getValue();
        Double fontsize = fontSize.getValue();
        String text = textField.getText();
        String fontFamily = font.getValue() == null ? "" : font.getValue().toString();
        ImageTextController controller = (ImageTextController) StageManager.CONTROLLER.get("ImageTextController");
        if (controller == null)
            return;
        TextModel textModel = new TextModel();
        textModel.setTextColor(color);
        textModel.setText(text);
        textModel.setFontFamily(fontFamily);
        textModel.setFontSize(fontsize);
        controller.setTextModel(textModel);
        Stage currentStage = StageManager.STAGE.get("TextFieldStage");
        currentStage.close();
    }

    public void cancel(ActionEvent event) {
        Stage currentStage = StageManager.STAGE.get("TextFieldStage");
        currentStage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.font.getItems().addAll(Constants.FONT_MAP.keySet());
        submitBtn.setOnAction(this::submit);
        cancelBtn.setOnAction(this::cancel);
    }
}

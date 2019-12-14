package javafxtry;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafxtry.imgbackends.utils.Constants;
import javafxtry.imgfrontends.StageManager;
import javafxtry.models.TextModel;

import java.net.URL;
import java.util.ResourceBundle;
/**
 * @ClassName TextFieldController
 * @Description controller for modifying text
 * @Author Zhenyu YE
 * @Date 2019/12/12 21:09
 * @Version 1.0
 **/
public class TextFieldController implements Initializable {
    public Slider fontSize;
    public TextField textField;
    public ComboBox font;
    public ColorPicker fontColor;
    public Button submitBtn;
    public Button cancelBtn;

    /**
     * Submit text field
     * @param event ActionEvent
     */
    public void submit(ActionEvent event) {
        Color color = fontColor.getValue();
        Double fontsize = fontSize.getValue();
        String text = textField.getText();
        String fontFamily = font.getValue() == null ? "" : font.getValue().toString();
        if(Constants.FONT_MAP.get(fontFamily)==null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Font not available");
            alert.setContentText("The current font is not available. Please choose other font family.");
            alert.showAndWait();
            return;
        }
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
    /**
     * Cancel submission
     * @param event ActionEvent
     */
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

package javafxtry;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafxtry.imgbackends.operations.ImageManager;
import javafxtry.imgbackends.utils.Constants;
import javafxtry.imgfrontends.FrontEndImageManager;
import javafxtry.imgfrontends.StageManager;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafxtry.imgfrontends.FrontEndImageManager.IMAGE_VIEW_PARAMS;
import static javafxtry.imgfrontends.FrontEndImageManager.locateImg;

public class PrimaryController implements Initializable, ImageCommand {
    public ImageView mainImage;
    public Pane imagePane;
    //    public ScrollPane scrollPane = new ScrollPane();
//    public StackPane stackPane;
//    private String originImagePath = null;
//    private String tmpImagePath = null;
    private boolean isSave = true;
    private String recentDir = null;
    private ImageManager imageManager = ImageManager.getInstance();
    private FrontEndImageManager frontEndManager = FrontEndImageManager.getInstance();
    @FXML
    private void openFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        if (recentDir == null)
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        else
            fileChooser.setInitialDirectory(new File(recentDir));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File originFile = fileChooser.showOpenDialog(App.stage);
        recentDir = originFile.getParent();
//        this.originImagePath = originFile.getPath();
//        tmpImagePath = null;
//        System.out.println(originImagePath);
        String path = originFile.getPath();
        //show selected Image
        Image img = new Image(new FileInputStream(originFile));
        frontEndManager.add(path, img);
        mainImage.setImage(img);
        locateImg(this.mainImage);
//        System.out.println("width: "+img.getWidth());
//        System.out.println("height: "+img.getHeight());
//        System.out.println("view width: "+mainImage.getFitWidth());
//        System.out.println("view height: "+mainImage.getFitHeight());
//        System.out.println("view X: "+mainImage.getX());
//        System.out.println("view Y: "+mainImage.getY());
    }


    public void saveImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.setInitialDirectory(new File(recentDir==null?System.getProperty("user.home"):recentDir));
        fileChooser.setInitialFileName("image.jpg");
        File file = fileChooser.showSaveDialog(App.stage);
        if(file!=null){
            try{
                ImageIO.write(SwingFXUtils.fromFXImage(mainImage.getImage(),null),"jpg",file);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        this.isSave = true;
    }

    @FXML
    public void closeImage() {
        if(!isSave){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("You haven't save the picture, would you like to save?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.YES||result.get()==ButtonType.OK){
                saveImage();
            }
        }
        File dir = new File(Constants.CACHES);
        File[] files = dir.listFiles();
        if(files!=null){
            for(File f:files){
                f.delete();
            }
        }
        App.stage.close();
    }

    @FXML
    public void about(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("This is a About Alert");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    public void ImageCutOp(MouseEvent event) throws IOException {
        if (event.getClickCount()!=0) {
//            if (originImagePath != null) {
            if (frontEndManager.getImageSize() != 0) {
                openImageCutStage();
            }
        }
    }

    @FXML
    public void ImageRotateOp(MouseEvent event) throws FileNotFoundException {
        if (event.isPrimaryButtonDown()) {
//            if(tmpImagePath==null)
//                tmpImagePath = imageManager.rotate(originImagePath, 90);
//            else{
//                tmpImagePath = imageManager.rotate(tmpImagePath, 90);
//            }
//            if(tmpImagePath==null) throw new FileNotFoundException();
//            System.out.println(tmpImagePath);
//            mainImage.setImage(new Image(new FileInputStream(new File(tmpImagePath))));
//            locateImg(this.mainImage);
//            this.isSave = false;
        }
    }

    private void openImageCutStage() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Cutting Image");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("imageCut.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setOnShowing(event -> {
            ImageCutController controller = fxmlLoader.getController();
            controller.setImage(this.mainImage.getImage());
            locateImg(controller.cutMainImage);
            controller.shadeImage();
        });
        stage.show();
//        App.stage.showAndWait();
        StageManager.STAGE.put("ImageCutStage",stage);
        StageManager.CONTROLLER.put("PrimaryController",this);
        stage.setOnCloseRequest(e->{

        });

    }

    public void ImageTextOp(MouseEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IMAGE_VIEW_PARAMS.put("fitHeight",mainImage.getFitHeight());
        IMAGE_VIEW_PARAMS.put("fitWidth", mainImage.getFitWidth());
    }

    @Override
    public void redo() {

    }

    @Override
    public void undo() {

    }
}

package javafxtry;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafxtry.imgbackends.operations.ImageManager;
import javafxtry.imgfrontends.FrontEndImageManager;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;


public class ThumbnailViewController implements Initializable {
    public ImageView thumbnailImageView;
    private ImageManager imageManager = ImageManager.getInstance();
    private FrontEndImageManager frontEndManager = FrontEndImageManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String topImagePath = frontEndManager.getTopImage().getKey();
        try{
            String thumbImagePath = imageManager.getThumbnailImg(topImagePath, 100,100);
            Image thumbnailImg = new Image(new FileInputStream(thumbImagePath));
            thumbnailImageView.setImage(thumbnailImg);
            frontEndManager.locateThumbnail(thumbnailImageView);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void saveThumbnail(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Thumbnail");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName(frontEndManager.getBaseImageName()+"-thumbnail"+"."+frontEndManager.getBaseFormat());
        File file = fileChooser.showSaveDialog(App.stage);
        if(file!=null){
            try{
                ImageIO.write(SwingFXUtils.fromFXImage(thumbnailImageView.getImage(),null),frontEndManager.getBaseFormat(),file);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}

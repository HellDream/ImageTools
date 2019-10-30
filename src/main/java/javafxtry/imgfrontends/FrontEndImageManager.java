package javafxtry.imgfrontends;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FrontEndImageManager {
    public static final Map<String, Object> IMAGE_VIEW_PARAMS = new HashMap<>();
    public static Stack<Image> imageStack = new Stack<>();
    public static void locateImg(ImageView imageView) {
        Image img = imageView.getImage();
        if (img != null) {
            double w;
            double h;

            double ratioX = (double)IMAGE_VIEW_PARAMS.get("fitWidth") / img.getWidth();
            double ratioY = (double)IMAGE_VIEW_PARAMS.get("fitHeight") / img.getHeight();

            double reducCoeff;
            if (ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageView.setX(((double)IMAGE_VIEW_PARAMS.get("fitWidth") - w) / 2);
            imageView.setY(((double)IMAGE_VIEW_PARAMS.get("fitHeight") - h) / 2);
            imageView.setFitWidth(w);
            imageView.setFitHeight(h);
        }
    }

    public static WritableImage writeImg(PixelReader reader, ImageView imageView, Rectangle cutView){
        double ratioX = (double)IMAGE_VIEW_PARAMS.get("fitWidth") / imageView.getImage().getWidth();
        double ratioY = (double)IMAGE_VIEW_PARAMS.get("fitHeight") / imageView.getImage().getHeight();
        double reducCoeff = ratioX >= ratioY? ratioY:ratioX;
        double originWidth = cutView.getWidth() / reducCoeff;
        double originHeight = cutView.getHeight() / reducCoeff;
        return new WritableImage(reader, (int)cutView.getX(),(int)cutView.getY(), (int)originWidth, (int)originHeight);
    }

}

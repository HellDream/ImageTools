package javafxtry.imgfrontends;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    public static final Map<String, Object> IMAGE_VIEW_PARAMS = new HashMap<>();

    public static void locateImg(ImageView imageView) {
        Image img = imageView.getImage();
        if (img != null) {
            double w;
            double h;

            double ratioX = (double)IMAGE_VIEW_PARAMS.get("fitWidth") / img.getWidth();
            double ratioY = (double)IMAGE_VIEW_PARAMS.get("fitHeight") / img.getHeight();

            double reducCoeff = 0;
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

}

package javafxtry.imgfrontends;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FrontEndImageManager {
    public static final Map<String, Object> IMAGE_VIEW_PARAMS = new HashMap<>();
    public static final Map<Image, Double> IMAGE_RATIO = new HashMap<>();
    private Stack<String> imagePaths;
    private Stack<Image> imageStack;
    private Stack<String> imagePathCache;
    private Stack<Image> imageStackCache;
    private int imageSize = 0;
    private static FrontEndImageManager manager = null;

    private FrontEndImageManager(){
        imagePaths = new Stack<>();
        imageStack = new Stack<>();
        imagePathCache = new Stack<>();
        imageStackCache = new Stack<>();
    }

    public static FrontEndImageManager getInstance(){
        if(manager==null){
            manager = new FrontEndImageManager();
        }
        return manager;
    }

    // modified to local method
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
            double ratio = imageView.getFitHeight() / img.getHeight();
            IMAGE_RATIO.put(img, ratio);
        }
    }

    public int getImageSize(){
        return imageSize;
    }

    public void add(String imagePath, Image image){
        imagePaths.push(imagePath);
        imageStack.push(image);
        imagePathCache.clear();
        imageStackCache.clear();
        imageSize++;
    }

    public void remove(){
        imagePathCache.push(imagePaths.pop());
        imageStackCache.push(imageStack.pop());
        imageSize--;
    }

    public void reAdd(){
        imagePaths.push(imagePathCache.pop());
        imageStack.push(imageStackCache.pop());
        imageSize++;
    }

    public Pair<String, Image> getTopImage(){
        return new Pair<>(imagePaths.peek(), imageStack.peek());
    }





}

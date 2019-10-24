package javafxtry.imgbackends.operations;

import javafxtry.imgbackends.utils.Constants;
import javafxtry.imgbackends.utils.DateTimeUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.Info;
import sun.awt.OSInfo;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class ImageManager {
    public static ImageManager manager = null;
    private ConvertCmd convertCmd;

    private ImageManager() {
        convertCmd = new ConvertCmd();
        if (OSInfo.getOSType() == OSInfo.OSType.WINDOWS) {
            convertCmd.setSearchPath(Constants.IMAGE_MAGIC_K_PATH);
        }
    }

    public static ImageManager getInstance() {
        if (manager == null) {
            manager = new ImageManager();
        }
        return manager;
    }

    public Info getImageInfo(String filepath) throws Exception {
        return new Info(filepath);
    }

    /**
     * @description: rotate image
     * @param: filepath, angle
     */
    public String rotate(String filepath, double angle) {
        IMOperation operation = new IMOperation();
        operation.addImage(filepath);
        operation.rotate(angle);
        String tmpFilepath = Constants.CACHES + DateTimeUtils.getCurrentDateTime() + ".jpg";
        operation.addImage(tmpFilepath);
        try {
            convertCmd.run(operation);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return tmpFilepath;
    }

    /**
     * @description: cut Image
     * @param: filepath, x1, y1 -- start axles, x2, y2 -- end axles
     */
    public String cutImage(String filepath, int x1, int y1, int x2, int y2) {
        IMOperation operation = new IMOperation();
        operation.addImage(filepath);
        operation.crop(x2 - x1, y2 - y1, x1, y1);
        String tmpFilepath = Constants.CACHES + DateTimeUtils.getCurrentDateTime() + ".jpg";
        operation.addImage(tmpFilepath);
        try {
            convertCmd.run(operation);
        } catch (Exception e) {

        }
        return tmpFilepath;
    }

    /**
     * @description: add text to Img
     * @param: filepath, text, font, gravity, pointsize, color
     */
    public String addImgText(String filepath, String text, String font, String gravity, int pointsize, String color) throws Exception {
        IMOperation operation = new IMOperation();
        operation.font(font).gravity(gravity).pointsize(pointsize).fill(color).draw(text);
        operation.addImage(filepath);
        String tmpFilepath = Constants.CACHES + DateTimeUtils.getCurrentDateTime() + ".jpg";
        operation.addImage(tmpFilepath);
        convertCmd.run(operation);
        return tmpFilepath;
    }

    public String resizeImg(String filepath, int width, int height) throws Exception {
        IMOperation operation = new IMOperation();
        operation.resize(width, height);
        operation.addImage(filepath);
        String tmpFilepath = Constants.CACHES + DateTimeUtils.getCurrentDateTime() + ".jpg";
        operation.addImage(tmpFilepath);
        convertCmd.run(operation);
        return tmpFilepath;
    }

    public String getThumbnailImg(String filepath,int width, int height) throws Exception {
        IMOperation operation = new IMOperation();
        operation.resize(width, height);
        operation.quality(100.0);
        operation.strip();
        operation.addImage(filepath);
        String tmpFilepath = Constants.CACHES + DateTimeUtils.getCurrentDateTime() + ".jpg";
        operation.addImage(tmpFilepath);
        convertCmd.run(operation);
        return tmpFilepath;
    }

}

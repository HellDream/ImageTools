package javafxtry.imgbackends.operations;

import javafxtry.imgbackends.utils.Constants;
import javafxtry.imgbackends.utils.DateTimeUtils;
import org.im4java.core.*;
import org.im4java.process.ArrayListOutputConsumer;
import sun.awt.OSInfo;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String cutImage(String filepath, int x1, int y1, int width, int height) {
        IMOperation operation = new IMOperation();
        operation.addImage(filepath);
        operation.crop(width, height, x1, y1);
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

    public void convertImage(String filepath, String outputpath) throws Exception {
        IMOperation operation = new IMOperation();
        operation.addImage(filepath);
        operation.addImage(outputpath);
        convertCmd.run(operation);
    }

    public Map<String, String> identifyImg(String filepath) throws Exception {
        Map<String, String> imageInfo = new HashMap<>();
        IMOperation operation = new IMOperation();
//        operation.identify();
        operation.format("%w,%h,%d/%f,%Q,%b,%e");
        operation.addImage(filepath);
        IdentifyCmd identifyCmd = new IdentifyCmd();
        ArrayListOutputConsumer output = new ArrayListOutputConsumer();
        identifyCmd.setOutputConsumer(output);
        identifyCmd.run(operation);
        List<String> cmdOutput = output.getOutput();
        String[] result = cmdOutput.get(0).split(",");
        if (result.length == 6) {
            imageInfo.put("Width", result[0]);
            imageInfo.put("Height", result[1]);
            imageInfo.put("Path", result[2]);
            imageInfo.put("Quality", result[3]);
            imageInfo.put("Size", result[4]);
            imageInfo.put("Type", result[5]);
        }
        return imageInfo;

    }

    public static void main(String[] args) throws Exception {
        Constants.initialize("D:\\ImageMagick-7.0.8-Q16");
        String filePath = "C:\\Users\\YU YE\\Pictures\\IMG_6077.JPG";
        ImageManager manager = ImageManager.getInstance();
        manager.identifyImg(filePath);
    }
}

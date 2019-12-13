package javafxtry.imgbackends.operations;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataReader;
import com.drew.metadata.Tag;
import javafxtry.imgbackends.utils.Constants;
import javafxtry.imgbackends.utils.DateTimeUtils;
import org.im4java.core.*;
import org.im4java.process.ArrayListOutputConsumer;
import org.im4java.process.OutputConsumer;
import sun.awt.OSInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
    public String addImgText(String filepath, String text, String font, int pointsize, String color) throws Exception {
        IMOperation operation = new IMOperation();
        operation.font(font).pointsize(pointsize).fill(color).draw(text);
        operation.addImage(filepath);
        String tmpFilepath = Constants.CACHES + DateTimeUtils.getCurrentDateTime() + ".jpg";
        operation.addImage(tmpFilepath);
        convertCmd.run(operation);
        return tmpFilepath;
    }

    public String addText(String filepath, String text, String font, int x, int y,Integer pointsize, String color) throws InterruptedException, IOException, IM4JavaException {
        IMOperation operation = new IMOperation();
        System.out.println(font);
        operation.font(font).pointsize(pointsize).fill(color).draw("text "+x+","+y+" \'"+text+"\'");
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
    public void getFontFamily(Map<String, String> fontMap)throws Exception{
        IMOperation operation = new IMOperation();
        operation.list("font");
        operation.encoding("UTF-8");
//        operation.label("@./chinese_words.utf8");
        ArrayListOutputConsumer output = new ArrayListOutputConsumer();
        convertCmd.setOutputConsumer(output);
        convertCmd.run(operation);
        List<String> out = output.getOutput();
        List<String> fonts = new ArrayList<>();
        List<String> family = new ArrayList<>();
        for(String s:out){
            s = s.trim();
            if(s.startsWith("Font: ")){
                int start = s.indexOf("Font: ");
                fonts.add(new String(s.substring(start+6).getBytes(), StandardCharsets.UTF_8));
            }else if(s.startsWith("family: ")){
                int start = s.indexOf("family: ");
                family.add(new String(s.substring(start+8).getBytes(), StandardCharsets.UTF_8));
            }
        }
        for(int i=0;i<family.size();i++){
            if(!fontMap.containsKey(family.get(i))){
//                System.out.print(family.get(i)+"        "+fonts.get(i));
                fontMap.put(family.get(i),fonts.get(i));
            }
            if(family.get(i).equals("Yu Gothic Regular & Yu Gothic UI Semilight")) break;
        }

    }

    public Map<String, String> getMetadata(String filepath) throws Exception {
        File file = new File(filepath);
        Metadata  metadata = ImageMetadataReader.readMetadata(file);
        Map<String, String> imageInfo = new HashMap<>();
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                //格式化输出[directory.getName()] - tag.getTagName() = tag.getDescription()
                imageInfo.put(tag.getTagName(), tag.getDescription());
            }
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.format("ERROR: %s", error);
                }
            }
        }
        return imageInfo;
    }

    public static void main(String[] args) throws Exception {
        Constants.initialize("D:\\ImageMagick-7.0.8-Q16");
        String filePath = "C:\\Users\\YU YE\\Pictures\\IMG_6077.JPG";
        ImageManager manager = ImageManager.getInstance();
//        String tmp = manager.addText(filePath, "abcdefg","Arial",100,100,23,"black");
//        System.out.println(tmp);
//        Map<String, String> map = new TreeMap<>();
        manager.getMetadata(filePath);
    }
}

package javafxtry.imgbackends.utils;

import java.util.Map;
import java.util.TreeMap;

public class Constants {
    public static String IMAGE_MAGIC_K_PATH;
    public static String CACHES = "./caches/";
    public static Map<String, String> FONT_MAP = new TreeMap<>();

    public static void initialize(String imageMagicKPath) {
        IMAGE_MAGIC_K_PATH = imageMagicKPath;
    }

}

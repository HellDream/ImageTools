package javafxtry.models;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import jdk.internal.dynalink.beans.StaticClass;

import java.util.HashMap;
import java.util.Map;
/**
 * @ClassName TextModel
 * @Description model of text
 * @Author Zhenyu YE
 * @Date 2019/12/12 21:09
 * @Version 1.0
 **/
public class TextModel {
    private Color textColor;
    private String fontFamily;
    private double fontSize;
    private String text;

    public Color getTextColor() {
        return textColor;
    }

    public String getRGBAColor() {
        StringBuilder sb = new StringBuilder();
        int r = (int)(textColor.getRed()*255);
        int g = (int)(textColor.getGreen()*255);
        int b = (int)(textColor.getBlue()*255);
        int op = (int)(textColor.getOpacity()*255);
        sb.append("rgba(")
                .append(r).append(",")
                .append(g).append(",")
                .append(b).append(",")
                .append(op).append(")");
        return sb.toString();
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public double getFontSize() {
        return fontSize;
    }

    public void setFontSize(Double fontSize) {
        this.fontSize = fontSize;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

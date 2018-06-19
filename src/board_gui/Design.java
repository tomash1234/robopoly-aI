package board_gui;

import javafx.scene.text.Font;

public class Design {
	
	private static Font f1;
	private static Font f2;
	private static Font f3;
	private static Font f4;
	public static final String DARK_COLOR = "#212121";
	public static final String LIGHT_BLACK_COLOR = "#484848";
	public static final String LIGHT_COLOR = "#707070";

	public static final String SECONDARY_COLOR = "#bf360c";
	
	
	public static void initFont(){
		 f1= Font.loadFont(ClassLoader.getSystemResourceAsStream("RobotoMono-Medium.ttf"), 14);
		 f2= Font.loadFont(ClassLoader.getSystemResourceAsStream("RobotoMono-Regular.ttf"), 14);
		 f3= Font.loadFont(ClassLoader.getSystemResourceAsStream("RobotoMono-Italic.ttf"), 14);
		 f4= Font.loadFont(ClassLoader.getSystemResourceAsStream("Roboto-Bold.ttf"), 14);
	}
	public static String getFontRegularCSS(){
		if(f2==null){
			initFont();
		}
		return "-fx-font-family: \'" + f2.getName() + "\';";
	}

	public static String getFontMediumCSS() {
		if (f1 == null) {
			initFont();
		}
		return "-fx-font-family: \'" + f1.getName() + "\';";
	}

	public static String getFontItalicCSS() {
		if (f3 == null) {
			initFont();
		}
		return "-fx-font-family: \'" + f3.getName() + "\';";
	}
	public static String getFontNotMonoCSS() {
		if (f4 == null) {
			initFont();
		}
		return "-fx-font-family: \'" + f4.getName() + "\';";
	}
	public static Font getFontRegular() {
		// TODO Auto-generated method stub
		return f2;
	}
	public static String getNormalTextWhiteCSS(){
		return "-fx-font-size: 12px;" + "-fx-text-fill: #ffffff;" +Design.getFontRegularCSS();
	}
	public static String getNormalTextWhiteItalicCSS(){
		return "-fx-font-size: 12px;" + "-fx-text-fill: #ffffff;" +Design.getFontItalicCSS();
	}
	public static String getButtonCSS(){
		return /*"-fx-background-color: " + Design.DARK_COLOR + ";"*/" -fx-text-fill: #ffffff; -fx-cursor: hand; -fx-font-weight: bold;" +Design.getFontMediumCSS();
	}
	public static String getEditTextCSS(){
		return "-fx-background-color: " + Design.DARK_COLOR + ";-fx-text-fill: #ffffff;  -fx-font-weight: bold;" +Design.getFontMediumCSS();
	}
	public static String getBackgroundColorCSS(String color){
		return "-fx-background-color: " + color + ";";
	}
	public static String getButtonColorCSS(){
		return "-fx-background-color:  "+Design.SECONDARY_COLOR +"; -fx-text-fill: #fff; -fx-cursor: hand; -fx-font-weight: bold;" +Design.getFontMediumCSS();
	}
	public static String getButtonSmallCSS(){
		return "-fx-background-color: " + Design.DARK_COLOR + "; -fx-text-fill: #ffffff; -fx-cursor: hand; -fx-font-weight: bold;-fx-font-size: 10px;" +Design.getFontMediumCSS();
	}
}

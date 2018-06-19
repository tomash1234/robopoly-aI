package game_mechanics;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Plat {
	
	public static final int START = 0;
	public static final int VISITING_JAIL = 1;
	public static final int PARKING = 2;
	public static final int GO_TO_JAIL = 3;
	public static final int CHANCE = 4;
	public static final int TAX = 5;
	public static final int BUYABLE = 6;
	public static final int FINANCY = 7;
	
	public static final String TYPES[] = new String[]{"START", "VISITING JAIL", "PARKING", "GO_TO_JAIL", "CHANCE", "TAX", "BUYABLE", "FINANCY"};
	

	public static Color GROUP_COLOR[] = new Color[]{null, Color.web("#f47d2a"), null, Color.web("#0096bb"), Color.web("#c900c9"), null, Color.web("#77c900"),
			Color.web("#c90000"), Color.web("#ffe400"), Color.web("#006900"), Color.web("#003ebb")};
	

	public static String GROUP_COLOR_STRING[] = new String[]{"#ffffff", "#f47d2a", "#ffffff", "#0096bb", "#c900c9", "#ffffff", "#77c900",
			"#c90000", "#ffe400", "#006900", "#003ebb"};
	
	private final  int value;
	private final  String name;
	private final  Color color;
	private final  int id;
	private final int type;
	//private Image image;
	private String image;
	private final int group;
	
	public Plat(int id, String name, int price, Color color, String image, int group) {
		this.id = id;
		this.value = price;
		this.group = group;
		this.name = name;
		this.color = GROUP_COLOR[group];
		this.image = image;
		this.type = getType(id);
	}
	public int getId() {
		return id;
	}
	public int getPlatType(){
		return type;
	}
	public String getImage() {
		return image;
	}
	public static int getType(int id){
		if(id==0){
			return START;
		}else if(id==10){
			return VISITING_JAIL;
		}else if(id==20){
			return PARKING;
		}else if(id==30){
			return GO_TO_JAIL;
		}else if(id==2 || id== 18|| id==33){
			return FINANCY;
		}else if(id==7 || id== 22|| id==36){
			return CHANCE;
		}else if(id==4 || id== 38){
			return TAX;
		}else{
			return BUYABLE;
		}
	}
	
	public int getGroup() {
		return group;
	}
	public int getValue() {
		return value;
	}
	public String getName() {
		return name;
	}
	public String getPrice(){
		if(!isShowPrice()){
			return null;
		}
		return value + " BTC";
	}
	public Color getColor() {
		if(type!=BUYABLE){
			return null;
		}
		return color;
	}
	public boolean isShowText(){
		return type==BUYABLE || type==TAX;
	}
	public boolean isShowPrice(){
		return type==BUYABLE ||type==TAX;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Plat " + id + ", " + TYPES[type] + ", " + value;
	}
}

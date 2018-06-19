package board_gui;

import javafx.scene.image.Image;

public class Figure extends PaintableElement{
	


	public static final String IMG_FIG[] = new String[]{"fig1.png", "fig2.png", "fig3.png", "fig4.png", "fig5.png"};
	
	private int playerId;
	private Field[] fields;
	private int figurkaType;
	private int is;;
	
	public Figure(int playerId, int figurkaType, Field[] fields) {
		super(0, 0, 40, 40, new Image(ClassLoader.getSystemResourceAsStream(IMG_FIG[figurkaType])));
		this.figurkaType = figurkaType;
		this.playerId = playerId;
		this.fields = fields;
	}
	
	public void setTo(int fieldID){
		is = fieldID;
		setPosX(fields[fieldID].getPosX());
		setPosY(fields[fieldID].getPosY()+fields[fieldID].getsY()/2- fields[fieldID].getsY()/8.*playerId-getSizeY()/2);
		setPivotX(fields[fieldID].getPosX());
		setPivotY(fields[fieldID].getPosY());
		setRotation((fields[fieldID].getId()/10)*90);
	}
	public int getIs() {
		return is;
	}
	public double getPosXForField(int fieldID){

		return fields[fieldID].getPosX();
	}
	public double getPosYForField(int fieldID){

		return fields[fieldID].getPosY();//+fields[fieldID].getsY()/2- fields[fieldID].getsY()/8.*playerId-getSizeY()/2;
	}
	
	public static Image getImage(int type) {
		return new Image(ClassLoader.getSystemResourceAsStream(IMG_FIG[type]));
	}
}

package board_gui;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * 
 * @author Tomas Hromada
 *
 */
public class PaintableElement {
	public static final int TYPE_RECTANGLE = 0;
	public static final int TYPE_IMAGE = 1;
	public static final int TYPE_TEXT = 2;
	
	private double sizeX, sizeY, rotation;
	private double posX, posY, pivotX, pivotY;
	private String text;
	private Image image;
	private Color color;
	
	private int type;
	/**
	 * Constructor for creating a rectangle paintable element
	 * @param posX
	 * @param posY
	 * @param sizeX
	 * @param sizeY
	 * @param color
	 */
	public PaintableElement(double posX, double posY, double sizeX, double sizeY, Color color) {
		this.posX = posX;
		this.posY = posY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.color = color;
		type = TYPE_RECTANGLE;
		pivotX = posX;
		pivotY = posY;
	}
	
	public PaintableElement(double posX, double posY, String text, double sizeX, double textSize, Color color) {
		this.posX = posX;
		this.sizeY = textSize;
		this.posY = posY;
		this.color = color;
		this.sizeX = sizeX;
		type = TYPE_TEXT;
		this.text = text;
		pivotX = posX;
		pivotY = posY;
	}
	
	public PaintableElement(double posX, double posY,  double sizeX, double sizeY, Image image) {
		this.posX = posX;
		this.posY = posY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.image = image;
		type = TYPE_IMAGE;
		pivotX = posX;
		pivotY = posY;
	}
	
	public void setPosX(double posX) {
		this.posX = posX;
	}
	public void setPosY(double posY) {
		this.posY = posY;
	}
	

	public Color getColor() {
		return color;
	}
	public Image getImage() {
		return image;
	}
	public double getPosX() {
		return posX;
	}
	public double getPosY() {
		return posY;
	}
	public double getSizeX() {
		return sizeX;
	}
	public double getSizeY() {
		return sizeY;
	}
	public String getText() {
		return text;
	}
	public int getType() {
		return type;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	public double getRotation() {
		return rotation;
	}
	
	public double getPivotX() {
		return pivotX;
	}
	public double getPivotY() {
		return pivotY;
	}
	public void setPivotX(double pivotX) {
		this.pivotX = pivotX;
	}
	public void setPivotY(double pivotY) {
		this.pivotY = pivotY;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}

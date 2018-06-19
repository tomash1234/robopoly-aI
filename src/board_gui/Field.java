package board_gui;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Field {
	
	private PaintableElement pText1, pText2,myImage, houses;
	public static Image[] houseImages = new Image[6];
	private double posX = 0;
	private double posY =0;
	private double sX, sY;
	private Color color;
	private int id;
	private int houseNumber = 0;
	
	public Field(Color boardColor, int id, double boardWidth, double boardGap, double padding, boolean strip, Color stripColor, String text1, String text2, Image image){
		this.color = stripColor;
		this.id = id;
		sY = (boardGap/2-2*padding);
		sX = (boardWidth-boardGap-8*padding)/9;
		double width = (boardWidth-boardGap)/9;
		
		if(id%10==0){
			sX= sY;
			switch(id){
			case 0:
				posX = boardWidth/2-boardGap/4;
				posY = -boardWidth/2+boardGap/4;
				break;
			case 10:
				posX = -boardWidth/2+boardGap/4;
				posY = -boardWidth/2+boardGap/4;
				break;
			case 20:
				posX = -boardWidth/2+boardGap/4;
				posY = boardWidth/2-boardGap/4;
				break;
			case 30:
				posX = boardWidth/2-boardGap/4;
				posY = boardWidth/2-boardGap/4;
				break;
			}
		}else{
			/*double stripSzX = sX;
			double stripSzY = 20;
			double stripPsX = 10;
			double stripPsY = 10;
			
			double bstripSzX = sX;
			double bstripSzY = 2;
			double bstripPsX = 10;
			double bstripPsY = 10;*/

			
			if(id<10){
				posX = boardWidth/2-boardGap/2-(id-0.5)*width;
				posY = -boardWidth/2+boardGap/4;
			}else if(id>10&&id<20){
				posX = -boardWidth/2+boardGap/4;
				posY = -boardWidth/2+boardGap/4+(id-9-0.5)*width+padding/2;
			}else if(id>=20&&id<30){
				posX = -boardWidth/2+boardGap/4+(id-19-0.5)*width+padding/2;
				posY = boardWidth/2-boardGap/4;
			}else if(id>30){
				posX = boardWidth/2-boardGap/4;
				posY = boardWidth/2-boardGap/4-(id-29-0.5)*width-padding/2;
			}
			if(strip){
				/*stripPsX= posX;
				stripPsY= posY+sY/2-stripSzY/2;
				
				bstripPsX= posX;
				bstripPsY =stripPsY -stripSzY/2 - bstripSzY/2;*/
				//colorStrip = new PaintableElement(stripPsX, stripPsY, stripSzX, stripSzY, stripColor);
				//blackStrip = new PaintableElement(bstripPsX, bstripPsY, bstripSzX, bstripSzY, Color.BLACK);
			}
		}
		
		//background = new PaintableElement(posX, posY, sX, sY, boardColor);

		/*if(colorStrip!=null){
			colorStrip.setPivotX(posX);
			colorStrip.setPivotY(posY);
			blackStrip.setPivotX(posX);
			blackStrip.setPivotY(posY);
			colorStrip.setRotation(90*(id/10));
			blackStrip.setRotation(90*(id/10));
			
		}*/
		if(text1!=null){
			pText1 = new PaintableElement(posX, posY+sY/6+(text2==null?sY/8.:0), text1, sX-8, 10,Color.BLACK);
			pText1.setPivotX(posX);
			pText1.setPivotY(posY);
			pText1.setRotation(90*(id/10));
		}
		if(text2!=null){
			pText2 = new PaintableElement(posX, posY-sY/2, text2, sX-8, 10,Color.BLACK);
			pText2.setPivotX(posX);
			pText2.setPivotY(posY);
			pText2.setRotation(90*(id/10));
		}
		if(image!=null){
			myImage = new PaintableElement(posX, posY-sY/8, sX/3*2, sX/3*2, image);
			myImage.setPivotX(posX);
			myImage.setPivotY(posY);
			myImage.setRotation(90*(id/10));
		}
		if(image!=null){
			houses = new PaintableElement(posX, posY+sY/8, sX/1.5, sX/3*2, houseImages[0]);
			houses.setPivotX(posX); 
			houses.setPivotY(posY);
			houses.setRotation(90*(id/10)); 
		}
		//background.setRotation(90*(id/10));
		
	}
	public static void setHouseImages(Image h0,Image h1, Image h2, Image h3, Image h4, Image h5 ) {
		houseImages[0] = h0;
		houseImages[1] = h1;
		houseImages[2] = h2;
		houseImages[3] = h3;
		houseImages[4] = h4;
		houseImages[5] = h5;
	}
	
	public void setHouseNumber(int houseNumber) {
		this.houseNumber = houseNumber;
		houses.setImage(houseImages[houseNumber]);
	}
	public int getId() {
		return id;
	}
	public boolean isClickedOnPlat(double x, double y){
		if(id/10%2==0){
			if(x>=posX-sX/2 && x<=posX+sX/2 && y>=posY-sY/2 && y<=posY+sY/2 ){
				return true;
			}
		}else if(id/10%2==1){
			if(x>=posX-sY/2 && x<=posX+sY/2 && y>=posY-sX/2 && y<=posY+sX/2 ){
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ID: " + id + ", " + color;
	}

	public PaintableElement getLabel1() {
		return pText1;
	}
	public PaintableElement getLabel2() {
		return pText2;
	}
	public PaintableElement getMyImage() {
		return myImage;
	}
	
	public PaintableElement getHouses() {
		return houses;
	}
	public double getPosX() {
		return posX;
	}
	public double getPosY() {
		return posY;
	}
	public double getsY() {
		return sY;
	}
}

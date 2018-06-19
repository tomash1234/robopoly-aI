package board_gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.naming.directory.InvalidAttributeValueException;

import game_mechanics.GraphicsController;
import game_mechanics.Player;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;

public class GameBoard extends Canvas implements GraphicsController{
	
	private Color backgroundColor = Color.web("#9e9e9e");
	private Color boardColor = Color.MEDIUMTURQUOISE;

	private double zoom = 1;
	private double psX, psY, oldX, oldY, cX,cY;
	private Field fields[] = new Field[40];
	private Figure figures[];
	private List<PaintableElement> paintableElements = Collections.synchronizedList(new ArrayList<>());
	
	/**
	 * Creates canvas which fills up its parent
	 */
	public GameBoard() {
		init();
	}
	
	private void init(){
		initKeyAndMouseInput();
		widthProperty().addListener(evt -> update());
		heightProperty().addListener(evt -> update());
		
	}
	
	public Figure getFigure(int id){
		return figures[id];
	}
	//https://translate.google.com/translate_tts?ie=UTF-8&tl=cs-CZ&client=tw-ob&q=Te%C4%8De%20ti%20do%20bod?
	private void initKeyAndMouseInput() {
		addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (event.getDeltaY() > 0) {

					zoom *= 1.1;
					psX *= 1.1;
					psY *= 1.1;
				} else if (event.getDeltaY() < 0) {
					zoom /= 1.1;
					psX /= 1.1;
					psY /= 1.1;
				}
				 update();
			}
		});
		addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				oldX = mouseEvent.getX();
				oldY = mouseEvent.getY();
				cX = mouseEvent.getX();
				cY = mouseEvent.getY();
			}
		});
		
		addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				if(!(oldX == cX && oldY== cY)){
					return;
				}
				double x = ((mouseEvent.getX()-getWidth()/2)/zoom)-psX/zoom;
				double y = -(((mouseEvent.getY()-getHeight()/2)/zoom)-psY/zoom);
				
				for(Field field:fields){
					if(field.isClickedOnPlat(x, y)){
						//
						break;
					}
				}
			}
		});

		addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				oldX = mouseEvent.getX();
				oldY = mouseEvent.getY();
				setCursor(Cursor.DEFAULT);
				 update();
			}
		});

		addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				double x = mouseEvent.getX();
				double y = mouseEvent.getY();
				psX += x - oldX;
				psY += y - oldY;
				oldX = x;
				oldY = y;
				setCursor(Cursor.MOVE);
				 update();

			}
		});
	}
	
	private void createBoard(){

		/*PaintableElement board = new PaintableElement(0, 0, 800, 800, Color.BLACK);
		PaintableElement centralSquare = new PaintableElement(0, 0, 550, 550, boardColor);
		paintableElements.add(board);
		paintableElements.add(centralSquare);
		*/
		for (int i = 0; i < fields.length; i++) {
			fields[i] = new Field(boardColor, i, 800, 800-550, 2, true, Color.RED, "Farma na\n bitcoiny", "4500 BTC", loadImage("def.png"));
			//if(i%10==0){
				/*paintableElements.add(fields[i].getBackground());
				if(fields[i].getColorStrip()!=null){
					paintableElements.add(fields[i].getColorStrip());
					paintableElements.add(fields[i].getBlackStrip());
				}*/
				if(fields[i].getLabel1()!=null){
					paintableElements.add(fields[i].getLabel1());
				}
				if(fields[i].getLabel2()!=null){
					paintableElements.add(fields[i].getLabel2());
				}
				if(fields[i].getMyImage()!=null){
					paintableElements.add(fields[i].getMyImage());
				}
				/*if(fields[i].getLabel2()!=null){
					paintableElements.add(fields[i].getLabel2());
				}*/
			//}
		}
	}

	public static Image loadImage(String file){
		return new Image(ClassLoader.getSystemResourceAsStream(file));
	}
	
	
	public synchronized void update() {
		// TODO Auto-generated method stub
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//clears screen
				GraphicsContext gC = getGraphicsContext2D();
				gC.setFill(backgroundColor);
				gC.fillRect(0, 0, getWidth(), getHeight());
				
				
				for(PaintableElement paintableElement: paintableElements){
					drawElement(gC, paintableElement);
				}
			}
		});
		
	}
	
	/**
	 * Draws element on the board
	 * @param gC
	 * @param paintableElement
	 */
	public synchronized void drawElement(GraphicsContext gC, PaintableElement paintableElement){
		double perspectiveSizeX = paintableElement.getSizeX() * zoom;
		double perspectiveSizeY = paintableElement.getSizeY() * zoom;
		double perspectivePosX = paintableElement.getPosX() * zoom;
		double perspectivePosY = -paintableElement.getPosY() * zoom;

		double positionOnScreenX = perspectivePosX+psX+getWidth()/2-perspectiveSizeX/2;
		double positionOnScreenY = perspectivePosY+psY+getHeight()/2-perspectiveSizeY/2;

		gC.save();
		//rotating
		rotate(gC, paintableElement.getRotation(), perspectiveSizeX/2-(paintableElement.getPivotX()-paintableElement.getPosX())*zoom,
				perspectiveSizeY/2-(paintableElement.getPivotY()-paintableElement.getPosY())*zoom);
		double[] rotationXY = calculateRotatedCords(positionOnScreenX, positionOnScreenY, -paintableElement.getRotation());
		positionOnScreenX = rotationXY[0];
		positionOnScreenY = rotationXY[1];
		
		
		switch(paintableElement.getType()){
		case PaintableElement.TYPE_RECTANGLE:
			gC.setFill(paintableElement.getColor());
			gC.fillRect(positionOnScreenX, positionOnScreenY, perspectiveSizeX, perspectiveSizeY);
			break;
		case PaintableElement.TYPE_TEXT:
			gC.setFont(new Font(perspectiveSizeY));
			gC.setFill(paintableElement.getColor());
			gC.fillText(paintableElement.getText(), positionOnScreenX, positionOnScreenY, perspectiveSizeX);
			
			break;
		case PaintableElement.TYPE_IMAGE:
			gC.drawImage(paintableElement.getImage(), positionOnScreenX, positionOnScreenY, perspectiveSizeX, perspectiveSizeY);
			break;
		}
		gC.restore();
	}
	/**
	 * calculates new cords after rotation
	 * @param x posX
	 * @param y posY 
	 * @param alpha angle in degrees
	 * @return double array with two cords [x,y]
	 */
	public static double[] calculateRotatedCords(double x, double y, double alpha){
		alpha = Math.toRadians(alpha);
		return new double[]{x*Math.cos(alpha)-y*Math.sin(alpha), x*Math.sin(alpha)+y*Math.cos(alpha)};
	}
	
	private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

	@Override
	public void initBoard() {
		// TODO Auto-generated method stub

		PaintableElement board = new PaintableElement(0, 0, 800, 800, loadImage("board.png"));
		Field.setHouseImages(loadImage("h0.png"), loadImage("h1.png"),  loadImage("h2.png"), loadImage("h3.png"), loadImage("h4.png"), loadImage("h5.png"));
		paintableElements.add(board);
	}

	@Override
	public void initPlat(int id,String text1, String price, String imageUrl, Color color) {
		// TODO Auto-generated method stub
		
		Image image = null;
		if(imageUrl!=null){
			if(imageUrl.contains(File.separator)){
				try {
					image = new Image(new FileInputStream(imageUrl));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					image = new Image(ClassLoader.getSystemResourceAsStream(imageUrl));
				}
			}else{
				image = new Image(ClassLoader.getSystemResourceAsStream(imageUrl));
			}
		}
		
		fields[id] = new Field(boardColor, id, 800, 800-550, 2, color!=null, color, text1, price, image);
		if(fields[id] .getLabel1()!=null){
			paintableElements.add(fields[id].getLabel1());
		}
		if(fields[id].getLabel2()!=null){
			paintableElements.add(fields[id].getLabel2());
		}
		if(fields[id].getMyImage()!=null){
			paintableElements.add(fields[id].getMyImage());
		}
		if(fields[id].getHouses()!=null){
			paintableElements.add(fields[id].getHouses());
		}
	}

	@Override
	public void initPlayer(Player[] players) {
		// TODO Auto-generated method stub
		figures = new Figure[players.length];
		for (int i = 0; i < players.length; i++) {
			figures[i] = new Figure(players[i].getPlayerId(), players[i].getFigurka(), fields);
			figures[i].setTo(0);
			paintableElements.add(figures[i]);
		}
	}

	@Override
	public void moveWithFigure(int playerId, int destination) {
		// TODO Auto-generated method stub
		/*if(playerId<0||playerId>=figures.length){
			throw new InvalidAttributeValueException("Player's ID (" +playerId +") does not exit");
		}
		if(destination<0||destination>=40){
			throw new IllegalArgumentException("Destination to move (" + destination+") does not exit");
		}*/
		figures[playerId].setTo(destination);
	}

	@Override
	public void setHouse(int platId, int number) {
		// TODO Auto-generated method stub
		fields[platId].setHouseNumber(number);
		update() ;
	}

}

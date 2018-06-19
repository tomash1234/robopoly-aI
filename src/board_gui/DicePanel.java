package board_gui;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class DicePanel{
	private HBox hbox = new HBox(4);
	
	private Dice dice1, dice2;
	public DicePanel(){
		dice1 = new Dice();
		dice2 = new  Dice();
		hbox.getChildren().addAll(dice1.getCanvas(), dice2.getCanvas());
		hbox.setPadding(new Insets(8));
		hbox.setAlignment(Pos.CENTER);
		hbox.setStyle("-fx-background-color: "+Design.DARK_COLOR+";-fx-cursor: hand;");
	}

	public HBox getHbox() {
		return hbox;
	}
	
	public void setNumbers(int a, int b){
		dice1.set(a);
		dice2.set(b);
	}

	public class Dice {
		private Canvas canvas;
		
		public Dice() {
			// TODO Auto-generated constructor stub
			canvas = new Canvas(80,80);
			GraphicsContext gC= canvas.getGraphicsContext2D();
			gC.setFill(Color.BLACK);
			gC.fillRect(0, 0,canvas.getWidth(), canvas.getHeight());
			gC.setFill(Color.WHITESMOKE);
			gC.fillRoundRect(0, 0, canvas.getWidth(), canvas.getHeight(), canvas.getWidth()/10, canvas.getHeight()/10);
			
			drawNumber(6);
		}
		public Canvas getCanvas() {
			return canvas;
		}
		
		public void set(int num){
			GraphicsContext gC= canvas.getGraphicsContext2D();
			gC.setFill(Color.WHITESMOKE);
			gC.fillRoundRect(0, 0, canvas.getWidth(), canvas.getHeight(), canvas.getWidth()/10, canvas.getHeight()/10);
			
			drawNumber(num);
			
		}
		private void drawPoint(GraphicsContext gC, int x, int y){
			gC.setFill(Color.BLACK);
			
			gC.fillOval(loc(x, canvas.getWidth())-canvas.getWidth()/10, loc(y, canvas.getHeight())-canvas.getHeight()/10, canvas.getWidth()/5, canvas.getHeight()/5);
			
		}
		private double loc(int x, double size){
			if(x==0){
				return size/6;
			}
			if(x==1){
				return size/2;
			}
			if(x==2){
				return size/6*5;
			}
			return 0;
		}
		
		private void drawNumber(int number){
			switch (number) {
			case 1:
				drawPoint(canvas.getGraphicsContext2D(), 1,1);
				break;
			case 2:
				drawPoint(canvas.getGraphicsContext2D(), 0,0);
				drawPoint(canvas.getGraphicsContext2D(), 2,2);
				break;
			case 3:
				drawPoint(canvas.getGraphicsContext2D(), 0,0);
				drawPoint(canvas.getGraphicsContext2D(), 1,1);
				drawPoint(canvas.getGraphicsContext2D(), 2,2);
				break;
			case 4:
				drawPoint(canvas.getGraphicsContext2D(), 0,0);
				drawPoint(canvas.getGraphicsContext2D(), 2,2);
				drawPoint(canvas.getGraphicsContext2D(), 0,2);
				drawPoint(canvas.getGraphicsContext2D(), 2,0);
				break;
			case 5:
				drawPoint(canvas.getGraphicsContext2D(), 0,0);
				drawPoint(canvas.getGraphicsContext2D(), 2,2);
				drawPoint(canvas.getGraphicsContext2D(), 1,1);
				drawPoint(canvas.getGraphicsContext2D(), 0,2);
				drawPoint(canvas.getGraphicsContext2D(), 2,0);
				break;
			case 6:
				drawPoint(canvas.getGraphicsContext2D(), 0,0);
				drawPoint(canvas.getGraphicsContext2D(), 2,2);
				drawPoint(canvas.getGraphicsContext2D(), 0,1);
				drawPoint(canvas.getGraphicsContext2D(), 2,1);
				drawPoint(canvas.getGraphicsContext2D(), 0,2);
				drawPoint(canvas.getGraphicsContext2D(), 2,0);
				break;
			}
		}
		
	}
}

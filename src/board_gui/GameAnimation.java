package board_gui;

import game_mechanics.GraphicsRespond;

public class GameAnimation{
	
	private GameBoard gameBoard;
	private GraphicsRespond graphicsRespond;
	private long movingTime = 1000;
	private long startTime;
	
	
	
	
	public GameAnimation(GameBoard gameBoard, long movingTime) {
		super();
		this.movingTime = movingTime;
		this.gameBoard = gameBoard;
	}

public void setGraphicsRespond(GraphicsRespond graphicsRespond) {
	this.graphicsRespond = graphicsRespond;
}


	public  void startToMoving(int playerId, int destination) {
		/*synchronized (gameBoard) {
			*/
		Figure fig = gameBoard.getFigure(playerId);
		startTime = System.currentTimeMillis();
		int steps = (int) (movingTime*50/1000);
		if(steps==0){
			steps = 1;
		}
/*
		Thread t1 = new Thread() {
			@Override
			public void run() { 
				// TODO Auto-generated method stub
				super.run();
				synchronized (gameBoard) {*/
				double[] xy = getXY(fig.getPosXForField(destination), fig.getPosYForField(destination), destination);
				double[] xya = getXY(fig.getPosX(), fig.getPosY(), fig.getIs());
				double posXS = (xy[0]-xya[0])/steps;
				double posYS = (xy[1]-xya[1])/steps;
				
				while (System.currentTimeMillis() - startTime < movingTime) {
					fig.setRotation(0);
					fig.setPosX(fig.getPosX()+posXS);
					fig.setPosY(fig.getPosY()+posYS);
					gameBoard.update();
					try {
						Thread.sleep(movingTime/steps);
					} catch (InterruptedException e) {
					}
				}

			
				fig.setTo(destination);
				gameBoard.update();
				graphicsRespond.onFigureReachedRightPlace();
		}
	public double[] getXY(double posX, double posY, int i){
		return GameBoard.calculateRotatedCords(posX, posY, 0);
	}
}

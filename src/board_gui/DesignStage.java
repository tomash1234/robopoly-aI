package board_gui;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class DesignStage {

	private double grapX=-10, grapY=-10;
	private Stage stage = new Stage();
	private Label labelTitle = new Label();
	private VBox vbox = new VBox();
	private Scene scene;
	private VBox all = new VBox();
	private HBox titleBox = new HBox();
	public DesignStage(String title, boolean closeButton, Node body){
		init(title, closeButton, false, body);
	}
	public DesignStage(String title, boolean closeButton, Node body, Stage stage){
		this.stage = stage;
		init(title, closeButton, false, body);
	}
	public DesignStage(String title, boolean closeButton,  boolean min,Node body){
		init(title, closeButton, min, body);
	}
	
	
	public void setTitleBoxColor(String textColor, String backgroundColor){
		titleBox.setStyle(titleBox.getStyle() +"-fx-background-color: " + backgroundColor + ";");
		labelTitle.setStyle(labelTitle.getStyle() + Design.getNormalTextWhiteCSS() + "-fx-font-size: 14px;-fx-text-fill: " + textColor + ";" );
		
	}
	
	public void setTitleBoxColorDark(String backgroundColor){
		Color c = Color.web(backgroundColor);
		Color a = c.darker().darker().darker().darker();
		titleBox.setStyle(titleBox.getStyle() +"-fx-background-color: rgb(" + a.getRed()*255 + ", " + a.getGreen()*255 + ", " + a.getBlue()*255  + ");");
		
	}
	
	private void init(String title, boolean closeButton,boolean minim, Node body){
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.initModality(Modality.APPLICATION_MODAL);
		//all.setStyle("-fx-background-color: white;");
		all.setPadding(new Insets(16));
		all.setStyle("-fx-border-insets: 16;-fx-background-insets: 16;-fx-effect: dropshadow(three-pass-box, rgba(0, 0,0, .6), 12, 0.4, 1, 0.6);");
		
		titleBox.setOnMouseReleased(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				 grapX=-10;
				 grapY=-10;
			}
			
		});
		titleBox.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if(grapX==-10 && grapY==-10){
					grapX = event.getX();
					grapY = event.getY();
				}
				
				double x = event.getScreenX();
				double y = event.getScreenY();
				
				stage.setX(x-grapX);
				stage.setY(y-grapY);
			}
		});
		titleBox.setPadding(new  Insets(6));
		titleBox.setStyle("-fx-background-color: " + "black" + ";");
		labelTitle.setText(title);
		labelTitle.setStyle(Design.getNormalTextWhiteCSS() + "-fx-font-size: 14px;");
		Button b = new Button("×");
		b.setStyle(Design.getButtonCSS());
		Button b2 = new Button("_");
		b2.setStyle(Design.getButtonCSS());
		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		titleBox.getChildren().addAll(labelTitle,region);
		if(minim){
			titleBox.getChildren().add(b2);
			b2.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					minimalize();
				}
			});
		}
		if(closeButton){
			titleBox.getChildren().add(b);
			b.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					close();
				}
			});
		}
		
		vbox.getChildren().addAll(titleBox, body);
		vbox.setStyle("-fx-background-color: " + Design.LIGHT_BLACK_COLOR + ";");
		
		all.getChildren().addAll(vbox);
		
		
	stage.setOnShowing(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				Timeline timeline = new Timeline();
		        KeyFrame key = new KeyFrame(Duration.millis(150),
		                       new KeyValue (stage.getScene().getRoot().opacityProperty(), 1)); 
		        timeline.getKeyFrames().add(key);   
		        //timeline.setOnFinished((ae) -> stage.close()); 
		        timeline.play();
			}
		});
		scene= new Scene(all);
		scene.setFill(null);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		scene.getRoot().setOpacity(0);
		
		stage.setScene(scene);
	}
	
	public void initModality(Modality modality){
		stage.initModality(modality);
	}
	
	public void setTitle(String t){
		labelTitle.setText(t);
	}
	
	/*public void addBody(){
		vbox.getChildren().add(body);
		//scene= new Scene(all);
		
		stage.setScene(scene);
	}*/
	
	public void showAndWait(){
		
		stage.showAndWait();
	}
	public void minimalize(){
		stage.setIconified(true);
	}
	public void close(){
		Timeline timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(150),
                       new KeyValue (stage.getScene().getRoot().opacityProperty(), 0)); 
        timeline.getKeyFrames().add(key);   
        timeline.setOnFinished((ae) -> stage.close()); 
        timeline.play();

	}

	public void show() {
		// TODO Auto-generated method stub
		stage.show();
	}

	public boolean isShowing() {
		// TODO Auto-generated method stub
		return stage.isShowing();
	}
	public Stage getStage(){
		return stage;
	}
}

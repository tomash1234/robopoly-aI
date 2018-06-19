package board_gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class OptionDialog {
	
	/*private Stage stage = new Stage();
	*/private boolean answer;
	/*private double grapX=-10, grapY=-10;*/
	private DesignStage designStage;
	public OptionDialog(String title, String message, boolean yesNo){
		/*stage.initStyle(StageStyle.TRANSPARENT);
		stage.initModality(Modality.APPLICATION_MODAL);
		VBox all = new VBox();
		//all.setStyle("-fx-background-color: white;");
		all.setPadding(new Insets(16));
		VBox vbox = new VBox();
		HBox titleBox = new HBox();
		all.setStyle("-fx-border-insets: 16;-fx-background-insets: 16;-fx-effect: dropshadow(three-pass-box, rgba(0, 0,0, .6), 12, 0.2, 1, 0.5);");
		
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
		//vbox.setStyle("-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );");
		titleBox.setPadding(new  Insets(6));
		titleBox.setStyle("-fx-background-color: " + "black" + ";");
		Label label = new Label(title);
		label.setStyle(Design.getNormalTextWhiteCSS() + "-fx-font-size: 14px;");
		titleBox.getChildren().add(label);
		vbox.getChildren().addAll(titleBox);
		vbox.setStyle("-fx-background-color: " + Design.LIGHT_BLACK_COLOR + ";");*/
		VBox vbox = new VBox();
		Label mes = new Label(message);
		mes.setWrapText(true);
		mes.setStyle(Design.getNormalTextWhiteCSS());
		mes.setPadding(new Insets(18));
		//all.getChildren().addAll(vbox);
		vbox.setPrefWidth(380);
		HBox buttonBox = new HBox(4);
		buttonBox.setPadding(new Insets(8));
		buttonBox.setAlignment(Pos.CENTER);
		if(yesNo){
			Button but1 = new Button("ANO");
			but1.setPrefWidth(100);
			but1.setStyle(Design.getButtonCSS());
			Button but2 = new Button("NE");
			but2.setPrefWidth(100);
			but2.setStyle(Design.getButtonCSS());
			buttonBox.getChildren().addAll(but1, but2);
			but1.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					close(true);
				}
			});
			but2.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					close(false);
				}
			});
		}else{

			Button but3 = new Button("ZAVŘÍT");
			but3.setPrefWidth(100);
			but3.setStyle(Design.getButtonCSS());
			but3.setStyle(Design.getButtonCSS());
			buttonBox.getChildren().addAll(but3);
			but3.setOnAction(new  EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					
					close(true);
				}
			});
		}
		
		vbox.getChildren().addAll(mes, buttonBox);
		designStage = new DesignStage(title, false,vbox);
		//designStage.addBody(vbox);

		/*stage.setOnShowing(new EventHandler<WindowEvent>() {
			
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
		Scene scene = new Scene(all);scene.setFill(null);
		scene.getRoot().setOpacity(0);
		
		stage.setScene(scene);*/
		
		
	}
	
	public void close(boolean retur){
		answer = retur;
		/*Timeline timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(150),
                       new KeyValue (stage.getScene().getRoot().opacityProperty(), 0)); 
        timeline.getKeyFrames().add(key);   
        timeline.setOnFinished((ae) -> stage.close()); 
        timeline.play();*/
		designStage.close();

		
	}
	
	public void setTitleColor(String back){
		designStage.setTitleBoxColor("white", back);
	}
	public boolean showAndWait(){
		designStage.showAndWait();
		//stage.showAndWait();
		return answer;
		
	}

	public void show(){
		designStage.show();
	}
}

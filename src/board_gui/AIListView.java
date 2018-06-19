package board_gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import board_gui.Lobby.AIItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class AIListView {
	
	private DesignStage designStage;
	private List<AIItem> aiPlayers = new ArrayList<>();
	private ListView<String> listView = new ListView<>();
	
	public AIListView(){
		VBox vboxAll = new VBox();
		
		

		Button add = new Button("PŘIDAT AI");
		Button remove = new Button("ODEBRAT AI");
		Button close = new Button("ZAVŘÍT");
		add.setStyle(Design.getButtonCSS());
		remove.setStyle(Design.getButtonCSS());
		close.setStyle(Design.getButtonCSS());
		
		HBox hbox= new HBox(4);
		hbox.getChildren().addAll(add, remove, close);
		hbox.setStyle(Design.getBackgroundColorCSS(Design.DARK_COLOR));
		hbox.setPadding(new Insets(4));
		hbox.setAlignment(Pos.CENTER);
		
		vboxAll.getChildren().addAll(listView,hbox);
		
		update();
		
		designStage = new DesignStage("Přidat/Odebrat AI", true, vboxAll);
		
		add.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Otevřít AI Soubor");
				fileChooser.getExtensionFilters().add(new ExtensionFilter("AI File", "*.robAI"));
				File file = fileChooser.showOpenDialog(designStage.getStage());
				if(file!=null){
					
					add(file.getAbsolutePath());
					update();
				}
			}
		});
		
		remove.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				int index= listView.getSelectionModel().getSelectedIndex();
				if(index!=-1){
					remove(index);
					update();
					
				}
			}
		});	
		
		close.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				designStage.close();
			}
		});
	}
	public void show(){
		designStage.show();
	}
	
	public Stage getStage(){
		return designStage.getStage();
	}
	private void add(String aiFile){
		String n= readNameFormAiFile(aiFile);
		if(n==null){
			return;
		}
		AIItem aiItem = new AIItem(n, aiFile);
		aiPlayers.add(aiItem);
		saveAiFile();
	}
	private void update(){
		aiPlayers.clear();
		listView.getItems().clear();
		loadAIFromFile();
		for(AIItem item:aiPlayers){
			listView.getItems().add(item.getName());
		}
	}
	
	private void remove(int index){
		aiPlayers.remove(index);
		saveAiFile();
		
		
	}
	private void loadAIFromFile(){
		
		BufferedReader input =null;
		try {
			input = new BufferedReader(new FileReader(new File(Lobby.AI_LIST_FILE)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return;
		}
		try {
			String line = null;
			while ((line = input.readLine()) != null) {
				String[] rr = line.split(";");
				aiPlayers.add(new AIItem(rr[0], rr[1]));
				
			}
		}catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	private String readNameFormAiFile(String file){
		BufferedReader input =null;
		try {
			input = new BufferedReader(new FileReader(new File(file)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String line = null;
			while ((line = input.readLine()) != null) {
				return line;
				
			}
		}catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	private void saveAiFile(){
		
		
		File file = new File(Lobby.AI_LIST_FILE);
		
		BufferedWriter  output;
		try {
			output = new BufferedWriter(new FileWriter(file));
			try {
				
				for(AIItem item: aiPlayers){
					output.write(item.getName()+";"+item.getFile());
					output.newLine();
				}
				
				
			} finally {
				output.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

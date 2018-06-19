package game_mechanics;

public class PrePlayer {
	
	public static final int PLAYER_TYPE_HUMAN = 0;
	public static final int PLAYER_TYPE_STATIC_AI = 1;
	public static final int PLAYER_TYPE_AI_FILE = 2;
	
	private String name;
	private int figurka;
	private int typeOfPlayer;
	private String aiFile;
	
	
	public PrePlayer(String name, int figurka) {
		super();
		this.name = name;
		this.figurka = figurka;
	}
	public int getFigurka() {
		return figurka;
	}
	public String getName() {
		return name;
	}
	public void setType(int type){
		this.typeOfPlayer = type;
	}
	public void setAIFile(String file){
		this.aiFile = file;
		typeOfPlayer = 2;
	}
	public String getAiFile() {
		return aiFile;
	}
	public int getTypeOfPlayer() {
		return typeOfPlayer;
	}

}

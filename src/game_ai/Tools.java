package game_ai;

import java.util.List;

import game_mechanics.Game;
import game_mechanics.GameSession;
import game_mechanics.Player;
import game_mechanics.Property;

public class Tools {
	
	public static float valueForPlayer(int player, Property pro, List<Property> allProperties){
		if(pro.getType()==Property.CARD){
			return 0;
		}
		int owned  = 0;
		int others = 0;
		int banks = 0;
		for(Property property: allProperties){
			if(property.getRealty().getGroup()==pro.getRealty().getGroup()){
				if(property.getOwner()==player){
					owned ++;
				}else if(property.getOwner()== -1){
					banks++;
				}else {
					others ++;
				}
			}
		}
		int total = owned + others + banks;
		int left = total - owned;
		if(left==0||left==1|| total==4 &&left==2){
			return 1;
		}
		
		if(total>2 && (left==2 || total==4 && left==3)){
			float base = 0.5f+banks*0.1f;
			return base;
		}
		if(total==banks){
			return 0.3f;
		}
		return 0;
	}
	
	
	
	public static float countGroupsOwned(List<Property> playersProperty){
		int count = 0;
		boolean [] groups = new boolean[11];
		for(Property pro: playersProperty){
			if(pro.getType()==Property.CARD){
				continue;
			}
			groups[pro.getRealty().getGroup()] = true;
		}
		for(boolean group: groups){
			if(group){
				count ++;
			}
		}
		return 1f/groups.length*count;
	}
	
	public static float groupLoaded(Player player, Property property){
		if(property.getType()==Property.CARD){
			return 0;
		}
		if(GameSession.ownAll(player, property)){
			int group = property.getRealty().getGroup();
			int h = 0;
			int t = 0;
			for(Property pro: player.getPropertiesPublic()){
				if(pro.getRealty()!=null&&pro.getRealty().getGroup()==group){
					h+=pro.getNumberOfHouse();
					t++;
				}
			}
			return 0.1f+ 0.9f/(t*5)*h;		
		}
		return 0f;
	}
	
	
	
	public static float priceValue(Property property){
		if(property.getType()==Property.CARD){
			return 0;
		}
		
		return priceValue(property.getRealty().getGroup());
	}
	
	public static float priceValue(int group){
		
		switch(group){
		case 10:
			return 1;
		case 9:
			return 0.8f;
		case 8:
			return 0.7f;
		case 7:
			return 0.6f;
		case 6:
			return 0.5f;
		case 5:
			return 0.4f;
		case 4:
			return 0.3f;
		case 3:
			return 0.2f;
		case 2:
			return 0.5f;
		case 1:
			return 0.1f;
			
		}
		return 1;
	}

}

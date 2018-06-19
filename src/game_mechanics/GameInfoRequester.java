package game_mechanics;

import java.util.ArrayList;
import java.util.List;

public class GameInfoRequester implements GameInfo {
	private List<GameInfo> infos = new ArrayList<>();

	public void addGameInfo(GameInfo gameInfo) {
		infos.add(gameInfo);
	}

	@Override
	public void onGameStarted(String info) {
		// TODO Auto-generated method stub
		for (GameInfo gameInfo : infos) {
			gameInfo.onGameStarted(info);
		}
	}

	@Override
	public void setGraphicsRespond(GraphicsRespond graphicsRespond) {
		// TODO Auto-generated method stub
		for (GameInfo gameInfo : infos) {
			gameInfo.setGraphicsRespond(graphicsRespond);
		}
	}

	@Override
	public void onStartMovingFigure(int playerId, int destination) {
		// TODO Auto-generated method stub
		for (GameInfo gameInfo : infos) {
			
			gameInfo.onStartMovingFigure(playerId, destination);
		}
	}

	@Override
	public void onDiceStoppedRolling(int val1, int val2, int doubleRows) {
		// TODO Auto-generated method stub
		for (GameInfo gameInfo : infos) {
			gameInfo.onDiceStoppedRolling(val1, val2, doubleRows);
		}
	}

	@Override
	public void onArriveOnPlat(Plat plat) {
		// TODO Auto-generated method stub
		for (GameInfo gameInfo : infos) {
			gameInfo.onArriveOnPlat(plat);
		}
	}

	@Override
	public void onPropertySold(Realty realty, int player) {
		// TODO Auto-generated method stub
		for (GameInfo gameInfo : infos) {
			gameInfo.onPropertySold(realty, player);
		}
	}

	@Override
	public void onPlayerPassStart(int player) {
		// TODO Auto-generated method stub
		for (GameInfo gameInfo : infos) {
			gameInfo.onPlayerPassStart(player);
		}
	}

	@Override
	public void onCardFlip(int type, String text) {
		// TODO Auto-generated method stub
		for (GameInfo gameInfo : infos) {
			gameInfo.onCardFlip(type, text);
		}
	}

	/*@Override
	public void onDealAccepted(Deal deal) {
		// TODO Auto-generated method stub
		for (GameInfo gameInfo : infos) {
			gameInfo.onDealAccepted(deal);
		}
	}

	@Override
	public void onDealRefused(Deal dael) {
		// TODO Auto-generated method stub
		for (GameInfo gameInfo : infos) {
			gameInfo.onDealAccepted(dael);
		}
	}*/

	@Override
	public void setGameReferee(GameReferee gameReferee) {
		// TODO Auto-generated method stub
		for (GameInfo gameInfo : infos) {
			gameInfo.setGameReferee(gameReferee);
		}
	}

	@Override
	public void onPlayerTurnStart(int player) {
		// TODO Auto-generated method stub
		for (GameInfo gameInfo : infos) {
			gameInfo.onPlayerTurnStart(player);
		}
	}
	
	@Override
	public void onEndOfGame(Stats stats, List<Player> players) {
		// TODO Auto-generated method stub
		for (GameInfo gameInfo : infos) {
			gameInfo.onEndOfGame(stats, players);
		}
	}

	@Override
	public boolean gameInfoAll() {
		// TODO Auto-generated method stub
		boolean f = false;
		for (GameInfo gameInfo : infos) {
			if(gameInfo.gameInfoAll()){
				f = true;
				break;
			}
		}
		return f;
	}

}

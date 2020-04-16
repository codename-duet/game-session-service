package com.valdemar.codenameduet.gamesessionservice.ports;

import com.valdemar.codenameduet.gamesessionservice.model.GameSession;
import com.valdemar.codenameduet.gamesessionservice.model.GameStatus;

import java.util.List;

/**
 * Primary Port representing the business core.
 */
public interface SessionManagerService {

    GameSession createNewGameSession(long playerId);

    List<String> getExistingSessionsForPlayer(long playerId);

    String getGameSessionData(String sessionId);

    GameSession getGameSessionDetails(String sessionId);

    GameSession joinGameSession(String sessionId, long playerId);

    List<GameSession> getGameSessionsDetails(Long playerId, GameStatus gameStatus);

    List<GameSession> getGameSessionsDetails(Long playerId);
}

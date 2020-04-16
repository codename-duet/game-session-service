package com.valdemar.codenameduet.gamesessionservice.ports;


import com.valdemar.codenameduet.gamesessionservice.model.GameSession;
import com.valdemar.codenameduet.gamesessionservice.model.GameStatus;
import java.util.List;
import java.util.Optional;

/**
 * Secondary or "driven" Port for obtaining information about players
 */
public interface SessionManagerRepository {

  GameSession createGameSession(final GameSession gameSession);

  void updateGameSession(final String gameSession);

  GameSession sessionDetails(final String gameSessionId);

  Optional<GameSession> findGameSession(final String gameSessionId);

  GameSession save(final GameSession gameSession);

  List<GameSession> findAllGameSessionForPlayer(Long playerId, GameStatus gameStatus);

  List<GameSession> findAllGameSessionForPlayer(Long playerId);
}

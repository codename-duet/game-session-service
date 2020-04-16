package com.valdemar.codenameduet.gamesessionservice.service;

import com.valdemar.codenameduet.gamesessionservice.exceptions.GameSessionFullException;
import com.valdemar.codenameduet.gamesessionservice.exceptions.GameSessionNotFoundException;
import com.valdemar.codenameduet.gamesessionservice.model.GameSession;
import com.valdemar.codenameduet.gamesessionservice.model.GameStatus;
import com.valdemar.codenameduet.gamesessionservice.model.SessionId;
import com.valdemar.codenameduet.gamesessionservice.ports.SessionManagerRepository;
import com.valdemar.codenameduet.gamesessionservice.ports.SessionManagerService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class SessionManagerServiceImpl implements SessionManagerService {

  private final SessionManagerRepository sessionManagerRepository;

  public SessionManagerServiceImpl(SessionManagerRepository sessionManagerRepository) {
    this.sessionManagerRepository = sessionManagerRepository;
  }

  @Override
  public GameSession createNewGameSession(long playerId) {
    log.info("Creating a new game session for playerId '{}'", playerId);
    SessionId sessionId = SessionId.ofRandom();
    GameSession gameSession = GameSession.newGameSession(sessionId, playerId);
    GameSession savedGameSession = sessionManagerRepository.save(gameSession);
    return savedGameSession;
  }

  @Override
  public List<String> getExistingSessionsForPlayer(long playerId) {
    log.info("Getting all game session Ids for player Id '{}'", playerId);

    return null;
  }

  @Override
  public String getGameSessionData(String sessionId) {
    log.info("Getting game session data for session id '{}'", sessionId);

    return getGameSessionDetails(sessionId).getGameSessionData();
  }

  @Override
  public GameSession getGameSessionDetails(String sessionId) {
    log.info("Getting game session details session id '{}'", sessionId);

    return sessionManagerRepository.findGameSession(sessionId)
        .orElseThrow(() -> new GameSessionNotFoundException(sessionId));
  }

  @Override
  public GameSession joinGameSession(String sessionId, long playerTwoId) {
    log.info("Joining game session details session id '{}'", sessionId);

    GameSession gameSession = sessionManagerRepository.findGameSession(sessionId)
        .orElseThrow(() -> new GameSessionNotFoundException(sessionId));

    if (gameSession.getPlayerTwoId() != null) {
      throw new GameSessionFullException(gameSession.getSessionId().getSessionId(),
          gameSession.getPlayerOneId(), gameSession.getPlayerTwoId());
    }

    gameSession.joinPlayerTwo(playerTwoId);

    return sessionManagerRepository.save(gameSession);
  }

  @Override
  public List<GameSession> getGameSessionsDetails(Long playerId, GameStatus gameStatus) {
    return sessionManagerRepository.findAllGameSessionForPlayer(playerId, gameStatus);
  }

  @Override
  public List<GameSession> getGameSessionsDetails(Long playerId) {
    return sessionManagerRepository.findAllGameSessionForPlayer(playerId);
  }


}

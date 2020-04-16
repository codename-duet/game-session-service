package com.valdemar.codenameduet.gamesessionservice.adapters.repository;

import com.valdemar.codenameduet.gamesessionservice.model.GameSession;
import com.valdemar.codenameduet.gamesessionservice.model.GameStatus;
import com.valdemar.codenameduet.gamesessionservice.ports.SessionManagerRepository;
import java.util.List;
import java.util.Optional;

public class SessionManagerRepoSpringJDBCDataAdapter implements SessionManagerRepository {

  private final GameSessionRepositorySpringJDBCData gameSessionRepository;

  public SessionManagerRepoSpringJDBCDataAdapter(
      GameSessionRepositorySpringJDBCData gameSessionRepository) {
    this.gameSessionRepository = gameSessionRepository;
  }

  @Override
  public GameSession createGameSession(GameSession gameSession) {
    return gameSessionRepository.save(gameSession);
  }

  @Override
  public void updateGameSession(String gameSession) {
    throw new UnsupportedOperationException("Pending Implementation");
  }

  @Override
  public GameSession sessionDetails(String gameSessionId) {
    return gameSessionRepository.findBySessionId(gameSessionId);
  }

  @Override
  public Optional<GameSession> findGameSession(String gameSessionId) {
    return Optional.ofNullable(gameSessionRepository.findBySessionId(gameSessionId));
  }

  @Override
  public GameSession save(GameSession gameSession) {
    return gameSessionRepository.save(gameSession);
  }

  @Override
  public List<GameSession> findAllGameSessionForPlayer(Long playerId, GameStatus gameStatus) {
    return gameSessionRepository
        .findByPlayerOneIdOrPlayerTwoIdAndStatusEquals(playerId, gameStatus.name());
  }

  @Override
  public List<GameSession> findAllGameSessionForPlayer(Long playerId) {
    return gameSessionRepository.findByPlayerOneIdOrPlayerTwoId(playerId);
  }
}

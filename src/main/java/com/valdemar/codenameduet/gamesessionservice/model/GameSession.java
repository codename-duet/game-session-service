package com.valdemar.codenameduet.gamesessionservice.model;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Embedded;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@PersistenceConstructor))
@Builder
public final class GameSession {

  @Id
  @With
  private final Long id;
  @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
  private final SessionId sessionId;
  private final Long playerOneId;
  private Long playerTwoId;
  private final GameStatus status;
  private final String gameSessionData;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  public GameSession(String sessionId, Long playerOneId, Long playerTwoId, GameStatus status,
      String gameSessionData, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = null;
    this.sessionId = SessionId.of(sessionId);
    this.playerOneId = playerOneId;
    this.playerTwoId = playerTwoId;
    this.status = status;
    this.gameSessionData = gameSessionData;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static GameSession newGameSession(SessionId sessionId, long hostPlayerId) {

    return new GameSession(
        sessionId.getSessionId(),
        hostPlayerId,
        null,
        GameStatus.WAITING_FOR_PLAYER_TWO,
        null,
        LocalDateTime.now(),
        LocalDateTime.now()

    );
  }

  public void joinPlayerTwo(final Long playerTwo) {
    this.playerTwoId = playerTwo;
    ;
  }
}

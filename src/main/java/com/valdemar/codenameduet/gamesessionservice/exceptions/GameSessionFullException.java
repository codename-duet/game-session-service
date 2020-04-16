package com.valdemar.codenameduet.gamesessionservice.exceptions;

import java.text.MessageFormat;

public class GameSessionFullException extends RuntimeException {

  public GameSessionFullException(String gameSessionId, Long playerOneId, Long playerTwoId) {
    super(MessageFormat
        .format("Game Session Id {0} is full with player one {1} and player two {2}", gameSessionId,
            playerOneId, playerTwoId));
  }
}

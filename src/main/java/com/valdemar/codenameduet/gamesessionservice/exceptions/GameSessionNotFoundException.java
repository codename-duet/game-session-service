package com.valdemar.codenameduet.gamesessionservice.exceptions;

public class GameSessionNotFoundException extends RuntimeException {

  public GameSessionNotFoundException(String gameSessionId) {
    super("Game Session Id not found" + gameSessionId);
  }
}

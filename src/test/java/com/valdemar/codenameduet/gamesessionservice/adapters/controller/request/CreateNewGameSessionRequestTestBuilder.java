package com.valdemar.codenameduet.gamesessionservice.adapters.controller.request;

public class CreateNewGameSessionRequestTestBuilder {

  public static final long PLAYER_1_ID = 10L;

  public static CreateNewGameSessionRequest.CreateNewGameSessionRequestBuilder aCreateNewGameSessionRequest() {

    return new CreateNewGameSessionRequest.CreateNewGameSessionRequestBuilder()
        .playerId(PLAYER_1_ID);
  }
}
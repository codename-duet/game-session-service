package com.valdemar.codenameduet.gamesessionservice.adapters.controller.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
//Hides the constructor to force usage of the Builder.
public final class GameSessionDetailsResponse {

  private final String sessionId;
  private final Long playerOneId;
  private final Long playerTwoId;
  private final String status;
  private final String gameData;
}

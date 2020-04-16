package com.valdemar.codenameduet.gamesessionservice.adapters.controller.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
//Hides the constructor to force usage of the Builder.
@JsonDeserialize(builder = CreateNewGameSessionRequest.CreateNewGameSessionRequestBuilder.class)
public final class CreateNewGameSessionRequest {

  private final long playerId;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CreateNewGameSessionRequestBuilder {
    // Lombok will add constructor, setters, build method
  }
}

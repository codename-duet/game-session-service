package com.valdemar.codenameduet.gamesessionservice.model;

import java.util.UUID;
import javax.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data(staticConstructor = "of")
public final class SessionId {

  private final String sessionId;

  public static SessionId ofRandom() {
    return SessionId.of(UUID.randomUUID().toString());
  }
}

package com.valdemar.codenameduet.gamesessionservice.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data(staticConstructor = "of")
public final class PlayerId {

  @Column
  private final Long playerId;
}

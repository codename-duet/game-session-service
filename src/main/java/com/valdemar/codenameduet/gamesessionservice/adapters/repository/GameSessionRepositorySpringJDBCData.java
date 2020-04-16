package com.valdemar.codenameduet.gamesessionservice.adapters.repository;

import com.valdemar.codenameduet.gamesessionservice.model.GameSession;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface GameSessionRepositorySpringJDBCData extends CrudRepository<GameSession, Long> {

  @Query("select * from Game_Session where session_id = :sessionId")
  GameSession findBySessionId(final String sessionId);

  @Query("select * from Game_Session where player_one_id = :playerId OR player_two_id = :playerId")
  List<GameSession> findByPlayerOneIdOrPlayerTwoId(final Long playerId);

  @Query("select * from Game_Session where (player_one_id = :playerId OR player_two_id = :playerId) AND status = :status")
  List<GameSession> findByPlayerOneIdOrPlayerTwoIdAndStatusEquals(final Long playerId,
      final String status);
}

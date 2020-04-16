package com.valdemar.codenameduet.gamesessionservice.adapters.repository;

import com.valdemar.codenameduet.gamesessionservice.config.DatabaseProperties;
import com.valdemar.codenameduet.gamesessionservice.config.RepositoryConfig;
import com.valdemar.codenameduet.gamesessionservice.model.GameSession;
import com.valdemar.codenameduet.gamesessionservice.model.GameSessionTestBuilders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.valdemar.codenameduet.gamesessionservice.model.GameSessionTestBuilders.PLAYER_FOUR_ID;
import static com.valdemar.codenameduet.gamesessionservice.model.GameSessionTestBuilders.PLAYER_ONE_ID;
import static com.valdemar.codenameduet.gamesessionservice.model.GameSessionTestBuilders.PLAYER_TWO_ID;
import static com.valdemar.codenameduet.gamesessionservice.model.GameSessionTestBuilders.SESSION_ID;
import static com.valdemar.codenameduet.gamesessionservice.model.GameSessionTestBuilders.SESSION_ID_1;
import static com.valdemar.codenameduet.gamesessionservice.model.GameSessionTestBuilders.SESSION_ID_2;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {DatabaseProperties.class, RepositoryConfig.class})
@AutoConfigureJdbc
@ActiveProfiles("h2")
//Transactional does not work because we are using A SpringBootTest annotation.
@Transactional
class SessionManagerRepositorySpringJDBCDataTest {

    @Autowired
    private GameSessionRepositorySpringJDBCData repository;

    @Test
    @DisplayName("Giving a new GameSession, save it and check the ID has been set")
    public void exerciseRepositoryForSimpleEntity() {
        GameSession gameSession = repository.save(GameSessionTestBuilders.aGameSessionNotPersisted().build());

        assertThat(gameSession.getId()).isNotNull();
        assertThat(gameSession).isEqualToIgnoringGivenFields(GameSessionTestBuilders.aGameSessionNotPersisted().build(), "id");
    }

    @Test
    @DisplayName("Giving an existing GameSession, and a valid session_id, retrieve the GameSession from the repository")
    public void findBySessionId() {
        GameSession gameSession = repository.save(GameSessionTestBuilders.aGameSessionNotPersisted().build());

        GameSession gameSessionFromRepo = repository.findBySessionId(SESSION_ID.getSessionId());

        //Todo: ignoring the data fields because the precision is being reduced. need to fix this.
        assertThat(gameSession).isEqualToIgnoringGivenFields(gameSessionFromRepo, "createdAt", "updatedAt");
    }


    @Test
    @DisplayName("Giving an existing GameSession, find all gameSession by players")
    public void findByGameSessionByPlayer() {

        repository.deleteAll();

        //2 games session with player is = 1;
        GameSession gameSession1 = repository.save(GameSessionTestBuilders.aGameSessionNotPersisted().build());
        GameSession gameSession2 = repository.save(GameSessionTestBuilders.aGameSessionNotPersisted().sessionId(SESSION_ID_1)
            .playerOneId(PLAYER_TWO_ID).playerTwoId(PLAYER_ONE_ID).build());

        //a game session with a different player
        repository.save(GameSessionTestBuilders.aGameSessionNotPersisted().sessionId(SESSION_ID_2)
            .playerOneId(PLAYER_TWO_ID).build());

        List<GameSession> gameSessionFromRepo = repository
            .findByPlayerOneIdOrPlayerTwoId(PLAYER_ONE_ID);

        //Todo: ignoring the data fields because the precision is being reduced. need to fix this.
        assertThat(gameSessionFromRepo).hasSize(2);

        assertThat(gameSessionFromRepo)
            .containsExactlyInAnyOrder(
                GameSessionTestBuilders
                    .aGameSessionNotPersisted()
                    .id(gameSession1.getId())
                    .build(),
                GameSessionTestBuilders.aGameSessionNotPersisted()
                    .id(gameSession2.getId())
                    .sessionId(SESSION_ID_1)
                    .playerOneId(PLAYER_TWO_ID)
                    .playerTwoId(PLAYER_ONE_ID).build());
    }

    @Test
    @DisplayName("Giving an existing GameSession and a playerId With no matches, find all gameSession by players")
    public void findByGameSessionByPlayerWithNoGameSession() {

        repository.deleteAll();

        //2 games session with player is = 1;
        repository.save(GameSessionTestBuilders.aGameSessionNotPersisted().build());
        repository.save(GameSessionTestBuilders.aGameSessionNotPersisted().sessionId(SESSION_ID_1)
            .playerOneId(PLAYER_TWO_ID).playerTwoId(PLAYER_ONE_ID).build());

        //a game session with a different player
        repository.save(GameSessionTestBuilders.aGameSessionNotPersisted().sessionId(SESSION_ID_2)
            .playerOneId(PLAYER_TWO_ID).build());


        List<GameSession> gameSessionFromRepo = repository
            .findByPlayerOneIdOrPlayerTwoId(PLAYER_FOUR_ID);

        //Todo: ignoring the data fields because the precision is being reduced. need to fix this.
        assertThat(gameSessionFromRepo).hasSize(0);
    }
}
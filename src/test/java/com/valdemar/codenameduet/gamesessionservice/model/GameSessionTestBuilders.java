package com.valdemar.codenameduet.gamesessionservice.model;

import java.time.LocalDateTime;


public class GameSessionTestBuilders{

    public static final Long ID = 1L;
    public static final SessionId SESSION_ID = SessionId.of("ABC");
    public static final SessionId SESSION_ID_1 = SessionId.of("XYZ");
    public static final SessionId SESSION_ID_2 = SessionId.of("XPTO");
    public static final Long PLAYER_ONE_ID = 10L;
    public static final Long PLAYER_TWO_ID = 20L;
    public static final Long PLAYER_THREE_ID = 30L;
    public static final Long PLAYER_FOUR_ID = 40L;
    public static final GameStatus GAME_STATUS =  GameStatus.IN_PROGRESS;
    public static final String GAME_SESSION_DATE = "";
    public static final LocalDateTime CREATED_AT = LocalDateTime.of(2020, 01, 01, 01,00, 00);
    public static final LocalDateTime UPDATED_AT = LocalDateTime.of(2020, 01, 01, 01,00, 00);


    public static GameSession.GameSessionBuilder aGameSessionWaitingForPlayer2(){

       return new GameSession.GameSessionBuilder()
               .id(ID)
               .playerOneId(PLAYER_ONE_ID)
               .sessionId(SESSION_ID)
               .status(GameStatus.IN_PROGRESS)
               .createdAt(CREATED_AT)
               .updatedAt(UPDATED_AT);
   }

    public static GameSession.GameSessionBuilder aGameSessionNotPersisted(){

        return new GameSession.GameSessionBuilder()
                .id(null)
                .playerOneId(PLAYER_ONE_ID)
                .sessionId(SESSION_ID)
                .status(GameStatus.WAITING_FOR_PLAYER_TWO)
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT);
    }

    public static GameSession.GameSessionBuilder aGameSessionWithBothPlayers(){

        return new GameSession.GameSessionBuilder()
                .id(ID)
                .playerOneId(PLAYER_ONE_ID)
                .playerTwoId(PLAYER_TWO_ID)
                .sessionId(SESSION_ID)
                .status(GameStatus.IN_PROGRESS)
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT);
    }

}
package com.valdemar.codenameduet.gamesessionservice.adapters.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.valdemar.codenameduet.gamesessionservice.adapters.controller.request.CreateNewGameSessionRequest;
import com.valdemar.codenameduet.gamesessionservice.exceptions.GameSessionNotFoundException;
import com.valdemar.codenameduet.gamesessionservice.model.GameSession;
import com.valdemar.codenameduet.gamesessionservice.model.GameStatus;
import com.valdemar.codenameduet.gamesessionservice.model.SessionId;
import com.valdemar.codenameduet.gamesessionservice.ports.SessionManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SessionManagerServiceController.class)
@ActiveProfiles("h2")
class SessionManagerServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionManagerService sessionManagerService;

    // This object will be magically initialized by the initFields method below.
    private JacksonTester<CreateNewGameSessionRequest> jsonCreateNewGameSessionRequest;

    @BeforeEach
    public void setup() {
        // Initializes the JacksonTester
        JacksonTester.initFields(this, new ObjectMapper());
    }

    private static final long PLAYER_ID_1 = 1L;
    private static final long PLAYER_ID_2 = 2L;
    private static final String SESSION_ID = "ABC";

    @Test
    void createGameSessionReturnsHttpStatusOk() throws Exception {
        when(sessionManagerService.createNewGameSession(eq(PLAYER_ID_1)))
                .thenReturn(aGameSession(PLAYER_ID_1));

        mockMvc.perform(
                post("/gamesession")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateNewGameSessionRequest.write(createNewGameSessionRequest(PLAYER_ID_1)).getJson())
        )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/gamesession/"));
    }

    @DisplayName("Getting an existing game session id, retuns the game session Details")
    @Test
    void getGameSessionBySessionIdReturnsHttpStatusOk() throws Exception {

        when(sessionManagerService.getGameSessionDetails(eq(SESSION_ID)))
                .thenReturn(aFullGameSessionInProgress());

        mockMvc.perform(
                get("/gamesession/{session_id}", SESSION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sessionId").value(SESSION_ID))
                .andExpect(jsonPath("$.playerOneId").value(PLAYER_ID_1))
                .andExpect(jsonPath("$.playerTwoId").value(PLAYER_ID_2))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

    }

    @DisplayName("Getting an NON existing game session id, returns a 404")
    @Test
    void getNonExistingGameSessionHttpStatusNotFound() throws Exception {

        when(sessionManagerService.getGameSessionDetails(eq(SESSION_ID))).thenThrow(new GameSessionNotFoundException(SESSION_ID));

        mockMvc.perform(
                get("/gamesession/{session_id}", SESSION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());

    }

    //TODO: Add other test cases

    private CreateNewGameSessionRequest createNewGameSessionRequest(Long player) {
        return CreateNewGameSessionRequest.builder()
                .playerId(player)
                .build();
    }

    private GameSession aGameSession( Long player) {
        return GameSession.newGameSession(SessionId.of(SESSION_ID), player);
    }

    private GameSession aFullGameSessionInProgress( ) {
        return new GameSession(SESSION_ID, PLAYER_ID_1, PLAYER_ID_2, GameStatus.IN_PROGRESS, null, LocalDateTime.now(), LocalDateTime.now());
    }
}
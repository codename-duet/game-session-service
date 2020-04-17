package com.valdemar.codenameduet.gamesessionservice;


import static com.valdemar.codenameduet.gamesessionservice.model.GameSessionTestBuilders.PLAYER_FOUR_ID;
import static com.valdemar.codenameduet.gamesessionservice.model.GameSessionTestBuilders.PLAYER_TWO_ID;
import static com.valdemar.codenameduet.gamesessionservice.model.GameSessionTestBuilders.SESSION_ID;
import static com.valdemar.codenameduet.gamesessionservice.model.GameSessionTestBuilders.SESSION_ID_1;
import static com.valdemar.codenameduet.gamesessionservice.model.GameSessionTestBuilders.SESSION_ID_2;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valdemar.codenameduet.gamesessionservice.adapters.controller.request.CreateNewGameSessionRequest;
import com.valdemar.codenameduet.gamesessionservice.adapters.controller.request.CreateNewGameSessionRequestTestBuilder;
import com.valdemar.codenameduet.gamesessionservice.model.GameSession;
import com.valdemar.codenameduet.gamesessionservice.model.GameSessionTestBuilders;
import com.valdemar.codenameduet.gamesessionservice.model.GameStatus;
import com.valdemar.codenameduet.gamesessionservice.ports.SessionManagerRepository;
import com.valdemar.codenameduet.gamesessionservice.ports.SessionManagerService;
import javax.servlet.RequestDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = SessionManagerServiceApplication.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@ActiveProfiles("h2")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class SessionManagerServiceApplicationTest {

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private SessionManagerRepository sessionManagerRepository;

  @Autowired
  private SessionManagerService sessionManagerService;

  // This object will be magically initialized by the initFields method below.
  private JacksonTester<CreateNewGameSessionRequest> jsonPlayer;

  private MockMvc mockMvc;

  @BeforeEach
  public void setUp(RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(documentationConfiguration(restDocumentation)).build();

    JacksonTester.initFields(this, new ObjectMapper());

    //Example on how to configure the schema, host and port
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
//                .apply(documentationConfiguration(this.restDocumentation).uris()
//                        .withScheme("https")
//                        .withHost("example.com")
//                        .withPort(443))
//                .build();
  }

  @Test
  @DisplayName("Create a new GameSession and returns the id of in the header")
  public void createNewGameSession() throws Exception {
    mockMvc.perform(
        post("/gamesession")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonPlayer.write(
                CreateNewGameSessionRequestTestBuilder.aCreateNewGameSessionRequest().build())
                .getJson())
    )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(redirectedUrlPattern("http://*/gamesession/*"))
        .andExpect(jsonPath("gameSessionId", is(notNullValue())))
        .andDo(
            document("register-gameSession",
                responseFields(
                    fieldWithPath("gameSessionId").description("The Game Session Unique Id"))));
  }

  @Test
  @DisplayName("Giving an id of an existing Game Session, returns the details of the Game Session")
  public void gameSessionDetails() throws Exception {

    String gameSessionId = createGameSession();

    mockMvc.perform(
        get("/gamesession/{session_id}", gameSessionId))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("sessionId", is(SESSION_ID.getSessionId())))
        .andExpect(jsonPath("playerOneId", is(10)))
        .andExpect(jsonPath("playerTwoId", equalTo(20)))
        .andExpect(jsonPath("status", is(GameStatus.IN_PROGRESS.name())))
        .andExpect(jsonPath("gameData", is(nullValue())))
        .andDo(document("list-gameSession-details",
            pathParameters(
                parameterWithName("session_id").description("The Id of the Game Session")),
            responseFields(
                fieldWithPath("sessionId").description("The Game Session Unique Id"),
                fieldWithPath("playerOneId").description("The associated Player 1 Id"),
                fieldWithPath("playerTwoId").description("The associated Player 2 Id"),
                fieldWithPath("status").description("The Game Session status"),
                fieldWithPath("gameData").description("The Game Session data")
            )));
  }

  @Test
  @DisplayName("Get all game session attached to a player ")
  public void filterGameSessionDetails() throws Exception {

    createGameSession(
        GameSessionTestBuilders.aGameSessionWithBothPlayers().id(null).playerOneId(PLAYER_FOUR_ID)
            .sessionId(SESSION_ID_1).build());
    createGameSession(
        GameSessionTestBuilders.aGameSessionWithBothPlayers().id(null).playerTwoId(PLAYER_FOUR_ID)
            .sessionId(SESSION_ID_2).build());

    mockMvc.perform(
        get("/gamesession").param("playerId", "40").param("filter", "OPEN"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("[0]sessionId", is(SESSION_ID_1.getSessionId())))
        .andExpect(jsonPath("[0]playerOneId", is(40)))
        .andExpect(jsonPath("[0]playerTwoId", equalTo(20)))
        .andExpect(jsonPath("[0]status", is(GameStatus.IN_PROGRESS.name())))
        .andExpect(jsonPath("[0]gameData", is(nullValue())))
        .andExpect(jsonPath("[1]sessionId", is(SESSION_ID_2.getSessionId())))
        .andExpect(jsonPath("[1]playerOneId", is(10)))
        .andExpect(jsonPath("[1]playerTwoId", equalTo(40)))
        .andExpect(jsonPath("[1]status", is(GameStatus.IN_PROGRESS.name())))
        .andExpect(jsonPath("[1]gameData", is(nullValue())))
        .andDo(document("player-gameSessions-details",
            responseFields(

                fieldWithPath("[]").description("The list of filters Game Sessions"),
                fieldWithPath("[].sessionId").description("The Game Session Unique Id"),
                fieldWithPath("[].playerOneId").description("The associated Player 1 Id"),
                fieldWithPath("[].playerTwoId").description("The associated Player 2 Id"),
                fieldWithPath("[].status").description("The Game Session status"),
                fieldWithPath("[].gameData").description("The Game Session data")
            )));
  }

  @Test
  public void errorExample() throws Exception {
    this.mockMvc
        .perform(get("/error")
            .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
            .requestAttr(RequestDispatcher.ERROR_REQUEST_URI,
                "/notes")
            .requestAttr(RequestDispatcher.ERROR_MESSAGE,
                "The tag 'http://localhost:8080/tags/123' does not exist"))
        .andDo(print()).andExpect(status().isBadRequest())
        .andExpect(jsonPath("error", is("Bad Request")))
        .andExpect(jsonPath("timestamp", is(notNullValue())))
        .andExpect(jsonPath("status", is(400)))
        .andExpect(jsonPath("path", is(notNullValue())))
        .andDo(document("error-example",
            responseFields(
                fieldWithPath("error")
                    .description("The HTTP error that occurred, e.g. `Bad Request`"),
                fieldWithPath("message").description("A description of the cause of the error"),
                fieldWithPath("path").description("The path to which the request was made"),
                fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
                fieldWithPath("timestamp")
                    .description("The time, in milliseconds, at which the error occurred"))));
  }

  private String createGameSession() {
    GameSession gameSession = sessionManagerRepository
        .save(GameSessionTestBuilders.aGameSessionNotPersisted().playerTwoId(PLAYER_TWO_ID).status(
            GameStatus.IN_PROGRESS).build());
    return createGameSession(gameSession);
  }

  private String createGameSession(GameSession gameSession) {
    GameSession gameSessionPersisted = sessionManagerRepository.save(gameSession);
    return gameSessionPersisted.getSessionId().getSessionId();
  }


}
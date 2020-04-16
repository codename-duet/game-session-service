package com.valdemar.codenameduet.gamesessionservice.adapters.controller;

import static com.valdemar.codenameduet.gamesessionservice.adapters.controller.request.FilterSession.ALL;

import com.valdemar.codenameduet.gamesessionservice.adapters.controller.request.CreateNewGameSessionRequest;
import com.valdemar.codenameduet.gamesessionservice.adapters.controller.request.FilterSession;
import com.valdemar.codenameduet.gamesessionservice.adapters.controller.request.JoinGameSessionRequest;
import com.valdemar.codenameduet.gamesessionservice.adapters.controller.response.CreateGameSessionResponse;
import com.valdemar.codenameduet.gamesessionservice.adapters.controller.response.GameSessionDetailsResponse;
import com.valdemar.codenameduet.gamesessionservice.exceptions.GameSessionNotFoundException;
import com.valdemar.codenameduet.gamesessionservice.model.GameSession;
import com.valdemar.codenameduet.gamesessionservice.model.GameStatus;
import com.valdemar.codenameduet.gamesessionservice.ports.SessionManagerService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/gamesession")
@Slf4j
public class SessionManagerServiceController {

  private SessionManagerService sessionManagerService;

  @Autowired
  public SessionManagerServiceController(final SessionManagerService sessionManagerService) {
    this.sessionManagerService = sessionManagerService;
  }

  @PostMapping(value = "")
  ResponseEntity<CreateGameSessionResponse> createGameSession(
      UriComponentsBuilder uriComponentsBuilder, @RequestBody CreateNewGameSessionRequest request) {

    log.info("Request for creating a new game session from Player Id: {}", request.getPlayerId());

    GameSession gameSession = sessionManagerService.createNewGameSession(request.getPlayerId());

    UriComponents uriComponents =
        uriComponentsBuilder.path("/gamesession/{id}").buildAndExpand(gameSession.getId());

    log.info("New Game Session '{}' created", gameSession.getSessionId());

    return ResponseEntity
        .created(uriComponents.toUri())
        .body(
            CreateGameSessionResponse
                .builder()
                .gameSessionId(gameSession.getSessionId().getSessionId()).build());
  }

  @PutMapping(value = "/{sessionId}", consumes = {"application/json"})
  ResponseEntity<GameSessionDetailsResponse> joinPlayer(@PathVariable String sessionId,
      @RequestBody JoinGameSessionRequest request) {

    log.info("Request for player id {} to join session id: {}", request.getPlayerId(), sessionId);

    try {
      GameSession gameSession = sessionManagerService
          .joinGameSession(sessionId, request.getPlayerId());
      return new ResponseEntity<>(makeGameSessionDetailsResponse(gameSession), HttpStatus.OK);
    } catch (GameSessionNotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/{sessionId}")
  ResponseEntity<GameSessionDetailsResponse> gameSessionDetails(@PathVariable String sessionId) {
    log.info("Request for game session details for sessionId: {}", sessionId);

    try {
      GameSession gameSession = sessionManagerService.getGameSessionDetails(sessionId);
      return new ResponseEntity<>(makeGameSessionDetailsResponse(gameSession), HttpStatus.OK);
    } catch (GameSessionNotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("")
  ResponseEntity<List<GameSessionDetailsResponse>> filterGameSessionsDetails(
      @RequestParam Long playerId, @RequestParam(defaultValue = "ALL") FilterSession filter) {
    //  log.info("Request for getting details of the existing player Id: {}", playerId);
    List<GameSession> gameSessions;
    if (filter == ALL) {
      gameSessions = sessionManagerService.getGameSessionsDetails(playerId);
    } else {
      GameStatus gameStatus = mapFilterToGameStatus(filter);
      gameSessions = sessionManagerService.getGameSessionsDetails(playerId, gameStatus);

    }

    return new ResponseEntity<>(
        gameSessions.stream().map(gameSession -> makeGameSessionDetailsResponse(gameSession))
            .collect(Collectors.toList()), HttpStatus.OK);
  }

  private GameStatus mapFilterToGameStatus(FilterSession filter) {
    switch (filter) {
      case OPEN:
        return GameStatus.IN_PROGRESS;
      case CLOSE:
        return GameStatus.ENDED;
      case WAITING_FOR_PLAYER:
        return GameStatus.WAITING_FOR_PLAYER_TWO;
      default:
        throw new IllegalArgumentException("Filter not recognised: " + filter);
    }
  }

  private GameSessionDetailsResponse makeGameSessionDetailsResponse(GameSession gameSession) {
    return GameSessionDetailsResponse.builder()
        .sessionId(gameSession.getSessionId().getSessionId())
        .playerOneId(gameSession.getPlayerOneId())
        .playerTwoId(gameSession.getPlayerTwoId())
        .status(gameSession.getStatus().name())
        .gameData(gameSession.getGameSessionData())
        .build();
  }

}

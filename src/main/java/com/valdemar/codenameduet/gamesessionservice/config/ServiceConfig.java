package com.valdemar.codenameduet.gamesessionservice.config;

import com.valdemar.codenameduet.gamesessionservice.ports.SessionManagerRepository;
import com.valdemar.codenameduet.gamesessionservice.service.SessionManagerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

  @Bean
  public SessionManagerServiceImpl sessionManagerServiceImpl(
      final SessionManagerRepository sessionManagerRepository) {
    return new SessionManagerServiceImpl(sessionManagerRepository);
  }
}

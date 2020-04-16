package com.valdemar.codenameduet.gamesessionservice;

import com.valdemar.codenameduet.gamesessionservice.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({AppConfig.class})
public class SessionManagerServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SessionManagerServiceApplication.class, args);
  }
}

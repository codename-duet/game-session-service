package com.valdemar.codenameduet.gamesessionservice.config;

import com.valdemar.codenameduet.gamesessionservice.adapters.repository.GameSessionRepositorySpringJDBCData;
import com.valdemar.codenameduet.gamesessionservice.adapters.repository.SessionManagerRepoSpringJDBCDataAdapter;
import com.valdemar.codenameduet.gamesessionservice.ports.SessionManagerRepository;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJdbcRepositories(basePackages = "com.valdemar.codenameduet.gamesessionservice.adapters.repository")
@EnableTransactionManagement
public class RepositoryConfig extends AbstractJdbcConfiguration {

  @Bean
  public DataSource dataSource(final DatabaseProperties databaseProperties) {
    return databaseProperties.dataSource();
  }

  @Bean
  public NamedParameterJdbcOperations namedParameterJdbcOperations(final DataSource dataSource) {
    return new NamedParameterJdbcTemplate(dataSource);
  }

  @Bean
  public SessionManagerRepository sessionManagerRepository(
      final GameSessionRepositorySpringJDBCData playerRepository) {
    return new SessionManagerRepoSpringJDBCDataAdapter(playerRepository);
  }

  @Bean
  PlatformTransactionManager transactionManager(final DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }
}

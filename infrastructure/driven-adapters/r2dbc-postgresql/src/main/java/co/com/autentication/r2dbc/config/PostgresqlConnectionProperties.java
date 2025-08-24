package co.com.autentication.r2dbc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for connecting to a PostgreSQL database using R2DBC.
 */
@ConfigurationProperties(prefix = "adapters.r2dbc")
public record PostgresqlConnectionProperties(String host, Integer port, String database,
                                             String schema, String username, String password) {

}

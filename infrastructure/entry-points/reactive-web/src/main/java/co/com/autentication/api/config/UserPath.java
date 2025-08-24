package co.com.autentication.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for user-related paths in the application.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "user.paths")
public class UserPath {

  private String user;
}

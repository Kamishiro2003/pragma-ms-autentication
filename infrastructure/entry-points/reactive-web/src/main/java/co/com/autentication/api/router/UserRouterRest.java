package co.com.autentication.api.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import co.com.autentication.api.config.UserPath;
import co.com.autentication.api.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class UserRouterRest {

  private final UserPath userPath;
  private final UserHandler userHandler;

  @Bean
  public RouterFunction<ServerResponse> routerFunction() {
    return route(POST(userPath.getUser()), userHandler::listenUserCreate);
  }
}

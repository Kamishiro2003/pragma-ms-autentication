package co.com.autentication.api.router;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import co.com.autentication.api.config.UserPath;
import co.com.autentication.api.error.ErrorResponse;
import co.com.autentication.api.error.GlobalErrorWebFilter;
import co.com.autentication.api.handler.UserHandler;
import co.com.autentication.api.model.request.UserCreateRequest;
import co.com.autentication.api.model.response.UserRestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Router configuration for user-related endpoints.
 */
@Configuration
@RequiredArgsConstructor
public class UserRouterRest {

  private final UserPath userPath;
  private final UserHandler userHandler;
  private final GlobalErrorWebFilter globalErrorWebFilter;

  /**
   * Defines the router function for user-related endpoints.
   *
   * @return the router function
   */
  @Bean
  @RouterOperation(method = POST,
      beanClass = UserHandler.class,
      beanMethod = "listenUserCreate",
      operation = @Operation(operationId = "createUser",
          summary = "Crea un nuevo usuario",
          description = "Recibe datos de usuario y devuelve el usuario creado",
          requestBody = @RequestBody(required = true,
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = UserCreateRequest.class)
              )
          ),
          responses = {@ApiResponse(responseCode = "201",
              description = "Usuario creado correctamente",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = UserRestResponse.class)
              )
          ), @ApiResponse(responseCode = "400",
              description = "Parámetros inválidos o faltantes",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          ), @ApiResponse(responseCode = "409",
              description = "Usuario con email o documento ya existe",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )}
      )
  )
  public RouterFunction<ServerResponse> routerFunction() {
    return route(POST(userPath.getUser()), userHandler::listenUserCreate).filter(
        globalErrorWebFilter);
  }
}

package co.com.autentication.api.router;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import co.com.autentication.api.error.ErrorResponse;
import co.com.autentication.api.error.GlobalErrorWebFilter;
import co.com.autentication.api.handler.AuthHandler;
import co.com.autentication.api.model.request.LoginRequest;
import co.com.autentication.api.model.response.TokenResponse;
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
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class AuthRouterRest {

  private static final String PATH = "/api/v1/login";

  private final AuthHandler authHandler;
  private final GlobalErrorWebFilter globalErrorWebFilter;

  @Bean
  @RouterOperation(method = POST,
      path = PATH,
      beanClass = AuthHandler.class,
      beanMethod = "listenLogin",
      operation = @Operation(operationId = "login",
          summary = "Inicio de sesión",
          description = "Recibe datos del usuario y devuelve el token",
          requestBody = @RequestBody(required = true,
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = LoginRequest.class)
              )
          ),
          responses = {@ApiResponse(responseCode = "200",
              description = "Inicio de sesión exitoso",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = TokenResponse.class)
              )
          ), @ApiResponse(responseCode = "400",
              description = "Parámetros inválidos o faltantes",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )}
      )
  )
  public RouterFunction<ServerResponse> authRouter() {
    return RouterFunctions.route()
        .POST(PATH, authHandler::listenLogin)
        .filter(globalErrorWebFilter)
        .build();
  }
}

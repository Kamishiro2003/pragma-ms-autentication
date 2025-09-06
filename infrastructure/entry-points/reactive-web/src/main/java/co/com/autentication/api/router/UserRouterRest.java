package co.com.autentication.api.router;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import co.com.autentication.api.error.ErrorResponse;
import co.com.autentication.api.error.GlobalErrorWebFilter;
import co.com.autentication.api.handler.UserHandler;
import co.com.autentication.api.model.request.UserCreateRequest;
import co.com.autentication.api.model.response.UserRestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
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

  private static final String PATH = "/api/v1/usuarios";
  private static final String EMAIL_PATH = PATH + "/email";

  private final UserHandler userHandler;
  private final GlobalErrorWebFilter globalErrorWebFilter;

  /**
   * Defines the router function for user-related endpoints.
   *
   * @return the router function
   */
  @Bean
  @RouterOperations({@RouterOperation(method = POST,
      path = PATH,
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
  ), @RouterOperation(method = GET,
      beanClass = UserHandler.class,
      path = PATH,
      beanMethod = "listenFindUserByDocumentId",
      operation = @Operation(operationId = "findUserByDocumentId",
          summary = "Busca un usuario por documentId",
          description = "Devuelve un usuario si existe con el documentId proporcionado",
          parameters = {@Parameter(name = "documentId",
              in = ParameterIn.QUERY,
              required = true,
              description = "El número de documento del usuario",
              schema = @Schema(type = "string")
          )},
          responses = {@ApiResponse(responseCode = "200",
              description = "Usuario encontrado correctamente",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = UserRestResponse.class)
              )
          ), @ApiResponse(responseCode = "404",
              description = "Usuario no encontrado",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )}
      )
  ), @RouterOperation(method = GET,
      beanClass = UserHandler.class,
      path = EMAIL_PATH,
      beanMethod = "listenFindUserByEmail",
      operation = @Operation(operationId = "findUserByEmail",
          summary = "Busca un usuario por correo electrónico",
          description = "Devuelve un usuario si existe con el correo electrónico proporcionado",
          parameters = {@Parameter(name = "email",
              in = ParameterIn.QUERY,
              required = true,
              description = "El correo electrónico del usuario",
              schema = @Schema(type = "string")
          )},
          responses = {@ApiResponse(responseCode = "200",
              description = "Usuario encontrado correctamente",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = UserRestResponse.class)
              )
          ), @ApiResponse(responseCode = "404",
              description = "Usuario no encontrado",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )}
      )
  )}
  )
  public RouterFunction<ServerResponse> routerFunction() {
    return route(POST(PATH), userHandler::listenUserCreate)
        .andRoute(GET(PATH), userHandler::listenFindUserByDocumentId)
        .andRoute(GET(EMAIL_PATH), userHandler::listenFindUserByEmail)
        .filter(globalErrorWebFilter);
  }
}

package co.com.autentication.api.handler;

import co.com.autentication.api.mapper.UserRestMapper;
import co.com.autentication.api.model.request.UserCreateRequest;
import co.com.autentication.model.user.UserCreate;
import co.com.autentication.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Handler for user-related operations in a reactive web context.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {

  private final UserUseCase useCase;
  private final UserRestMapper mapper;
  private final RequestValidator requestValidator;

  /**
   * Handles the creation of a new user.
   *
   * @param serverRequest the incoming server request containing user creation data
   * @return a {@link Mono} that emits the server response with the created user details
   */
  public Mono<ServerResponse> listenUserCreate(ServerRequest serverRequest) {
    log.info("Received request to create user at path={} method={}",
        serverRequest.path(),
        serverRequest.method());
    return serverRequest.bodyToMono(UserCreateRequest.class)
        .doOnNext(req -> log.debug("Payload received: {}", req))
        .flatMap(request -> requestValidator.validate(request)
            .then(Mono.defer(() -> {
              UserCreate userCreate = mapper.toUserCreate(request);
              return useCase.createUser(userCreate)
                  .map(mapper::toUserRestResponse)
                  .flatMap(response -> ServerResponse.status(HttpStatus.CREATED)
                      .contentType(MediaType.APPLICATION_JSON)
                      .bodyValue(response));
            })));
  }

  public Mono<ServerResponse> listenFindUserByDocumentId(ServerRequest serverRequest) {
    String documentId = serverRequest.queryParam("documentId")
        .orElseThrow(() -> new IllegalArgumentException("documentId is required"));
    log.info("Received request to find user with documentId={} at path={} method={}",
        documentId,
        serverRequest.path(),
        serverRequest.method());

    return useCase.getUserByDocumentId(documentId)
        .map(mapper::toUserRestResponse)
        .flatMap(response -> ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(response));
  }

  public Mono<ServerResponse> listenFindUserByEmail(ServerRequest serverRequest) {
    String email = serverRequest.queryParam("email")
        .orElseThrow(() -> new IllegalArgumentException("email is required"));
    log.info("Received request to find user with email={} at path={} method={}",
        email,
        serverRequest.path(),
        serverRequest.method());

    return useCase.getUserByEmail(email)
        .map(mapper::toUserRestResponse)
        .flatMap(response -> ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(response));
  }
}

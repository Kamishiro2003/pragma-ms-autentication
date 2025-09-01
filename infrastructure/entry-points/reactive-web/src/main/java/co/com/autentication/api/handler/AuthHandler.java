package co.com.autentication.api.handler;

import co.com.autentication.api.mapper.AuthRestMapper;
import co.com.autentication.api.model.request.LoginRequest;
import co.com.autentication.model.auth.Login;
import co.com.autentication.usecase.user.AuthUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandler {

  private final AuthUseCase useCase;
  private final AuthRestMapper mapper;
  private final RequestValidator validator;


  public Mono<ServerResponse> listenLogin(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(LoginRequest.class)
        .flatMap(request -> validator.validate(request)
            .then(Mono.defer(() -> {
              Login login = mapper.toLogin(request);
              return useCase.login(login)
                  .map(mapper::toTokenResponse)
                  .flatMap(response -> ServerResponse.ok()
                      .contentType(MediaType.APPLICATION_JSON)
                      .bodyValue(response));
            }))
        );
  }
}

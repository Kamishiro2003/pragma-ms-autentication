package co.com.autentication.api.handler;

import co.com.autentication.api.mapper.UserRestMapper;
import co.com.autentication.api.model.request.UserCreateRequest;
import co.com.autentication.model.user.UserCreate;
import co.com.autentication.usecase.user.UserCreateUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {

  private final UserCreateUseCase createUseCase;
  private final UserRestMapper mapper;
  private final Validator validator;

  public Mono<ServerResponse> listenUserCreate(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(UserCreateRequest.class).flatMap(request -> {
      Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
      if (!violations.isEmpty()) {
        List<String> errors = violations.stream().map(ConstraintViolation::getMessage).toList();
        return ServerResponse.badRequest()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(errors);
      }

      UserCreate userCreate = mapper.toUserCreate(request);

      return createUseCase.createUser(userCreate)
          .map(mapper::toUserRestResponse)
          .flatMap(response -> ServerResponse.ok()
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue(response));
    });
  }

}

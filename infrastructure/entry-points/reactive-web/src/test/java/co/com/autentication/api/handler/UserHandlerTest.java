package co.com.autentication.api.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import co.com.autentication.api.mapper.UserRestMapper;
import co.com.autentication.api.model.request.UserCreateRequest;
import co.com.autentication.api.model.response.UserRestResponse;
import co.com.autentication.model.user.User;
import co.com.autentication.model.user.UserCreate;
import co.com.autentication.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

  @Mock
  private UserUseCase createUseCase;
  @Mock
  private UserRestMapper mapper;
  @Mock
  private RequestValidator requestValidator;
  @Mock
  private ServerRequest serverRequest;

  @InjectMocks
  private UserHandler userHandler;

  @Test
  void listenUserCreate_success() {
    UserCreateRequest request = new UserCreateRequest();
    UserCreate userCreate = mock(UserCreate.class);
    UserRestResponse restResponse = new UserRestResponse();
    User user = new User();

    when(serverRequest.bodyToMono(UserCreateRequest.class)).thenReturn(Mono.just(request));
    doNothing().when(requestValidator).validate(request);
    when(mapper.toUserCreate(request)).thenReturn(userCreate);
    when(createUseCase.createUser(userCreate)).thenReturn(Mono.just(user));
    when(mapper.toUserRestResponse(user)).thenReturn(restResponse);

    Mono<ServerResponse> responseMono = userHandler.listenUserCreate(serverRequest);

    StepVerifier.create(responseMono).assertNext(response -> {
      assertTrue(response.statusCode().is2xxSuccessful());
      assertEquals(MediaType.APPLICATION_JSON, response.headers().getContentType());
    }).verifyComplete();

    verify(requestValidator).validate(request);
    verify(mapper).toUserCreate(request);
    verify(createUseCase).createUser(userCreate);
    verify(mapper).toUserRestResponse(user);
  }

  @Test
  void listenUserCreate_validationError() {
    UserCreateRequest request = new UserCreateRequest();

    when(serverRequest.bodyToMono(UserCreateRequest.class)).thenReturn(Mono.just(request));
    doThrow(new IllegalArgumentException("Invalid")).when(requestValidator).validate(request);

    Mono<ServerResponse> responseMono = userHandler.listenUserCreate(serverRequest);

    StepVerifier.create(responseMono)
        .expectErrorMatches(error -> error instanceof IllegalArgumentException && error.getMessage()
            .equals("Invalid"))
        .verify();

    verify(requestValidator).validate(request);
    verifyNoInteractions(mapper, createUseCase);
  }

  @Test
  void listenUserCreate_createUserError() {
    UserCreateRequest request = new UserCreateRequest();
    UserCreate userCreate = mock(UserCreate.class);

    when(serverRequest.bodyToMono(UserCreateRequest.class)).thenReturn(Mono.just(request));
    doNothing().when(requestValidator).validate(request);
    when(mapper.toUserCreate(request)).thenReturn(userCreate);
    when(createUseCase.createUser(userCreate)).thenReturn(Mono.error(new RuntimeException("DB error")));

    Mono<ServerResponse> responseMono = userHandler.listenUserCreate(serverRequest);

    StepVerifier.create(responseMono)
        .expectErrorMatches(error -> error instanceof RuntimeException && error.getMessage()
            .equals("DB error"))
        .verify();

    verify(requestValidator).validate(request);
    verify(mapper).toUserCreate(request);
    verify(createUseCase).createUser(userCreate);
  }
}


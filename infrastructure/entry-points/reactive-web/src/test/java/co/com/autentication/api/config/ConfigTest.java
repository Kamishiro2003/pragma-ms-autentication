package co.com.autentication.api.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import co.com.autentication.api.error.GlobalErrorWebFilter;
import co.com.autentication.api.handler.RequestValidator;
import co.com.autentication.api.handler.UserHandler;
import co.com.autentication.api.mapper.UserRestMapper;
import co.com.autentication.api.model.request.UserCreateRequest;
import co.com.autentication.api.model.response.UserRestResponse;
import co.com.autentication.api.router.UserRouterRest;
import co.com.autentication.model.user.User;
import co.com.autentication.model.user.UserCreate;
import co.com.autentication.usecase.user.UserCreateUseCase;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class,
    ConfigTest.TestConfig.class}
)
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

  private final User user = User.builder().id("1").name("User 1").documentId("33545454").build();
  private final UserCreate userCreate = new UserCreate("User 1",
      "user 1",
      "33545454",
      "33545454",
      BigDecimal.valueOf(54546),
      "4545454",
      "cra 54 # 54-54",
      LocalDate.now().minusYears(19));
  private final UserRestResponse userRestResponse = UserRestResponse.builder()
      .name("User 1")
      .documentId("33545454")
      .email("user 1")
      .address("cra 54 # 54-54")
      .build();
  @Autowired
  private WebTestClient webTestClient;
  @MockitoBean
  private UserCreateUseCase createUseCase;
  @MockitoBean
  private UserRestMapper mapper;
  @MockitoBean
  private RequestValidator requestValidator;
  @MockitoBean
  private GlobalErrorWebFilter globalErrorWebFilter;

  @BeforeEach
  void setUp() {
    doNothing().when(requestValidator).validate(any());
    when(mapper.toUserCreate(any(UserCreateRequest.class))).thenReturn(userCreate);
    when(mapper.toUserRestResponse(any(User.class))).thenReturn(userRestResponse);
    when(createUseCase.createUser(userCreate)).thenReturn(Mono.just(user));
  }

  @Test
  void corsConfigurationShouldAllowOrigins() {
    when(createUseCase.createUser(any(UserCreate.class))).thenReturn(Mono.just(user));
    when(mapper.toUserRestResponse(any(User.class))).thenReturn(userRestResponse);

    webTestClient.get().uri("/api/v1/usuarios").exchange().expectStatus().isNotFound();
  }

  @TestConfiguration
  static class TestConfig {

    @Bean
    UserPath userPath() {
      UserPath path = new UserPath();
      path.setUser("/api/v1/usuarios");
      return path;
    }
  }
}
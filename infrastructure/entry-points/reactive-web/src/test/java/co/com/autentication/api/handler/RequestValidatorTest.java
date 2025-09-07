package co.com.autentication.api.handler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.validation.Validator;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RequestValidatorTest {

  @Mock
  private Validator validator;

  private RequestValidator requestValidator;

  @BeforeEach
  void setUp() {
    requestValidator = new RequestValidator(validator);
  }

  @Test
  void validate_noViolations_shouldPass() {
    Object request = new Object();
    when(validator.validate(request)).thenReturn(Collections.emptySet());

    assertDoesNotThrow(() -> requestValidator.validate(request));

    verify(validator).validate(request);
  }

}
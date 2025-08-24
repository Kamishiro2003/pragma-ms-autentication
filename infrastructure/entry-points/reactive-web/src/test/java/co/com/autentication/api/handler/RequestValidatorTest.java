package co.com.autentication.api.handler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.autentication.model.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Collections;
import java.util.Set;
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

  @Test
  void validate_withViolations_shouldThrowValidationException() {
    Object request = new Object();
    ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
    when(violation.getMessage()).thenReturn("Invalid field");
    Set<ConstraintViolation<Object>> violations = Set.of(violation);
    when(validator.validate(request)).thenReturn(violations);

    ValidationException exception = assertThrows(ValidationException.class,
        () -> requestValidator.validate(request));

    assertEquals(1, exception.getDetails().size());
    assertEquals("Invalid field", exception.getDetails().get(0));

    verify(validator).validate(request);
  }
}
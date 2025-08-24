package co.com.autentication.api.handler;

import co.com.autentication.model.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Utility class for validating request objects using Jakarta Bean Validation.
 */
@Component
@RequiredArgsConstructor
public class RequestValidator {

  private final Validator validator;

  /**
   * Validates the given request object and throws a {@link ValidationException} if any constraint
   * violations are found.
   *
   * @param <T>     the type of the request object
   * @param request the request object to validate
   * @throws ValidationException if any constraint violations are found
   */
  public <T> void validate(T request) {
    Set<ConstraintViolation<T>> violations = validator.validate(request);

    if (!violations.isEmpty()) {
      List<String> details = violations.stream().map(ConstraintViolation::getMessage).toList();

      throw new ValidationException(details);
    }
  }
}
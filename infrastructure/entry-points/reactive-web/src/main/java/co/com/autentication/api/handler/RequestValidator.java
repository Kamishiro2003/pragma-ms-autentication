package co.com.autentication.api.handler;

import co.com.autentication.model.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Utility class for validating request objects using Jakarta Bean Validation.
 */
@Component
@RequiredArgsConstructor
public class RequestValidator {

  private final Validator validator;

  public <T> Mono<Void> validate(T request) {
    Set<ConstraintViolation<T>> violations = validator.validate(request);

    if (violations.isEmpty()) {
      return Mono.empty();
    }

    List<String> details = violations.stream()
        .map(ConstraintViolation::getMessage)
        .toList();
    return Mono.error(new ValidationException(details));
  }
}
package co.com.autentication.model.error;

import lombok.Getter;

/**
 * Enum representing various error codes used in the application.
 */
@Getter
public enum ErrorCode {
  USER_CANNOT_BE_UNDER_AGE("USER-CANNOT-BE-UNDER-AGE", ExceptionCode.INVALID_INPUT,
      "El usuario no puede ser menor de edad."),
  ;

  private final String code;
  private final ExceptionCode exceptionCode;
  private final String message;

  /**
   * Constructor for ErrorCode enum.
   *
   * @param code          the unique error code
   * @param exceptionCode the associated exception code
   * @param message       the error message
   */
  ErrorCode(String code, ExceptionCode exceptionCode, String message) {
    this.code = code;
    this.exceptionCode = exceptionCode;
    this.message = message;
  }
}

package co.com.autentication.model.exception;

import co.com.autentication.model.error.ErrorCode;
import co.com.autentication.model.error.ExceptionCode;
import lombok.Getter;

/**
 * Generic exception for the application. This exception is used to handle application-specific
 * errors.
 */
@Getter
public class ApplicationException extends RuntimeException {

  private final ExceptionCode exceptionCode;
  private final String fullErrorCode;

  /**
   * Constructor for ApplicationException.
   *
   * @param errorCode the full error code associated with the exception
   */
  public ApplicationException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.exceptionCode = errorCode.getExceptionCode();
    this.fullErrorCode = errorCode.getCode();
  }
}

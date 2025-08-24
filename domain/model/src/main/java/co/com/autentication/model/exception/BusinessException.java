package co.com.autentication.model.exception;

import co.com.autentication.model.error.ErrorCode;

/**
 * Custom exception class for handling business logic errors.
 */
public class BusinessException extends ApplicationException {

  /**
   * Constructs a new BusinessException with the specified error code.
   *
   * @param errorCode the error code associated with this exception
   */
  public BusinessException(ErrorCode errorCode) {
    super(errorCode);
  }
}

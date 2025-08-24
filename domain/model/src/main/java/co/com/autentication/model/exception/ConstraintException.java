package co.com.autentication.model.exception;

import co.com.autentication.model.error.ErrorCode;

/**
 * Custom exception class for handling constraint violation errors.
 */
public class ConstraintException extends ApplicationException {

  /**
   * Constructs a new ConstraintException with the specified error code.
   *
   * @param errorCode the error code associated with this exception
   */
  public ConstraintException(ErrorCode errorCode) {
    super(errorCode);
  }
}

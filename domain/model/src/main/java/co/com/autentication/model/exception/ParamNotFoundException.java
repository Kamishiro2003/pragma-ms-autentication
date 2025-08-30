package co.com.autentication.model.exception;

import co.com.autentication.model.error.ErrorCode;

public class ParamNotFoundException extends ApplicationException {

  public ParamNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}

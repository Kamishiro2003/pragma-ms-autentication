package co.com.autentication.api.error;

import static org.junit.jupiter.api.Assertions.assertEquals;

import co.com.autentication.model.error.ExceptionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

class ExceptionCodeMapTest {

  private ExceptionCodeMap exceptionCodeMap;

  @BeforeEach
  void setUp() {
    exceptionCodeMap = new ExceptionCodeMap();
  }

  @ParameterizedTest
  @CsvSource({"INVALID_INPUT, BAD_REQUEST", "CONSTRAINT_VIOLATION, CONFLICT",
      "UNEXPECTED_ERROR, INTERNAL_SERVER_ERROR"}
  )
  void ShouldReturnHttpStatus(String exceptionCodeString, String expectedHttpStatusString) {
    // Arrange
    ExceptionCode exceptionCode = ExceptionCode.valueOf(exceptionCodeString);
    HttpStatus expectedHttpStatus = HttpStatus.valueOf(expectedHttpStatusString);
    // Act
    HttpStatus actualHttpStatus = exceptionCodeMap.getHttpStatusFromExceptionCode(exceptionCode);
    // Assert
    assertEquals(expectedHttpStatus, actualHttpStatus);
  }
}
package util;

import co.com.autentication.model.user.User;
import co.com.autentication.model.user.UserCreate;
import java.math.BigDecimal;
import java.time.LocalDate;

public class UserTestUtil {

  private static final String ID = "USER_ID";
  private static final String NAME = "John";
  private static final String LAST_NAME = "Doe";
  private static final String EMAIL = "jonh.doe@example.com";
  private static final String DOCUMENT_ID = "1234567890";
  private static final String PHONE = "555-1234";
  private static final String ADDRESS = "123 Main St, Anytown, USA";
  private static final BigDecimal BASE_SALARY = BigDecimal.valueOf(50000);

  public static User getUser() {
    return User.builder()
        .id(ID)
        .name(NAME)
        .lastName(LAST_NAME)
        .email(EMAIL)
        .documentId(DOCUMENT_ID)
        .baseSalary(BASE_SALARY)
        .phone(PHONE)
        .address(ADDRESS)
        .birthDate(LocalDate.parse("2000-01-01"))
        .build();
  }

  public static UserCreate getUserCreate() {
    return new UserCreate(
        NAME,
        LAST_NAME,
        EMAIL,
        DOCUMENT_ID,
        BASE_SALARY,
        PHONE,
        ADDRESS,
        LocalDate.parse("2000-01-01")
    );
  }

  public static UserCreate getUnderAgeUserCreate() {
    return new UserCreate(
        NAME,
        LAST_NAME,
        EMAIL,
        DOCUMENT_ID,
        BASE_SALARY,
        PHONE,
        ADDRESS,
        LocalDate.now()
            .minusYears(17)
    );
  }
}

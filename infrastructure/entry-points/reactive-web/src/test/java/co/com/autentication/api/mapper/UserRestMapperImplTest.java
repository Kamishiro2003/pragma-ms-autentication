package co.com.autentication.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import co.com.autentication.api.model.request.UserCreateRequest;
import co.com.autentication.api.model.response.UserRestResponse;
import co.com.autentication.model.user.User;
import co.com.autentication.model.user.UserCreate;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserRestMapperImplTest {

  private UserRestMapperImpl mapper;

  @BeforeEach
  void setUp() {
    mapper = new UserRestMapperImpl();
  }

  @Test
  void toUserCreate_shouldMapFieldsCorrectly() {
    UserCreateRequest request = new UserCreateRequest();
    request.setName("John");
    request.setLastName("Doe");
    request.setEmail("john.doe@email.com");
    request.setDocumentId("12345");
    request.setBaseSalary(BigDecimal.valueOf(5000));
    request.setPhone("555-1234");
    request.setAddress("123 Main St");
    request.setBirthDate(LocalDate.of(1990, 1, 1));

    UserCreate result = mapper.toUserCreate(request);

    assertNotNull(result);
    assertEquals("John", result.name());
    assertEquals("Doe", result.lastName());
    assertEquals("john.doe@email.com", result.email());
    assertEquals("12345", result.documentId());
    assertEquals(BigDecimal.valueOf(5000), result.baseSalary());
    assertEquals("555-1234", result.phone());
    assertEquals("123 Main St", result.address());
    assertEquals(LocalDate.of(1990, 1, 1), result.birthDate());
  }

  @Test
  void toUserCreate_nullRequest_shouldReturnNull() {
    assertNull(mapper.toUserCreate(null));
  }

  @Test
  void toUserRestResponse_shouldMapFieldsCorrectly() {
    User user = new User();
    user.setName("Jane");
    user.setLastName("Smith");
    user.setEmail("jane.smith@email.com");
    user.setDocumentId("67890");
    user.setBaseSalary(BigDecimal.valueOf(6000));
    user.setPhone("555-6789");
    user.setAddress("456 Elm St");
    user.setBirthDate(LocalDate.of(1985, 5, 20));

    UserRestResponse response = mapper.toUserRestResponse(user);

    assertNotNull(response);
    assertEquals("Jane", response.getName());
    assertEquals("Smith", response.getLastName());
    assertEquals("jane.smith@email.com", response.getEmail());
    assertEquals("67890", response.getDocumentId());
    assertEquals(BigDecimal.valueOf(6000), response.getBaseSalary());
    assertEquals("555-6789", response.getPhone());
    assertEquals("456 Elm St", response.getAddress());
    assertEquals(LocalDate.of(1985, 5, 20), response.getBirthDate());
  }

  @Test
  void toUserRestResponse_nullUser_shouldReturnNull() {
    assertNull(mapper.toUserRestResponse(null));
  }
}
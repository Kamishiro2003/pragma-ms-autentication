package co.com.autentication.api.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the response model for user information in the REST API.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserRestResponse {

  private String name;
  private String lastName;
  private String email;
  private String documentId;
  private BigDecimal baseSalary;
  private String phone;
  private String address;
  private LocalDate birthDate;
}

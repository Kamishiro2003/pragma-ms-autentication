package co.com.autentication.model.user;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user in the system.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

  private String id;
  private String name;
  private String lastName;
  private String email;
  private String documentId;
  private BigDecimal baseSalary;
  private String phone;
  private String address;
  private LocalDate birthDate;
}

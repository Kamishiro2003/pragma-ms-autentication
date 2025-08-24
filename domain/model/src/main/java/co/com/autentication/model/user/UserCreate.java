package co.com.autentication.model.user;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents the data required to create a new user in the system.
 */
public record UserCreate(String name, String lastName, String email, String documentId,
                         BigDecimal baseSalary, String phone, String address, LocalDate birthDate) {

}

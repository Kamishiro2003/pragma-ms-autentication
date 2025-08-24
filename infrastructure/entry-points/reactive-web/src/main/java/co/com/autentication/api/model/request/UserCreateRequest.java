package co.com.autentication.api.model.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request model for creating a new user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserCreateRequest {

  @NotBlank(message = "El nombre es obligatorio")
  @Size(max = 80, message = "El nombre debe tener menos de 80 caracteres")
  private String name;

  @NotBlank(message = "El apellido es obligatorio")
  @Size(max = 80, message = "El apellido debe tener menos de 80 caracteres")
  private String lastName;

  @NotBlank(message = "El correo electrónico es obligatorio")
  @Size(max = 80, message = "El correo electrónico debe tener menos de 80 caracteres")
  @Email(message = "El correo electrónico debe tener un formato válido")
  private String email;

  @NotBlank(message = "El documento de identidad es obligatorio")
  @Size(max = 30, message = "El documento de identidad debe tener menos de 30 caracteres")
  private String documentId;

  @NotNull(message = "El salario base es obligatorio")
  @Digits(integer = 8,
      fraction = 2,
      message = "El salario base debe tener como máximo 8 dígitos enteros y 2 decimales"
  )
  @DecimalMin(value = "0.0",
      inclusive = true,
      message = "El salario base debe ser mayor o igual a 0"
  )
  @DecimalMax(value = "15000000.0",
      inclusive = true,
      message = "El salario base no puede superar 15,000,000"
  )
  private BigDecimal baseSalary;

  @Size(max = 20, message = "El teléfono debe tener menos de 20 caracteres")
  private String phone;

  @Size(max = 255, message = "La dirección debe tener menos de 255 caracteres")
  private String address;

  @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
  private LocalDate birthDate;
}

package co.com.autentication.api.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoginRequest {

  @NotBlank(message = "El correo electrónico es obligatorio")
  @Size(max = 80, message = "El correo electrónico debe tener menos de 80 caracteres")
  @Email(message = "El correo electrónico debe tener un formato válido")
  private String email;

  @NotBlank(message = "La contraseña es obligatoria")
  @Size(max = 100, message = "La contraseña debe tener menos de 100 caracteres")
  private String password;
}

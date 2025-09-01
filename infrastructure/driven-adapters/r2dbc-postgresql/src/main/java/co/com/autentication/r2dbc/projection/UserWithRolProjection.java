package co.com.autentication.r2dbc.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserWithRolProjection {

  @Column("correo_electronico")
  private String email;

  @Column("contrasena")
  private String password;

  @Column("rol")
  private String roleName;
}

package co.com.autentication.r2dbc.entity;


import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Represents a user in the persistence layer.
 */
@Getter
@Setter
@Table("Usuario")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserEntity {

  @Id
  @Column("id_usuario")
  private String id;

  @Column("nombre")
  private String name;

  @Column("apellido")
  private String lastName;

  @Column("correo_electronico")
  private String email;

  @Column("contrasena")
  private String password;

  @Column("id_role")
  private String roleId;

  @Column("documento_identidad")
  private String documentId;

  @Column("salario_base")
  private BigDecimal baseSalary;

  @Column("telefono")
  private String phone;

  @Column("direccion")
  private String address;

  @Column("fecha_nacimiento")
  private LocalDate birthDate;
}

package co.com.autentication.model.role;

import lombok.Getter;

/**
 * Represents the available roles in the system.
 */
@Getter
public enum RoleEnum {
  ADMIN("administrador"),
  ADVISER("asesor"),
  CLIENT("cliente"),
  ;

  private final String name;

  RoleEnum(String name) {
    this.name = name;
  }
}

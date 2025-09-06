package co.com.autentication.r2dbc.mapper;

import co.com.autentication.model.user.UserWithRole;
import co.com.autentication.r2dbc.projection.UserWithRolProjection;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

  public UserWithRole toUserWithRole(UserWithRolProjection projection) {
    return UserWithRole.builder()
        .email(projection.getEmail())
        .password(projection.getPassword())
        .roleName(projection.getRoleName())
        .build();
  }
}

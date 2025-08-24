package co.com.autentication.api.mapper;

import co.com.autentication.api.model.request.UserCreateRequest;
import co.com.autentication.api.model.response.UserRestResponse;
import co.com.autentication.model.user.User;
import co.com.autentication.model.user.UserCreate;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between User-related DTOs and domain models.
 */
@Mapper(componentModel = "spring")
public interface UserRestMapper {

  /**
   * Converts a UserCreateRequest DTO to a UserCreate domain model.
   *
   * @param request the UserCreateRequest DTO
   * @return the corresponding UserCreate domain model
   */
  UserCreate toUserCreate(UserCreateRequest request);

  /**
   * Converts a User domain model to a UserRestResponse DTO.
   *
   * @param user the User domain model
   * @return the corresponding UserRestResponse DTO
   */
  UserRestResponse toUserRestResponse(User user);
}

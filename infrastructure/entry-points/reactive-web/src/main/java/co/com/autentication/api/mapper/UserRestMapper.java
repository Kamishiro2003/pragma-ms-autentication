package co.com.autentication.api.mapper;

import co.com.autentication.api.model.request.UserCreateRequest;
import co.com.autentication.api.model.response.UserRestResponse;
import co.com.autentication.model.user.User;
import co.com.autentication.model.user.UserCreate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRestMapper {

  UserCreate toUserCreate(UserCreateRequest request);

  UserRestResponse toUserRestResponse(User user);
}

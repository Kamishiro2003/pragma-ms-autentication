package co.com.autentication.api.mapper;

import co.com.autentication.api.model.request.LoginRequest;
import co.com.autentication.api.model.response.TokenResponse;
import co.com.autentication.model.auth.Login;
import co.com.autentication.model.auth.Token;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface AuthRestMapper {

  Login toLogin(LoginRequest request);

  TokenResponse toTokenResponse(Token token);
}

package community.project.community.client.service;

import community.project.community.client.model.UserInput;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

  //boolean register(UserInput parameter); //회원가입
  boolean emailAuth(String uuid); //이메일 인증


}

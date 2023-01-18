package community.project.community.client.service;

import community.project.community.client.model.UserInput;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

  //회원가입
  boolean register(UserInput parameter);

  boolean emailAuth(String uuid); //이메일 인증

  //패스워드 암호화
  String getEncryptPassword(String password);

  //동일한 이메일 체크
  boolean checkSameEmail(String email);

  //인증 이메일 전송
  void sendAuthEmail(String email,String uuid);

  //패스워드 확인
  boolean loginCheckPassword(String userInputPassword, String password);




}

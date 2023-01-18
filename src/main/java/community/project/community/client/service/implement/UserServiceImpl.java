package community.project.community.client.service.implement;

import community.project.community.client.entity.User;
import community.project.community.client.enums.UserRole;
import community.project.community.client.enums.UserStatus;
import community.project.community.client.exception.AlreadyRegistEmail;
import community.project.community.client.model.UserInput;
import community.project.community.client.repository.UserRepository;
import community.project.community.client.service.UserService;
import community.project.community.components.MailComponents;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final MailComponents mailComponents;

  private final PasswordEncoder passwordEncoder;

  //사용자 비밀번호 암호화
  public String getEncryptPassword(String password) {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    return bCryptPasswordEncoder.encode(password);
  }

  //회원가입
  @Override
  public boolean register(UserInput userInput) {
    String encryptPassword = getEncryptPassword(userInput.getPassword());
    String uuid = UUID.randomUUID().toString();

    User user = User.builder()
        .userId(userInput.getUserId())
        .userName(userInput.getUserName())
        .userNickName(userInput.getNickName())
        .phone(userInput.getPhone())
        .password(encryptPassword)
        .registerDate(LocalDateTime.now())
        .adminYn(false)
        .emailAuthYn(false)
        .emailAuthKey(uuid)
        .userStatus(UserStatus.REQ.toString())
        .userRole(UserRole.USER.toString())
        .build();
    userRepository.save(user);

    sendAuthEmail(userInput.getUserId(),uuid);

    return true;
  }

  //동일한 이메일 체크
  @Override
  public boolean checkSameEmail(String userId) {
    if (!userRepository.existsByUserId(userId)) {
      throw new AlreadyRegistEmail("이미가입된 이메일 계정입니다.");
    }
    return true;
  }

  @Override
  public void sendAuthEmail(String email, String uuid) {
    String subject = "00커뮤니티 사이트 가입을 축하드립니다.";
    String text = "<p> 00커뮤니티 사이트 가입을 축하드립니다. <p><p>아래 링크를 통해 인증하여 가입을 완료 하세요. </p>"
        + "<div><a target='_blank' href='http://localhost:8080/member/email-auth?id=" + uuid
        + "'>가입 완료 </a></div>";
    mailComponents.sendMail(email, subject, text);
  }

  //로그인
  @Override
  public boolean loginCheckPassword(String userInputPassword, String password) {
    if (!passwordEncoder.matches(userInputPassword, password)) {
      return false;
    }
    return true;
  }


  /*@Override
  public boolean register(UserInput parameter) {
    Optional<User> optionalUser = userRepository.findById(parameter.getUserId());

    if (optionalUser.isPresent()) {
      //현재 userId에 해당하는 데이터가 존재한다면, 아이디가 중복이라면
      return false;
    }
    String encPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());  //비밀번호 해쉬 암호화

    String uuid = UUID.randomUUID().toString();

    User user = User.builder()
        .userId(parameter.getUserId())
        .userName(parameter.getUserName())
        .phone(parameter.getPhone())
        .password(encPassword)
        .regDt(LocalDateTime.now())
        .emailAuthYn(false)
        .emailAuthKey(uuid)
        .userStatus(User.MEMBER_STATUS_REQ)
        .build();
    userRepository.save(user);

    //확인 이메일 전송
    String email = parameter.getUserId();
    String subject = "커뮤니티 사이트 가입을 축하드립니다.";
    String text = "<p>커뮤니티사이트 가입을 축하드립니다. <p><p>아래 링크를 통해 인증하여 가입을 완료 하세요. </p>"
        + "<div><a target='_blank' href='http://localhost:8080/member/email-auth?id=" + uuid
        + "'>가입 완료 </a></div>";
    mailComponents.sendMail(email, subject, text);

    return true;
  }*/


  @Override
  public boolean emailAuth(String uuid) {
    Optional<User> optionalUser = userRepository.findByEmailAuthKey(uuid);

    if (!optionalUser.isPresent()) {
      return false;
    }
    User user = optionalUser.get();

    if (user.isEmailAuthYn()) {
      return false; //이미 해당 계정이 활성화 되었으므로 재 활성화시 false
    }

    //member.setUserStatus(Member.MEMBER_STATUS_ING);
    user.setEmailAuthYn(true);
    user.setEmailAuthDt(LocalDateTime.now());
    user.setUserStatus(UserStatus.ING.toString());
    userRepository.save(user);

    return true;
  }
}

package community.project.community.client.service.implement;

import community.project.community.client.entity.User;
import community.project.community.client.exception.UserNotEmailAuthException;
import community.project.community.client.model.UserInput;
import community.project.community.client.repository.UserRepository;
import community.project.community.client.service.UserService;
import community.project.community.components.MailComponents;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final MailComponents mailComponents;

  @Override
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
  }

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
    user.setUserStatus(User.MEMBER_STATUS_ING);
    userRepository.save(user);

    return true;
  }

  //로그인 확인 ->DetailService
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> optionalUser = userRepository.findById(username);

    if (!optionalUser.isPresent()) {
      throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
    }
    User user = optionalUser.get();

    if (User.MEMBER_STATUS_REQ.equals(user.getUserStatus())) {
      throw new UserNotEmailAuthException("이메일 활성화 이후에 로그인을 해주세요.");
    }
    if (User.MEMBER_STATUS_STOP.equals(user.getUserStatus())) {
      throw new UserNotEmailAuthException("정지된 회원 입니다.");
    }

    userRepository.save(user);

    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));  //디폴트 롤

    if (user.isAdminYn()) {
      grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));  //관리자 롤
    }

    return new org.springframework.security.core.userdetails.User(user.getUserId(),
        user.getPassword(), grantedAuthorities);

  }
}

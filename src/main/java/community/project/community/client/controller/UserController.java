package community.project.community.client.controller;

import community.project.community.client.entity.User;
import community.project.community.client.enums.UserRole;
import community.project.community.client.enums.UserStatus;
import community.project.community.client.exception.AlreadyRegistEmail;
import community.project.community.client.exception.EmailExistException;
import community.project.community.client.exception.UserLoginNotCorrectPasswordException;
import community.project.community.client.exception.UserNotEmailAuthException;
import community.project.community.client.exception.UserNotFoundException;
import community.project.community.client.model.ResponseError;
import community.project.community.client.model.UserInput;
import community.project.community.client.model.UserInputLogin;
import community.project.community.client.model.UserInputPassword;
import community.project.community.client.repository.UserRepository;
import community.project.community.client.service.UserService;
import community.project.community.components.MailComponents;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@AllArgsConstructor
@Slf4j
public class UserController {

  private final UserRepository userRepository;
  PasswordEncoder passwordEncoder;
  private final MailComponents mailComponents;
  private final UserService userService;

  /*여기부터
  프론트 앤드는 구현 x */

  //사용자 비밀번호 암호화
  private String getEncryptPassword(String password) {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    return bCryptPasswordEncoder.encode(password);
  }

  @ExceptionHandler(value = {EmailExistException.class, AlreadyRegistEmail.class
      , UserNotEmailAuthException.class})
  public ResponseEntity<?> ExceptionHandler(RuntimeException exception) {
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  //회원가입
  @ApiOperation(value = "회원가입", notes = "회원가입API 입니다. 해당하는 입력값을 넣어주세요.")
  @PostMapping("/user/register")
  public ResponseEntity<?> userRegister(
      @ModelAttribute @Valid @ApiParam(value = "사용자 아이디와 비밀번호 : ") UserInput userInput
      , @ApiIgnore Errors error) {

    List<ResponseError> responseErrorList = new ArrayList<>();
    if (error.hasErrors()) {
      error.getAllErrors().stream().forEach((e) -> {
        responseErrorList.add(ResponseError.of((FieldError) e));
      });
      return ResponseEntity.badRequest().body(responseErrorList);
    }

    //동일한 이메일 계정이 있다면 예외처리
    if (userRepository.countByUserId(userInput.getUserId()) > 0) {
      //아래처럼 예외처리를 불러오는 방법이 맞는건지?,,,예외메세지를 api문서에서 보이게끔
      // 하려고 합니다. 궁금합니다.
      return ResponseEntity.badRequest().body(new AlreadyRegistEmail("이미가입된 이메일 계정입니다."));
    }

    String encryptPassword = getEncryptPassword(userInput.getPassword());
    String uuid = UUID.randomUUID().toString();

    User user = User.builder()
        .userId(userInput.getUserId())
        .userName(userInput.getUserName())
        .userNickName(userInput.getNickName())
        .phone(userInput.getPhone())
        .password(encryptPassword)
        .regDt(LocalDateTime.now())
        .adminYn(false)
        .emailAuthYn(false)
        .emailAuthKey(uuid)
        .userStatus(UserStatus.REQ.toString())
        .userRole(UserRole.USER.toString())
        .build();
    userRepository.save(user);

    //인증 이메일 전송

    String email = userInput.getUserId();
    String subject = "00커뮤니티 사이트 가입을 축하드립니다.";
    String text = "<p> 00커뮤니티 사이트 가입을 축하드립니다. <p><p>아래 링크를 통해 인증하여 가입을 완료 하세요. </p>"
        + "<div><a target='_blank' href='http://localhost:8080/member/email-auth?id=" + uuid
        + "'>가입 완료 </a></div>";
    mailComponents.sendMail(email, subject, text);

    return ResponseEntity.ok()
        .body("회원가입에 성공하였습니다. 회원 고유 번호는 (" + user.getUserId() + ") 입니다. 이메일을 통해 인증해주세요.");
  }

  //이메일 인증
  @ApiIgnore
  @GetMapping("/member/email-auth")
  public String emailAuth(HttpServletRequest request) {
    String uuid = request.getParameter("id");
    userService.emailAuth(uuid);

    return "이메일 인증 완료";
  }

  //로그인
  @ApiOperation(value = "로그인", notes = "로그인API 입니다. 아이디와 비밀번호를 입력하세요.")
  @PostMapping("/user/login")
  public ResponseEntity<?> userLogin(@ModelAttribute UserInputLogin userInputLogin
      , @ApiIgnore Errors error) {
    List<ResponseError> responseErrorList = new ArrayList<>();
    if (error.hasErrors()) {
      error.getAllErrors().stream().forEach((e) -> {
        responseErrorList.add(ResponseError.of((FieldError) e));
      });
      return ResponseEntity.badRequest().body(responseErrorList);
    }

    Optional<User> user = userRepository.findByUserId(userInputLogin.getUserId());
    //존재하는 계정인지 확인
    if (!user.isPresent()) {
      return ResponseEntity.badRequest().body(new EmailExistException("존재하지 않는 계정입니다."));
    }

    //이메일 인증이 되어있는지 확인
    if (!user.get().isEmailAuthYn()) {
      return ResponseEntity.badRequest().body(new UserNotEmailAuthException("먼저 이메일 인증을 해주세요."));
    }

    //아이디와 비밀번호 확인
    if (!passwordEncoder.matches(userInputLogin.getPassword(), user.get().getPassword())) {
      return ResponseEntity.badRequest()
          .body(new UserLoginNotCorrectPasswordException("아이디와 비밀번호가 일치하지 않습니다."));
    }
    log.info("로그인 성공");
    return ResponseEntity.ok().body("로그인에 성공하였습니다.");
  }

  //비밀번호 수정
  @ApiOperation(value = "비밀번호 수정", notes = "비밀번호 수정 API 입니다. 해당하는 입력값을 넣어주세요.")
  @PatchMapping("/user/{id}/setPassword")
  public ResponseEntity<?> updateUserPassword(@PathVariable Long id,
      @ModelAttribute UserInputPassword userInputPassword
      , @ApiIgnore Errors errors) {
    List<ResponseError> responseErrorList = new ArrayList<>();
    if (errors.hasErrors()) {
      errors.getAllErrors().stream().forEach((e) -> {
        responseErrorList.add(ResponseError.of((FieldError) e));
      });
      return ResponseEntity.badRequest().body(responseErrorList);
    }
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("해당하는 계정이 없습니다."));

    //아이디와 비밀번호 확인
    if (!passwordEncoder.matches(userInputPassword.getPassword(), user.getPassword())) {
      return ResponseEntity.badRequest()
          .body(new UserLoginNotCorrectPasswordException("아이디와 비밀번호가 일치하지 않습니다."));
    }
    user.setPassword(getEncryptPassword(userInputPassword.getNewPassword()));
    userRepository.save(user);

    return ResponseEntity.ok().body("비밀번호가 정상적으로 수정되었습니다.");
  }


  //회원탈퇴
  @ApiOperation(value = "회원탈퇴", notes = "회원탈퇴API 입니다. 해당하는 입력값을 넣어주세요.")
  @PostMapping("/user/delete/{id}")
  public ResponseEntity<?> userDelete(@PathVariable Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

    //해당 아이디의 사용자가 탈퇴를 하려는데
    //사용자가 작성한 게시글이 있는경우 예외 발생

    try {
      userRepository.delete(user);
    } catch (DataIntegrityViolationException e) {
      String message = "제약조건에 문제가 발생하였습니다.";
      return ResponseEntity.badRequest()
          .body(message);
    } catch (Exception e) {
      String message = "회원 탈퇴 중 문제가 발생하였습니다.";
      return ResponseEntity.badRequest()
          .body(message);
    }

    return ResponseEntity.ok().body("회원 탈퇴가 정상적으로 진행되었습니다.");
  }

}

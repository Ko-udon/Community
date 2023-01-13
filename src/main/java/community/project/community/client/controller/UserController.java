package community.project.community.client.controller;

import community.project.community.client.entity.User;
import community.project.community.client.enums.UserRole;
import community.project.community.client.enums.UserStatus;
import community.project.community.client.exception.AlreadyRegistEmail;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@AllArgsConstructor
@Slf4j
public class UserController {

  private final UserRepository userRepository;

  private final MailComponents mailComponents;
  private final UserService userService;


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
    userService.checkSameEmail(userInput.getUserId());

    //회원 정보 저장
    userService.register(userInput);
    
    Optional<User> user = userRepository.findByUserId(userInput.getUserId());

    return ResponseEntity.ok()
        .body("회원가입에 성공하였습니다. 회원 고유 Id값은 "+user.get().getId()+"입니다. 이메일을 통해 인증해주세요.");
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

    Optional<User> optionalUserser = Optional.ofNullable(
        userRepository.findByUserId(userInputLogin.getUserId())
            .orElseThrow(() -> new UserNotFoundException("존재하지 않는 계정입니다.")));
    User user = optionalUserser.get();

    //이메일 인증이 되어있는지 확인
    if (!user.isEmailAuthYn()) {
      return ResponseEntity.badRequest().body(new UserNotEmailAuthException("먼저 이메일 인증을 해주세요."));
    }

    //패스워드 확인
    if (!userService.loginCheckPassword(userInputLogin.getPassword(), user.getPassword())) {
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
    if (!userService.loginCheckPassword(userInputPassword.getPassword(), user.getPassword())) {
      return ResponseEntity.badRequest()
          .body(new UserLoginNotCorrectPasswordException("아이디와 비밀번호가 일치하지 않습니다."));
    }
    user.setPassword(userService.getEncryptPassword(userInputPassword.getNewPassword()));
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

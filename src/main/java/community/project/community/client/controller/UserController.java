package community.project.community.client.controller;

import community.project.community.client.entity.User;
import community.project.community.client.enums.UserRole;
import community.project.community.client.exception.EmailExistException;
import community.project.community.client.model.ResponseError;
import community.project.community.client.model.UserInput;
import community.project.community.client.repository.UserRepository;
import community.project.community.client.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
  private final UserRepository userRepository;

  /*여기부터
  프론트 앤드는 구현 x */

  //사용자 비밀번호 암호화
  private String getEncryptPassword(String password) {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    return bCryptPasswordEncoder.encode(password);
  }

  //회원가입
  @PostMapping("/user/register")
  public ResponseEntity<?> userRegister(@RequestBody @Valid UserInput userInput, Errors error) {
    List<ResponseError> responseErrorList = new ArrayList<>();
    if (error.hasErrors()) {
      error.getAllErrors().stream().forEach((e) -> {
        responseErrorList.add(ResponseError.of((FieldError) e));
      });
      return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
    }

    //동일한 이메일 계정이 있다면 예외처리
    if (userRepository.countByUserId(userInput.getUserId()) > 0) {
      throw new EmailExistException("이미 가입된 이메일 계정입니다.");
    }

    String encryptPassword = getEncryptPassword(userInput.getPassword());
    User user = User.builder()
        .userId(userInput.getUserId())
        .userName(userInput.getUserName())
        .phone(userInput.getPhone())
        .password(encryptPassword)
        .regDt(LocalDateTime.now())
        .adminYn(false)
        .emailAuthYn(false)
        .userStatus(UserRole.USER.toString())
        .build();
    userRepository.save(user);
    return ResponseEntity.ok().build();
  }

  /*@PostMapping("/user/login")
  public void userLogin(@RequestBody UserInput)*/




}

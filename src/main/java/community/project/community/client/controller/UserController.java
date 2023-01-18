package community.project.community.client.controller;

import community.project.community.client.service.UserService;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@AllArgsConstructor
@ApiIgnore
public class UserController {

  private final UserService userService;

  //이메일 인증
  @ApiIgnore
  @GetMapping("/member/email-auth")
  public String emailAuth(HttpServletRequest request) {

    //이메일 인증 화면을 페이지로 띄우기 위해 Controller로 정의
    String uuid = request.getParameter("id");
    boolean result = userService.emailAuth(uuid);
    return "client/email-auth";
  }

}

package community.project.community.client.controller;

import community.project.community.client.model.UserInput;
import community.project.community.client.service.UserService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  //회원가입 페이지 불러오기
  @GetMapping("/user/register")
  public String register() {
    return "client/register";
  }

  //회원가입 정보 받아오기 POST
  @PostMapping("/user/register")
  public String registerSubmit(Model model,
      UserInput parameter) {
    //System.out.println(parameter.toString());

    boolean result = userService.register(parameter);
    model.addAttribute("result", result);

    return "client/register_complete";
  }

  @GetMapping("/member/email-auth")
  public String emailAuth(Model model, HttpServletRequest request) {
    String uuid = request.getParameter("id");
    //System.out.println(uuid);

    boolean result = userService.emailAuth(uuid);
    model.addAttribute("result", result);

    return "client/email-auth";
  }


  //로그인 페이지 불러오기
  @RequestMapping("/user/login")
  public String login() {
    return "client/login";
  }

}

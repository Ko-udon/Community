package community.project.community.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {
  //어드민 페이지 테스트
  @GetMapping("/admin")
  public String adminMain(){
    return "admin/main";
  }
}

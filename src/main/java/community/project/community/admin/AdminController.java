package community.project.community.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

  @GetMapping("/admin")
  public String adminMain() {
    return "admin/main";
  }


}

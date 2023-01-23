package community.project.community.admin;

import community.project.community.client.entity.User;
import community.project.community.client.exception.UserNotFoundException;
import community.project.community.client.model.UserResponse;
import community.project.community.client.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class AdminRestController {

  private final UserRepository userRepository;

  @ApiIgnore
  @GetMapping("/admin")
  public String adminMain() {
    return "admin/main";
  }

  //계정 확인
  @ApiOperation(value = "사용자 확인", notes = "사용자 확인API 입니다. 유저 고유 ID를 입력해주세요.")
  @GetMapping("/api/{adminId}/user/{id}")
  public ResponseEntity<?> getUser(@PathVariable Long id, @PathVariable Long adminId) {
    User adminUser = userRepository.findById(adminId)
        .orElseThrow(() -> new UserNotFoundException("관리자 정보가 없습니다."));

    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

    if (!adminUser.isAdminYn()) {
      return ResponseEntity.badRequest().body("관리자 권한이 없습니다.");
    }
    UserResponse userResponse = UserResponse.of(user);

    return ResponseEntity.ok().body(userResponse);
  }


}

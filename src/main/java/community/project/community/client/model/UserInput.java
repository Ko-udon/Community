package community.project.community.client.model;

import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInput {

  @Email(message = "이메일 형식이 아닙니다.")
  @NotBlank(message = "이메일은 필수로 입력해주세요.")
  private String userId;

  @NotBlank(message = "이메일은 필수로 입력해주세요.")
  private String userName;

  @Size(min=4, message = "비밀번호는 4자 이상 입력해야 합니다.")
  @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
  private String password;

  @Size(max=20,message = "연락처는 최대 20자 입니다.")
  @NotBlank(message = "연락처는 필수 항목 입니다.")
  private String phone;


  private String nickName;
}

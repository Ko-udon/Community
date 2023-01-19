package community.project.community.client.model;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Data
@ToString
public class UserInputFindUserId {

  //아이디 찾기
  @NotBlank
  private String userName;

  @NotBlank
  private String phone;

}

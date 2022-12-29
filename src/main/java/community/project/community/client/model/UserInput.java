package community.project.community.client.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString

public class UserInput {

  private String userId;
  private String userName;
  private String password;
  private String phone;
  private String nickName;

  private String newPassword; //새로운 비밀번호 설정 시



}

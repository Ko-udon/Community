package community.project.community.client.model;

import community.project.community.client.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

  private Long id;
  private String userId;
  private String userName;
  protected String phone;

  public UserResponse(User user) {
    this.id = user.getId();
    this.userId = user.getUserId();
    this.userName = user.getUserName();
    this.phone = user.getPhone();
  }

  public static UserResponse of(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .userId(user.getUserId())
        .userName(user.getUserName())
        .phone(user.getPhone())
        .build();
  }
}

package community.project.community.client.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;  //회원 고유번호 부여

  private String userId;

  private String userName;
  private String userNickName;
  private String phone;
  private String password;
  private LocalDateTime regDt;
  private boolean adminYn;

  private boolean emailAuthYn;
  private LocalDateTime emailAuthDt;
  private String emailAuthKey;

  private String resetPasswordKey;
  private LocalDateTime resetPasswordLimitDt;
  private LocalDateTime updDt;

  private String userStatus;  //이용자의 상태, 정지 이용자 or 사용자
  private String userRole;
}

package community.project.community.client.repository;

import community.project.community.client.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmailAuthKey(String emailAuthKey);  //이메일 인증용

  int countByUserId(String userId); // 회원가입 확인용



}

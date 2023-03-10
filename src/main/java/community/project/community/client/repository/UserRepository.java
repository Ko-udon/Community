package community.project.community.client.repository;

import community.project.community.client.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmailAuthKey(String emailAuthKey);  //이메일 인증용

  boolean existsByUserId(String userId); // 회원가입 확인용

  Optional<User> findByUserId(String userId);

  Optional<Object> findByIdAndPassword(long id, String password); //사용자 비밀번호 업데이트

  Optional<User> findByUserNameAndPhone (String useName, String phone);


}

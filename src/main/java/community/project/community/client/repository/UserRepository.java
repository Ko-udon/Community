package community.project.community.client.repository;

import community.project.community.client.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByEmailAuthKey(String emailAuthKey);  //이메일 인증용


}

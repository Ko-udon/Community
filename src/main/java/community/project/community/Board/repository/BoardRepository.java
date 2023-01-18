package community.project.community.Board.repository;

import community.project.community.Board.entity.Board;
import community.project.community.client.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {

  Board findByUserId(String userId);

}

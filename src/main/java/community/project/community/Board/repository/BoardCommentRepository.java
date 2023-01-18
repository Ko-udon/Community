package community.project.community.Board.repository;

import community.project.community.Board.entity.Board;
import community.project.community.Board.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {


}

package community.project.community.Board.service;

import community.project.community.Board.entity.BoardComment;

public interface BoardCommentService {

  BoardComment addComment(long boardId, String userId,String comment);

}

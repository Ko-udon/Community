package community.project.community.Board.service;

import community.project.community.Board.entity.BoardComment;
import community.project.community.Board.model.BoardCommentModifyInput;

import java.util.List;

public interface BoardCommentService {

  BoardComment addComment(long boardId, String userId,String comment);

  boolean setCommentList(BoardCommentModifyInput boardCommentModifyInput);

}

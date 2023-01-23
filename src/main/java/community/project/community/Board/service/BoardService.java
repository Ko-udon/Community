package community.project.community.Board.service;

import community.project.community.Board.entity.Board;
import community.project.community.Board.model.BoardDeleteInput;
import community.project.community.Board.model.BoardInput;
import community.project.community.Board.model.BoardLikeHateInput;
import community.project.community.Board.model.BoardModifyInput;

public interface BoardService {


  String[] getNewSaveFile(String baseLocalPath, String baseUrlPath,
      String originalFilename);

  Board addBoard(BoardInput boardInput);

  Board modifyBoard(BoardModifyInput boardModifyInput);

  boolean deleteBoard(BoardDeleteInput boardDeleteInput);

  //좋아요, 싫어요 등록
  boolean addLikeHate(long boardId, BoardLikeHateInput boardLikeHateInput);

  //좋아요, 싫어요 수정


}

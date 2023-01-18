package community.project.community.Board.service;

import community.project.community.Board.entity.Board;
import community.project.community.Board.model.BoardDeleteInput;
import community.project.community.Board.model.BoardInput;
import community.project.community.Board.model.BoardModifyInput;

public interface BoardService {


  String[] getNewSaveFile(String baseLocalPath, String baseUrlPath,
      String originalFilename);

  Board addBoard(BoardInput boardInput);

  Board modifyBoard(BoardModifyInput boardModifyInput);

  boolean deleteBoard(BoardDeleteInput boardDeleteInput);

}

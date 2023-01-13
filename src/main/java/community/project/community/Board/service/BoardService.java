package community.project.community.Board.service;

import community.project.community.Board.model.BoardInput;
import community.project.community.Board.model.BoardModifyInput;

public interface BoardService {


  String[] getNewSaveFile(String baseLocalPath, String baseUrlPath,
      String originalFilename);

  boolean addBoard(BoardInput boardInput);

  boolean modifyBoard(BoardModifyInput boardModifyInput);

}

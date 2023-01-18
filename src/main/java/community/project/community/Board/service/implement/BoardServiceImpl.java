package community.project.community.Board.service.implement;

import community.project.community.Board.entity.Board;
import community.project.community.Board.exception.BoardModifyNotMatchUserException;
import community.project.community.Board.exception.BoardNotFoundException;
import community.project.community.Board.model.BoardInput;
import community.project.community.Board.model.BoardModifyInput;
import community.project.community.Board.repository.BoardRepository;
import community.project.community.Board.service.BoardService;
import community.project.community.client.entity.User;
import community.project.community.client.exception.UserNotFoundException;
import community.project.community.client.repository.UserRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
  private final UserRepository userRepository;

  private final BoardRepository boardRepository;

  @Override
  public boolean addBoard(BoardInput boardInput) {

    String saveFilename = "";
    String urlFilename = "";

    //등록한 이미지가 있다면 처리
    if (boardInput.getFile() != null) {
      String originalFilename = boardInput.getFile().getOriginalFilename();
      String baseLocalPath = "D:/IntelliJ/JavaWorkspace/Community/src/main/java/community/project/community/Img";
      String baseUrlPath = "/files";
      String[] arrFilename = getNewSaveFile(baseLocalPath, baseUrlPath, originalFilename);

      saveFilename = arrFilename[0];
      urlFilename = arrFilename[1];

      try {
        File newFile = new File(saveFilename);
        FileCopyUtils.copy(boardInput.getFile().getInputStream(), new FileOutputStream(newFile));
      } catch (IOException e) {
        log.info(e.getMessage());
      }
    }
    User user = userRepository.findByUserId(boardInput.getUserId())
        .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

    Board board = boardRepository.save(Board.builder()
        .userId(user.getUserId())
        .userNickName(user.getUserNickName())
        .title(boardInput.getTitle())
        .contents(boardInput.getContents())
        .registerDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .likes(0)
        .hates(0)
        .filename(saveFilename)
        .urlFilename(urlFilename)
        .build());

    return true;
  }


  //게시글 수정
  @Override
  public boolean modifyBoard(BoardModifyInput boardModifyInput) {

    Board board = boardRepository.findById(boardModifyInput.getBoardId())
        .orElseThrow(() -> new BoardNotFoundException("해당하는 게시판이 존재하지 않습니다."));

    if (!(board.getUserId() == boardModifyInput.getUserId())) {
      throw new BoardModifyNotMatchUserException("게시글 수정의 권한이 없습니다.");
    }

    if (boardModifyInput.getContents() != null) {
      board.setTitle(boardModifyInput.getTitle());
    }
    if (boardModifyInput.getTitle() != null) {
      board.setContents(boardModifyInput.getContents());
    }
    if (boardModifyInput.getFile() != null) {
      String saveFilename = "";
      String urlFilename = "";
      String originalFilename = boardModifyInput.getFile().getOriginalFilename();
      String baseLocalPath = "D:/IntelliJ/JavaWorkspace/Community/src/main/java/community/project/community/Img";
      String baseUrlPath = "/files";
      String[] arrFilename = getNewSaveFile(baseLocalPath, baseUrlPath, originalFilename);

      saveFilename = arrFilename[0];
      urlFilename = arrFilename[1];

      try {
        File newFile = new File(saveFilename);
        FileCopyUtils.copy(boardModifyInput.getFile().getInputStream(),
            new FileOutputStream(newFile));
      } catch (IOException e) {
        log.info(e.getMessage());
      }

      board.setFilename(saveFilename);
      board.setUrlFilename(urlFilename);
    }

    boardRepository.save(board);

    return false;
  }

  //등록된 파일 저장
  @Override
  public String[] getNewSaveFile(String baseLocalPath, String baseUrlPath,
      String originalFilename) {
    LocalDate now = LocalDate.now();
    String[] dirs = {
        String.format("%s/%d/", baseLocalPath, now.getYear()),
        String.format("%s/%d/%02d/", baseLocalPath, now.getYear(), now.getMonthValue()),
        String.format("%s/%d/%02d/%02d/", baseLocalPath, now.getYear(), now.getMonthValue(),
            now.getDayOfMonth())};

    String urlDir = String.format("%s/%d/%02d/%02d/", baseUrlPath, now.getYear(),
        now.getMonthValue(), now.getDayOfMonth());

    for (String dir : dirs) {
      File file = new File(dir);
      if (!file.isDirectory()) {
        file.mkdir();   //디렉토리가 없으면 생성하기
      }
    }
    String fileExtension = "";
    if (originalFilename != null) {
      int dotPos = originalFilename.lastIndexOf(".");
      if (dotPos > -1) {
        fileExtension = originalFilename.substring(dotPos + 1);
      }
    }

    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
    String newFilename = String.format("%s%s", dirs[2], uuid);
    String newUrlFilename = String.format("%s%s", urlDir, uuid);
    if (fileExtension.length() > 0) {
      newFilename += "." + fileExtension;
      newUrlFilename += "." + fileExtension;
    }
    return new String[]{newFilename, newUrlFilename};
  }




}

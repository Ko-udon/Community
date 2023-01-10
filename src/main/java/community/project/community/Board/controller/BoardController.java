package community.project.community.Board.controller;

import community.project.community.Board.entity.Board;
import community.project.community.Board.exception.BoardModifyNotMatchUserException;
import community.project.community.Board.exception.BoardNotFoundException;
import community.project.community.Board.model.BoardInput;
import community.project.community.Board.model.BoardModifyInput;
import community.project.community.Board.repository.BoardRepository;
import community.project.community.client.entity.User;
import community.project.community.client.exception.UserNotFoundException;
import community.project.community.client.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardController {

  private final BoardRepository boardRepository;
  private final UserRepository userRepository;


  //특정 게시판 글 가져오기
  @ApiOperation(value = "특정 게시판 글 불러오기", notes = "게시판 고유ID를 입력해주세요.")
  @GetMapping("/board/list/{boardId}")
  public Board boardList(@PathVariable long boardId) {
    Optional<Board> board = boardRepository.findById(boardId);
    //예외처리 필요
    return board.get();
  }

  //게시판 글 목록 가져오기
  @ApiOperation(value = "게시판 글 목록 불러오기", notes = "불러올 게시판 글의 수(size)를 입력해주세요.")
  @GetMapping("/board/list/latest/{size}")
  public Page<Board> boardList(@PathVariable int size) {
    Page<Board> boardList = boardRepository.findAll(PageRequest.of(0, size
        , Direction.DESC, "regDt"));
    return boardList;
  }


  //게시판 글 추가
  @ApiOperation(value = "게시판 글 작성", notes = "회원 고유ID 입력 후 제목과 내용을 입력해주세요. 업로드할 파일이 있다면 올려주세요.")
  @PostMapping("/board/add")
  public ResponseEntity addBoard(@ModelAttribute BoardInput boardInput)
      throws IOException {

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
    User user = userRepository.findById(boardInput.getUserId())
        .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

    Board board = boardRepository.save(Board.builder()
        .userId(user.getId())
        .userEmail(user.getUserId())
        .userNickName(user.getUserNickName())
        .title(boardInput.getTitle())
        .contents(boardInput.getContents())
        .regDt(LocalDateTime.now())
        .updDt(LocalDateTime.now())
        .likes(0)
        .hates(0)
        .filename(saveFilename)
        .urlFilename(urlFilename)
        .build());
    return ResponseEntity.ok()
        .body("게시글이 정상적으로 업로드 되었습니다. 게시글의 등록번호는" + board.getBoardId() + "입니다.");
  }

  //등록된 파일 저장
  private String[] getNewSaveFile(String baseLocalPath, String baseUrlPath,
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

  //게시판 글 삭제
  @ApiOperation(value = "게시판 삭제하기", notes = "삭제할 게시판의 고유 Id를 입력해주세요.")
  @DeleteMapping("/board/delete/{boardId}")
  public ResponseEntity deleteBoard(@ModelAttribute @PathVariable long boardId) {
    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new BoardNotFoundException("해당하는 게시판이 존재하지 않습니다."));
    boardRepository.deleteById(boardId);
    return ResponseEntity.ok().body("정상적으로 삭제되었습니다.");
  }

  //게시판 글 수정
  @PatchMapping("/board/modify")
  public ResponseEntity modifyBoard(@ModelAttribute BoardModifyInput boardModifyInput) {
    Board board = boardRepository.findById(boardModifyInput.getBoardId())
        .orElseThrow(() -> new BoardNotFoundException("해당하는 게시판이 존재하지 않습니다."));
    //어떤 방식이 더 나을지 고민중
    /*if (!board.isPresent()) {
      return ResponseEntity.badRequest().body(new BoardNotFoundException("해당하는 게시판이 존재하지 않습니다."));
    }*/
    //게시글을 작성한 유저의 아이디와 수정하려는 유저의 아이디가 일치하지 않은 경우
    if (!(board.getUserId() == boardModifyInput.getUserId())) {
      return ResponseEntity.badRequest()
          .body(new BoardModifyNotMatchUserException("게시글 수정의 권한이 없습니다."));
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

    return ResponseEntity.ok().body("게시글 수정을 완료하였습니다: " + board);
  }

}

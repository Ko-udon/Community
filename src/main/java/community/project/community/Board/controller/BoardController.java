package community.project.community.Board.controller;

import community.project.community.Board.entity.Board;
import community.project.community.Board.exception.BoardNotFoundException;
import community.project.community.Board.model.BoardInput;
import community.project.community.Board.model.BoardModifyInput;
import community.project.community.Board.repository.BoardRepository;
import community.project.community.Board.service.BoardService;
import community.project.community.client.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardController {

  private final BoardRepository boardRepository;
  private final UserRepository userRepository;
  private final BoardService boardService;


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
  public ResponseEntity addBoard(@ModelAttribute BoardInput boardInput) {

    //게시글 작성
    boardService.addBoard(boardInput);

    Board board = boardRepository.findByUserId(boardInput.getUserId());
    return ResponseEntity.ok()
        .body("게시글이 정상적으로 업로드 되었습니다. 게시글의 등록번호는" + board.getBoardId() + "입니다.");
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

    boardService.modifyBoard(boardModifyInput);

    Board board = boardRepository.findByUserId(boardModifyInput.getUserId());
    return ResponseEntity.ok().body("게시글 수정을 완료하였습니다: " + board);
  }

}

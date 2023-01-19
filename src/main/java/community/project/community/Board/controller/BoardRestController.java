package community.project.community.Board.controller;

import community.project.community.Board.entity.Board;
import community.project.community.Board.entity.BoardComment;
import community.project.community.Board.entity.BoardLikeHate;
import community.project.community.Board.exception.BoardNotFoundException;
import community.project.community.Board.model.*;
import community.project.community.Board.repository.BoardCommentRepository;
import community.project.community.Board.repository.BoardRepository;
import community.project.community.Board.service.BoardCommentService;
import community.project.community.Board.service.BoardService;
import io.swagger.annotations.ApiOperation;

import java.util.List;
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
public class BoardRestController {

    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final BoardCommentService boardCommentService;

    private final BoardCommentRepository boardCommentRepository;


    //특정 게시판 글 가져오기
    @ApiOperation(value = "특정 게시글 불러오기", notes = "게시판 고유ID를 입력해주세요.")
    @GetMapping("/board/list/{boardId}")
    public Board boardList(@PathVariable long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("해당하는 게시판이 존재하지 않습니다."));

        return board;
    }

    //게시판 글 목록 가져오기
    @ApiOperation(value = "게시판 글 목록 불러오기", notes = "불러올 게시판 글의 수(size)를 입력해주세요.")
    @GetMapping("/board/list/latest/{size}")
    public Page<Board> boardList(@PathVariable int size) {
        Page<Board> boardList = boardRepository.findAll(PageRequest.of(0, size
                , Direction.DESC, "registerDate"));
        return boardList;
    }

    //특정 유저가 작성한 글 목록 가져오기
    @ApiOperation(value = "특정 유저가 작성한 글 목록 가져오기")
    @GetMapping("/board/find/list/{userId}")
    public ResponseEntity<?> findBoardList(@PathVariable String userId) {
        List<Board> boardList = boardRepository.findByUserId(userId);

        return ResponseEntity.ok().body(boardList);
    }

    //게시판 글 추가
    @ApiOperation(value = "게시글 작성", notes = "회원 ID 입력 후 제목과 내용을 입력해주세요. 업로드할 파일이 있다면 올려주세요.")
    @PostMapping("/board/add")
    public ResponseEntity addBoard(@ModelAttribute BoardInput boardInput) {

        //게시글 작성
        Board board = boardService.addBoard(boardInput);

        return ResponseEntity.ok()
                .body("게시글이 정상적으로 업로드 되었습니다. 게시글의 등록번호는" + board.getBoardId() + "입니다.");
    }

    //게시판 글 삭제
    @ApiOperation(value = "게시글 삭제하기", notes = "삭제할 게시판의 고유 Id를 입력해주세요.")
    @DeleteMapping("/board/delete/{boardId}")
    public ResponseEntity deleteBoard(@ModelAttribute BoardDeleteInput boardDeleteInput) {

        boolean result = boardService.deleteBoard(boardDeleteInput);
        if (!result) {
            return ResponseEntity.badRequest().body("게시글이 삭제가 실패했습니다.");
        }

        return ResponseEntity.ok().body("정상적으로 삭제되었습니다.");
    }

    //게시판 글 수정
    @ApiOperation(value = "게시글 수정하기", notes = "수정할 게시판의 고유 Id를 입력해주세요.")
    @PatchMapping("/board/modify")
    public ResponseEntity<?> modifyBoard(@ModelAttribute BoardModifyInput boardModifyInput) {

        Board board = boardService.modifyBoard(boardModifyInput);

        return ResponseEntity.ok().body("게시글 수정을 완료하였습니다: " + board);
    }

    //게시글에 댓글 작성

    @ApiOperation(value = "게시글 댓글 작성")
    @PostMapping("/board/write/comment")
    public ResponseEntity<?> addBoardComment(@ModelAttribute BoardCommentInput boardCommentInput) {

        //게시글 댓글 작성
        BoardComment boardComment = boardCommentService.addComment(boardCommentInput.getBoardId()
                , boardCommentInput.getUserId(), boardCommentInput.getComment());

        return ResponseEntity.ok()
                .body("댓글이 정상적으로 업로드 되었습니다.");
    }

    //게시글의 댓글 목록
    @ApiOperation(value = "게시글 댓글 조회")
    @GetMapping("/board/comment/list/{boardId}")
    public ResponseEntity<?> commentList(@PathVariable long boardId) {
        Board board = boardRepository.findByBoardId(boardId)
                .orElseThrow(() -> new BoardNotFoundException("해당 게시글이 존재하지 않습니다."));

        List<BoardComment> boardCommentList = boardCommentRepository.findAllByBoard(board);

        return ResponseEntity.ok().body(boardCommentList);

    }

    //댓글 수정
    @ApiOperation(value = "게시글 댓글 수정")
    @PostMapping("/board/set/comment/list")
    public ResponseEntity<?> setCommentList(@ModelAttribute BoardCommentModifyInput boardCommentModifyInput) {

        boolean result = boardCommentService.setCommentList(boardCommentModifyInput);
        if (!result) {
            return ResponseEntity.badRequest().body("댓글 수정에실패하였습니다.");
        }

        return ResponseEntity.ok().body("댓글 수정 완료");

    }


    //좋아요 or 싫어요 추가
    @ApiOperation(value = "좋아요 또는 싫어요 추가")
    @PostMapping("/board/add/likeOrHate/{boardId}")
    public ResponseEntity<?> addLikeHate(@PathVariable long boardId
            , @ModelAttribute BoardLikeHateInput boardLikeHateInput) {

        boolean result = boardService.addLikeHate(boardId, boardLikeHateInput);
        if (!result) {
            return ResponseEntity.badRequest().body("좋아요 / 싫어요 등록에 실패하였습니다.");
        }

        return ResponseEntity.ok().body("좋아요 / 싫어요 등록에 성공했습니다.");
    }


}

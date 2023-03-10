package community.project.community.Board.service.implement;

import community.project.community.Board.entity.Board;
import community.project.community.Board.entity.BoardComment;
import community.project.community.Board.exception.BoardCommentNotFoundException;
import community.project.community.Board.exception.BoardModifyNotMatchUserException;
import community.project.community.Board.exception.BoardNotFoundException;
import community.project.community.Board.model.BoardCommentModifyInput;
import community.project.community.Board.model.BoardInput;
import community.project.community.Board.model.BoardModifyInput;
import community.project.community.Board.repository.BoardCommentRepository;
import community.project.community.Board.repository.BoardRepository;
import community.project.community.Board.service.BoardCommentService;
import community.project.community.Board.service.BoardService;
import community.project.community.client.entity.User;
import community.project.community.client.exception.UserNotFoundException;
import community.project.community.client.repository.UserRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardCommentServiceImpl implements BoardCommentService {

    private final UserRepository userRepository;

    private final BoardRepository boardRepository;

    private final BoardCommentRepository boardCommentRepository;


    //댓글 작성
    @Override
    public BoardComment addComment(long boardId, String userId, String comment) {

        //댓글 작성 전, 게시글 존재와 유저 존재 여부
        Board board = boardRepository.findByBoardId(boardId)
                .orElseThrow(() -> new BoardNotFoundException("해당 게시글이 존재하지 않습니다."));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));

        //댓글 작성
        BoardComment boardComment = BoardComment.builder()
                .comment(comment)
                .board(board)
                .userId(userId)
                .registerDate(LocalDateTime.now())
                .build();

        boardCommentRepository.save(boardComment);
        return boardComment;
    }

    @Override
    public boolean setCommentList(BoardCommentModifyInput boardCommentModifyInput) {

        //댓글 수정 전, 해당 댓글 존재 여부 확인
        BoardComment boardComment = boardCommentRepository.findById(boardCommentModifyInput.getBoardCommentId())
                .orElseThrow(() -> new BoardCommentNotFoundException("해당하는 댓글이 존재하지 않습니다."));

        User user = userRepository.findByUserId(boardCommentModifyInput.getUserId())
                .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));

        //수정하려는 유저와 작성한 유저가 일치하는지 확인
        if (!boardComment.getUserId().equals(user.getUserId())) {
            return false;
        }

        //일치할경우 댓글 내용 수정
        boardComment.setComment(boardCommentModifyInput.getComment());
        boardComment.setUpdateDate(LocalDateTime.now());
        boardCommentRepository.save(boardComment);

        return true;
    }
}

package community.project.community.Board.model;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardCommentInput {

  //작성자 정보
  @NotBlank(message = "작성자 정보는 필수로 입력해야 합니다.")
  private String userId;

  //게시글 정보
  @NotBlank(message = "게시글 정보는 필수로 입력해야 합니다.")
  private long boardId;

  //댓글 내용
  @NotBlank(message = "댓글은 필수 항목입니다.")
  private String comment;

}

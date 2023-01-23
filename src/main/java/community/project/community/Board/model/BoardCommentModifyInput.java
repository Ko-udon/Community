package community.project.community.Board.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardCommentModifyInput {

  //작성자 정보
  @NotBlank(message = "작성자 정보는 필수로 입력해야 합니다.")
  private String userId;

  //게시글 정보
  @NotBlank(message = "댓글 정보는 필수로 입력해야 합니다.")
  private long boardCommentId;

  //댓글 내용
  @NotBlank(message = "수정할 댓글 내용은 필수 항목입니다.")
  private String comment;

}

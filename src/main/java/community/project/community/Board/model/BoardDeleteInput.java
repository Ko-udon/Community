package community.project.community.Board.model;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDeleteInput {

  @NotBlank(message = "삭제할 게시글 번호는 필수입니다.")
  private long boardId;

  @NotBlank(message = "삭제를 시도하는 유저의 아이디를 입력하세요.")
  private String userId;

}

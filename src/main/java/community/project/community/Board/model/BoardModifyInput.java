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
public class BoardModifyInput {

  @NotBlank(message = "게시글 Id는 필수로 입력해야 합니다.")
  private long boardId;

  //작성자 정보
  private long userId;

  //게시글 내용
  private String title;
  private String contents;

  //파일첨부
  private MultipartFile file;
}

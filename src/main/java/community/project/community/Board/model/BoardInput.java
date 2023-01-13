package community.project.community.Board.model;

import java.time.LocalDateTime;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class BoardInput {

  //작성자 정보
  @NotBlank(message = "작성자 정보가 없습니다.")
  private String  userId;

  //게시글 내용
  @NotBlank(message = "게시글 제목은 필수 항목입니다.")
  private String title;
  @NotBlank(message = "게시글 내용을 입력해주세요.")
  private String contents;

  //파일첨부
  private MultipartFile file;
}

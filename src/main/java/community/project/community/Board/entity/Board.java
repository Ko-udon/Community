package community.project.community.Board.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long boardId;

  private String userId;
  private String userNickName;
  private String title;
  private String contents;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;

  private int likes;
  private int hates;

  String filename;
  String urlFilename;

}

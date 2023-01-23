package community.project.community.Board.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

  //Cannot call sendError() after the response has been committed 오류 처리
  //@JsonIgnore
  @JsonIgnore
  @OneToMany(mappedBy = "board",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
  private List<BoardComment> boardCommentList;

  @ElementCollection
  private List<String> likeList;

  @ElementCollection
  @CollectionTable(name = "hateList")
  private List<String> hateList;


}

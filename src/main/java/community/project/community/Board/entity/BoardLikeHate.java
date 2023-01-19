package community.project.community.Board.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardLikeHate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String userId;

  private String value;

  @ManyToOne
  @JoinColumn(name = "board_id")
  private Board board;


}



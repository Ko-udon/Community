package community.project.community.Board.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotBlank
  private String comment;

  @NotBlank
  private String userId;

  private LocalDateTime registerDate;

  private LocalDateTime updateDate;

  @ManyToOne
  @JoinColumn(name = "board_id")
  private Board board;





}



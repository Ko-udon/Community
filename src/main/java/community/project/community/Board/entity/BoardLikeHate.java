package community.project.community.Board.entity;

import community.project.community.client.entity.User;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardLikeHate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String value;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;



}



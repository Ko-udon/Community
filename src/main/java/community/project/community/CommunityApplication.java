package community.project.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@MapperScan(basePackages = {"community.project.community.Board.Image"})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CommunityApplication {
  public static void main(String[] args) {
    SpringApplication.run(CommunityApplication.class, args);
  }

}

package community.project.community.configuration;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException {
    //log.info("authentication : " + authentication.getName());
    response.sendRedirect("/"); //인증이 성공한 후에는 home으로 이동
  }
}

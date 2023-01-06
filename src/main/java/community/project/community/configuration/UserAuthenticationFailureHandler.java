package community.project.community.configuration;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {

    String msg = "로그인에 실패하였습니다.";
    if (exception instanceof InternalAuthenticationServiceException) {
      msg = exception.getMessage();
    }

    setUseForward(true);
    setDefaultFailureUrl("/user/login?error=true");
    request.setAttribute("errorMessage", msg);  //에러메세지 html로 전달

    //System.out.println("로그인에 실패하였습니다.");

    super.onAuthenticationFailure(request, response, exception);
  }

}

package community.project.community.configuration;

import community.project.community.client.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration  {

  //private final UserService userService;


  @Bean
  PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  //예외처리 핸들러
  @Bean
  UserAuthenticationFailureHandler getFailureHandler(){
    return new UserAuthenticationFailureHandler();
  }

  @Bean
  public AuthenticationSuccessHandler successHandler() {
    return new CustomAuthSuccessHandler();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    String hierarchy = "ROLE_ADMIN > ROLE_USER";
    roleHierarchy.setHierarchy(hierarchy);

    return roleHierarchy;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    http.csrf().disable(); //Forbidden, 403 에러. 권한 문제로 발생하는 오류로, 스프링 시큐리티는 csrf 토큰이 필요한데 이가 없어서 발생

    //로그인 전에 접속하는 권한 주기
    http.authorizeRequests()
        .antMatchers("/"
            ,"/user/register"
            ,"/user/email-auth"
            ,"/user/find-password"
            ,"/user/reset/password"
            ,"/user/login"
        )
        .permitAll();

    http.authorizeRequests()
        .antMatchers("/admin/**")
        .hasAnyAuthority("ROLE_ADMIN")   //관리자 페이지 접근 권한 부여
        .anyRequest().authenticated();  //그 외 모든 요청은 인증 필요

    http.formLogin()
        .defaultSuccessUrl("/")
        .failureUrl("/login?error=true")
        .loginPage("/user/login")
        .successHandler(successHandler())
        .failureHandler(getFailureHandler())
        .usernameParameter("username")
        .passwordParameter("password");

    http
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/login");


    return http.build();
  }



  //* 이렇게 세션을 설정하고 사용할 때, 로그아웃 후 해당 아이디로 다시 로그인을 하게 되면 Session에 인증 정보가 남아있어서 로그인이 안 되는 상황이 발생할 수 있다.
  //그럴 경우 ServletListenerRegistrationBean을 등록하게 되면 해당 문제가 발생하지 않는다.
  @Bean
  public static ServletListenerRegistrationBean httpSessionEventPublisher() {
    return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
  }



}

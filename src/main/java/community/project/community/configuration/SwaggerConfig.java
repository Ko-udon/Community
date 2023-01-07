package community.project.community.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry){
    resourceHandlerRegistry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");

    resourceHandlerRegistry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars");

  }
  @Bean
  public Docket api(){
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("community.project.community"))
        .paths(PathSelectors.any())
        .build().apiInfo(apiInfo());
  }
  private ApiInfo apiInfo(){
    return new ApiInfoBuilder()
        .title("커뮤니티 사이트 API")
        .description("커뮤니티에 필요한 기능을 CRUD 할 수 있는 백엔드 API입니다.")
        .version("1.0")
        .build();
  }

}

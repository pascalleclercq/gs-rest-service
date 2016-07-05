package hello;
 
import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.regex;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ImplicitGrant;
import springfox.documentation.service.LoginEndpoint;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
 
@SpringBootApplication
@EnableSwagger2
@ComponentScan("hello")
public class Application {
 
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public Docket apiDocumentation() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("api").apiInfo(apiInfo())
                .select().paths(regex("/greeting.*")).build()
                .securitySchemes(newArrayList(securitySchema()))
                .securityContexts(newArrayList(securityContext()));
    }


    private OAuth securitySchema() {
        AuthorizationScope authorizationScope = new AuthorizationScope(SwaggerConfig.authorizationScopeGlobal, SwaggerConfig.authorizationScopeGlobal);
        LoginEndpoint loginEndpoint = new LoginEndpoint("https://graph.facebook.com/oauth/access_token");
        GrantType grantType = new ImplicitGrant(loginEndpoint, "access_token");
        return new OAuth(SwaggerConfig.securitySchemaOAuth2, newArrayList(authorizationScope), newArrayList(grantType));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(regex("/greeting.*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope(SwaggerConfig.authorizationScopeGlobal, SwaggerConfig.authorizationScopeGlobalDesc);
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference(SwaggerConfig.securitySchemaOAuth2, authorizationScopes));
    }
    
    
//    @Bean
//    public Docket newsApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("greetings")
//                .apiInfo(apiInfo())
//                .select()
//                .paths(regex("/greeting.*"))
//                .build();
//    }
     
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring REST Sample with Swagger")
                .description("Spring REST Sample with Swagger")
                .termsOfServiceUrl("http://www-03.ibm.com/software/sla/sladb.nsf/sla/bm?Open")
                .contact("Niklas Heidloff")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE")
                .version("2.0")
                .build();
    }
    
    @Bean
    public SecurityConfiguration securityConfiguration(){
        SecurityConfiguration config = new SecurityConfiguration("233668646673605", "33b17e044ee6a4fa383f46ec6e28ea1d", "demo", "demo","",ApiKeyVehicle.QUERY_PARAM,"","");
        return config;
    }
    
    
}
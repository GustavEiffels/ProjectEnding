package com.make.plan.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger
{
    private ApiInfo commonInfo() {
        return new ApiInfoBuilder()
                .title("Plan API")
                .version("TEST")
                .build();
    }

    @Bean
    public Docket allApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("USER")
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any()) // 기존코드
                .paths(PathSelectors.any()) // 기존코드
                .build()
                .apiInfo(commonInfo());
    }
}
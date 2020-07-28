package com.lambdaschool.foundation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

/**
 * Configures the default Swagger Documentation
 */
@Configuration
@EnableSwagger2
public class Swagger2Config
{
    /**
     * Configures what to document using Swagger
     *
     * @return A Docket which is the primary interface for Swagger configuration
     */
    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors
                              .basePackage("com.lambdaschool.foundation"))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .useDefaultResponseMessages(false) // Allows only my exception responses
                .apiInfo(apiEndPointsInfo());
    }

    /**
     * Configures some information related to the Application for Swagger
     *
     * @return ApiInfo a Swagger object containing identification information for this application
     */
    private ApiInfo apiEndPointsInfo()
    {
        return new ApiInfoBuilder().title("Api Documentation bruh")
                .description("Wow shiny!")
                .contact(new Contact("Don't Click This!",
                                     "https://www.youtube.com/watch?v=oHg5SJYRHA0",
                                     "glassbonesofficial@gmail.com"))
                .license("Don't Click This Either!")
                .licenseUrl("https://www.youtube.com/watch?v=oHg5SJYRHA0")
                .version("1.0.0")
                .build();
    }
}
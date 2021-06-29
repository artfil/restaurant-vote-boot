package com.artfil.restaurantvoteboot.config;

import com.artfil.restaurantvoteboot.util.JsonUtil;
import com.artfil.restaurantvoteboot.util.converter.DateFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

import static springfox.documentation.builders.PathSelectors.ant;

@Configuration
@Slf4j
public class AppConfig implements WebMvcConfigurer {


    @Autowired
    public void storeObjectMapper(ObjectMapper objectMapper) {
        JsonUtil.setMapper(objectMapper);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                // https://github.com/springfox/springfox/issues/1139
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(ant("/rest/**"))
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("REST API documentation")
                        .description("Система голосования за рестораны")
                        .version("1.0")
                        .build())
                .securitySchemes(List.of(HttpAuthenticationScheme.BASIC_AUTH_BUILDER.name("basicAuth").build()))
                .securityContexts(List.of(SecurityContext.builder()
                        .securityReferences(List.of(new SecurityReference("basicAuth", new AuthorizationScope[0])))
                        .build()
                ));
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter());
    }
}

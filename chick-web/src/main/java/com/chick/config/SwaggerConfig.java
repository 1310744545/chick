package com.chick.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


/**
 * 通过 http://localhost:9094/swagger-ui.html 访问swagger页面
 *
 * @author lkf
 * @date Created in 2020/4/21 18:25
 * @description springboot入口
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 令牌请求头属性名
     */
    @Value("${jwt.header:Authorization}")
    private String tokenHeader;

    @Bean(value = "defaultApi")
    public Docket api() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name(tokenHeader)
                .description("token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("Bearer ")
                .required(true)
                .build();
        pars.add(ticketPar.build());
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .pathMapping("/")
                .select()
                .apis(Predicates.or(
                        RequestHandlerSelectors.basePackage("com.chick.controller"),
                        RequestHandlerSelectors.basePackage("com.chick.second.hotel.controller")
                ))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .paths(Predicates.not(PathSelectors.regex("/actuator.*")))
                .paths(PathSelectors.regex("/.*"))
                .paths(PathSelectors.any())
                .build().groupName("chick")
                .globalOperationParameters(pars);
    }

    /**
     * 配置在线文档的基本信息
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("chick")
                .description("小鸡工具")
                .termsOfServiceUrl("http://www.zrar.com")
                .version("v1.0")
                .build();
    }
}

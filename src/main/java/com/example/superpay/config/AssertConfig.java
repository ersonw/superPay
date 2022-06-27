package com.example.superpay.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AssertConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/").addResourceLocations( "classpath:/META-INF/resources/static/");
        registry.addResourceHandler("/admin/**").addResourceLocations( "classpath:/static/");
        registry.addResourceHandler("/web/**").addResourceLocations( "classpath:/web/");
        registry.addResourceHandler("/upload/**").addResourceLocations( "classpath:/static/upload/");
        registry.addResourceHandler("index.html").addResourceLocations( "classpath:/");
        //从这里开始，是我加的swagger的静态资源
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT","OPTIONS")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        List<String> exclude = new ArrayList<>();
//        exclude.add("/fonts/**");
//        exclude.add("/static/**");
//        exclude.add("/css/**");
//        exclude.add("/js/**");
//        exclude.add("/img/**");
//        exclude.add("/*");
//        exclude.add("/api/*");
//        exclude.add("/**");
//        AuthInterceptor authInterceptor = new AuthInterceptor();
//        registry.addInterceptor(authInterceptor).addPathPatterns("/**")
//                .excludePathPatterns(exclude)
//                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CustomMethodArgumentResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}

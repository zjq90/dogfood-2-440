package com.lzh.idouban.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lzh.idouban.interceptor.LoginInterceptor;
import com.lzh.idouban.interceptor.VisitorInterceptor;

import javax.annotation.Resource;

/**
 * Web MVC 配置类
 * @author 林泽鸿
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Resource
    private VisitorInterceptor visitorInterceptor;

    @Value("${idouban.upload.image-path:upload/images/}")
    private String imageUploadPath;

    @Value("${idouban.upload.portrait-path:upload/portrait/}")
    private String portraitUploadPath;

    /**
     * 配置视图控制器
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 默认首页跳转到登录页
        registry.addRedirectViewController("/", "/login");
        // 登录页面
        registry.addViewController("/login").setViewName("login");
        // 注册页面
        registry.addViewController("/register").setViewName("register");
    }

    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",
                        "/register",
                        "/user/login",
                        "/user/register",
                        "/user/verifyCode",
                        "/user/findBack",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/upload/**",
                        "/error"
                );

        // 访客统计拦截器
        registry.addInterceptor(visitorInterceptor)
                .addPathPatterns("/**");
    }

    /**
     * 配置静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 上传图片资源映射
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/" + imageUploadPath,
                                      "file:" + System.getProperty("user.dir") + "/" + portraitUploadPath);
    }

}

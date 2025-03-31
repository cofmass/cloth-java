package com.cofmass.clothRoomBackend.config;

import com.cofmass.clothRoomBackend.interceptor.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webConfig implements WebMvcConfigurer {

    @Autowired
    Interceptor interceptor;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 允许凭证
//        需要将addAllowedOrigin替换成addAllowedOriginPattern
        config.addAllowedOriginPattern("*"); // 允许所有来源
        config.addAllowedHeader("*"); // 允许所有头
        config.addAllowedMethod("*"); // 允许所有方法

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 对所有路径应用配置
        return new CorsFilter(source);
    }


//    拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册自定义拦截器LoginInterceptor
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")  // 拦截所有的请求
                .excludePathPatterns(
//                        小程序的路径
                        "/user/login","/user/reg","/user/forget","/user/id/**",
//                        管理后端的路径
                        "/admin/login","/admin/register","/admin/token","/admin/avatarUpload","/admin/getPreView",
//                        swagger的请求拦截忽略
                    "/doc.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs/**", "/ws/**","/swagger-ui.html");
    }
}

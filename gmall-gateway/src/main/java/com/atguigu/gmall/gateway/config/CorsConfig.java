package com.atguigu.gmall.gateway.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config=new CorsConfiguration();
        //表示允许跨域的域名，* 代表所有域名 不推荐 因为不安全 且无法携带cookie
        config.addAllowedOrigin("http://manager.gmall.com");
        config.addAllowedOrigin("http://api.gmall.com");
        config.addAllowedOrigin("http://0.0.0.0:*");
        //表示允许所有的请求方法跨域
        config.addAllowedMethod("*");
        //表示允许携带任意头信息/
        config.addAllowedHeader("*");
        //表示允许携带cookie 如果允许携带cookie那么 origin不能写成*
        config.setAllowCredentials(true);
        //cors注册类 拦截所有请求 进行cors验证
        configSource.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(configSource);

    }
}

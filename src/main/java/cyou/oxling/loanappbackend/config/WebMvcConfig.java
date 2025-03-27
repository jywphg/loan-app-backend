package cyou.oxling.loanappbackend.config;

import cyou.oxling.loanappbackend.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册JWT拦截器，拦截所有需要鉴权的接口
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")  // 拦截所有接口
                .excludePathPatterns(    // 排除不需要鉴权的接口
                        "/api/user/login",
                        "/api/user/register",
                        "/api/user/third-party-login"
                );
    }
} 
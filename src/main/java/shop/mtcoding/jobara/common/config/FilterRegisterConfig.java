package shop.mtcoding.jobara.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.mtcoding.jobara.common.config.filter.JwtVerifyFilter;

@Configuration
public class FilterRegisterConfig {
    @Bean
    public FilterRegistrationBean<?> jwtVerifyFilterAdd() {
        FilterRegistrationBean<JwtVerifyFilter> registraion = new FilterRegistrationBean<>();
        registraion.setFilter(new JwtVerifyFilter());
        registraion.addUrlPatterns("/employee/*");
        registraion.addUrlPatterns("/company/*");
        registraion.addUrlPatterns("/loves/*");
        registraion.setOrder(1);
        return registraion;
    }
}

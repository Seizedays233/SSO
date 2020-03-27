package com.seizedays.config;

import com.seizedays.listener.SessionListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ListenerConfig implements WebMvcConfigurer {

    @Bean
    public ServletListenerRegistrationBean bean(){
        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
        bean.setListener(new SessionListener());
        return bean;
    }

}

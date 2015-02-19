package be.kdg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Created by vman on 6/02/2015.
 */
//@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "be.kdg.controller")
public class WebConfig extends WebMvcConfigurerAdapter { //bootstrap the web context, for servlets
    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/"); //tells us the location of the dynamic resources (JSP files) by using
                                              //getInternalResourceViewResolver
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){ //enable forwarding to the 'default' servlet for unhandled requests
        configurer.enable();
    }

    @Bean
    public WebContentInterceptor webContentInterceptor(){
        WebContentInterceptor interceptor = new WebContentInterceptor();
        interceptor.setCacheSeconds(0); //no cache
        interceptor.setUseExpiresHeader(true);
        interceptor.setUseCacheControlHeader(true);
        interceptor.setUseCacheControlNoStore(true);

        return interceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){ //which static resources can be served?
        registry.addResourceHandler("/libs/**").addResourceLocations("/libs/");
        registry.addResourceHandler("/app/**").addResourceLocations("/app/");
        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){ //add an interceptor for no cache
        registry.addInterceptor(webContentInterceptor());
    }

}

package be.kdg.config;

import org.springframework.context.ApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletRegistration;
import java.nio.charset.StandardCharsets;

/**
 * Created by vman on 6/02/2015.
 */

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer { //load everything, Spring's replacement of web.xml

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration){ //Tomcat custom configuration, configure your servlets here with params etc.
        registration.setInitParameter("dispatchOptionsRequest","true"); //if you want to see/intercept the HTTP OPTIONS
        registration.setAsyncSupported(true); //so that connections do not have to be closed directly
    }

    @Override
    protected Class<?>[] getRootConfigClasses() { //bean configuration
        return new Class< ?>[]{AppConfig.class, WebSocketConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() { //bean configuration
        return new Class< ?>[] {WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() { //servlet configuration
        return new String[] { "/" }; //map application to root, specify the servlet mapping(s) for the DispatcherServlet, e.g. '/', '/app', etc.
    }

    @Override
    protected Filter[] getServletFilters(){ //servlet filter configuration
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(StandardCharsets.UTF_8.name());
        return new Filter[]{ characterEncodingFilter }; //make sure all content is UTF-8
    }
}

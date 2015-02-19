package be.kdg.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

/**
 * Created by vman on 6/02/2015.
 */
//@Configuration
//which packages to scan, but excludes all configuration and controller classes
@ComponentScan(basePackages = "be.kdg", excludeFilters = {
        @ComponentScan.Filter(value=Controller.class, type=FilterType.ANNOTATION),
        @ComponentScan.Filter(value=Configuration.class, type=FilterType.ANNOTATION)
        })
public class AppConfig { //beans
    //Since we will only need a controller, this class will do nothing special,
    //but if you have special services, then they will become spring beans if annoted correctly.
}

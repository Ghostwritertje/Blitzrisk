package integration;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by user jorandeboever
 * Date:14/02/15.
 */
public class MyServerConfiguration {



    private static final String CHROMEDRIVERLOCATION = System.getProperty("user.dir") + "/src/test/resources/chromedriver.exe"; //location for chromeDriver

    public static String getChromedriverlocation() {
        if (System.getProperty("os.name").equals("Mac OS X"))
            return System.getProperty("user.dir") + "/src/test/resources/chromedriver";

        return CHROMEDRIVERLOCATION;
    }

    public static String getURL() {
        /*
        * port.xml changes automatically when deploying to Jetty to 9090
        * */
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String port = "8080";
        try {
            port = IOUtils.toString(classloader.getResourceAsStream("port.txt"), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "http://localhost:" + port + "/BlitzRisk/";
    }




}

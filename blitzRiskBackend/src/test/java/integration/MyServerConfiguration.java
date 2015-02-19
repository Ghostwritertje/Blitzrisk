package integration;

/**
 * Created by user jorandeboever
 * Date:14/02/15.
 */
public class MyServerConfiguration {
    public static final String URL = "http://localhost:8080/BlitzRisk/";
    private static final String CHROMEDRIVERLOCATION = System.getProperty("user.dir") +"/src/test/resources/chromedriver.exe"; //location for chromeDriver

    public static String getChromedriverlocation() {
        if(System.getProperty("os.name").equals("Mac OS X")) return System.getProperty("user.dir") +"/src/test/resources/chromedriver";
        return CHROMEDRIVERLOCATION;
    }


}

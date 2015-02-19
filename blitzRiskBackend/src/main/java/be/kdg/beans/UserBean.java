package be.kdg.beans;

import org.springframework.stereotype.Component;


/**
 * Created by user jorandeboever
 * Date:13/02/15.
 */
@Component
public class UserBean {

    private String name;
    private String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}

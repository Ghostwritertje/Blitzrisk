package be.kdg.beans;

import be.kdg.model.User;
import org.springframework.stereotype.Component;


/**
 * Created by user jorandeboever
 * Date:13/02/15.
 */
@Component
public class UserBean {

    private String name;
    private String password;

    private String email;

    public UserBean() {
    }

    public UserBean(User user) {
        this.name = user.getName();
        this.password = user.getPassword();
        this.email = user.getEmail();
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

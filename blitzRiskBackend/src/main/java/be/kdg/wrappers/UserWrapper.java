package be.kdg.wrappers;

import be.kdg.model.User;
import org.springframework.stereotype.Component;

/**
 * Wraps the user model in a simple wrapper that is used in REST-calls
 */
@Component
public class UserWrapper {

    private String name;
    private String password;
    private String email;

    public UserWrapper() {
    }

    public UserWrapper(User user) {
        this.name = user.getName();
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

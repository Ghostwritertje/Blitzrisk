package be.kdg.security;
/*

import be.kdg.model.User;
import be.kdg.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

*/
/**
 * Created by Marlies on 6/01/2015.
 *//*

public class LoginService implements AuthenticationProvider {
    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userService.checkLogin(authentication.getName(), authentication.getCredentials().toString());
        if (user == null) return null;

        return new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), null);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
*/

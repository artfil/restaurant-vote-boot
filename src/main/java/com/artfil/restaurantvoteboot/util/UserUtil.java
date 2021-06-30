package com.artfil.restaurantvoteboot.util;

import com.artfil.restaurantvoteboot.model.Role;
import com.artfil.restaurantvoteboot.model.User;
import com.artfil.restaurantvoteboot.to.UserTo;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import static com.artfil.restaurantvoteboot.config.WebSecurityConfig.PASSWORD_ENCODER;

@UtilityClass
public class UserUtil {
    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static User prepareToSave(User user) {
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? PASSWORD_ENCODER.encode(password) : password);
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}

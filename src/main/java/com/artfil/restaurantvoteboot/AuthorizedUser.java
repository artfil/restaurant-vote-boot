package com.artfil.restaurantvoteboot;

import com.artfil.restaurantvoteboot.model.User;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString(of = "user")
public class AuthorizedUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public AuthorizedUser(@NonNull User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public int id() {
        return user.id();
    }
}

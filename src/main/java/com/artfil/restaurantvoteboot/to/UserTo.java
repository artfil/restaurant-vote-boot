package com.artfil.restaurantvoteboot.to;

import com.artfil.restaurantvoteboot.HasIdAndEmail;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserTo extends BaseTo implements HasIdAndEmail, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 2, max = 100) String name;

    @Email
    @NotBlank
    @Size(max = 100) String email;

    @NotBlank
    @Size(min = 5, max = 32) String password;

    public UserTo(Integer id, String name, String email, String password) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
    }
}

package com.artfil.restaurantvoteboot.web.user;

import com.artfil.restaurantvoteboot.HasIdAndEmail;
import com.artfil.restaurantvoteboot.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import static com.artfil.restaurantvoteboot.web.GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL;

@Component
@AllArgsConstructor
public class UniqueMailValidator implements org.springframework.validation.Validator {

    private final UserRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasIdAndEmail user = ((HasIdAndEmail) target);
        if (StringUtils.hasText(user.getEmail()) && repository.getByEmail(user.getEmail().toLowerCase())
                .filter(u -> !u.getId().equals(user.getId())).isPresent()) {
            errors.rejectValue("email", EXCEPTION_DUPLICATE_EMAIL);
        }
    }
}

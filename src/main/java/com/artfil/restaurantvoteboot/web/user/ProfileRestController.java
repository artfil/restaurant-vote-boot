package com.artfil.restaurantvoteboot.web.user;

import com.artfil.restaurantvoteboot.AuthorizedUser;
import com.artfil.restaurantvoteboot.model.User;
import com.artfil.restaurantvoteboot.to.UserTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;

import static com.artfil.restaurantvoteboot.util.UrlUtil.PROFILE_URL;

@RestController
@RequestMapping(value = PROFILE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ProfileRestController extends AbstractUserController {
    @GetMapping
    public HttpEntity<User> get(@AuthenticationPrincipal @ApiIgnore AuthorizedUser authUser) {
        return super.get(authUser.id());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal @ApiIgnore AuthorizedUser authUser) {
        super.delete(authUser.id());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("register {}", userTo);
        User created = super.create(userTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(PROFILE_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@RequestBody UserTo userTo, @AuthenticationPrincipal @ApiIgnore AuthorizedUser authUser) throws BindException {
        validateBeforeUpdate(userTo, authUser.id());
        super.update(userTo, authUser.id());
    }
}

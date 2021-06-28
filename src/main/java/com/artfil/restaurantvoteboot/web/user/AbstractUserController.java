package com.artfil.restaurantvoteboot.web.user;

import com.artfil.restaurantvoteboot.HasId;
import com.artfil.restaurantvoteboot.model.User;
import com.artfil.restaurantvoteboot.repository.UserRepository;
import com.artfil.restaurantvoteboot.to.UserTo;
import com.artfil.restaurantvoteboot.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import static com.artfil.restaurantvoteboot.util.ValidationUtil.assureIdConsistent;
import static com.artfil.restaurantvoteboot.util.ValidationUtil.checkNew;
import static com.artfil.restaurantvoteboot.util.ValidationUtil.checkSingleModification;

@Slf4j
public abstract class AbstractUserController {

    @Autowired
    protected UserRepository repository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @Autowired
    private LocalValidatorFactoryBean defaultValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public ResponseEntity<User> get(int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    public User create(UserTo userTo) {
        log.info("create from to {}", userTo);
        return create(UserUtil.createNewFromTo(userTo));
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        return prepareAndSave(user);
    }

    @Transactional
    public void update(UserTo userTo, int id) {
        log.info("update {} with id={}", userTo, id);
        User user = repository.getExisted(userTo.id());
        Assert.notNull(user, "user must not be null");
        prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }

    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        prepareAndSave(user);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        checkSingleModification(repository.delete(id), "User id=" + id + " not found");
    }

    protected User prepareAndSave(User user) {
        return repository.save(UserUtil.prepareToSave(user));
    }

    protected void validateBeforeUpdate(HasId user, int id) throws BindException {
        assureIdConsistent(user, id);
        DataBinder binder = new DataBinder(user);
        binder.addValidators(emailValidator, defaultValidator);
        binder.validate();
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }
}
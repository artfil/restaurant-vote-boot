package com.artfil.restaurantvoteboot.web;

import com.artfil.restaurantvoteboot.model.Restaurant;
import com.artfil.restaurantvoteboot.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.artfil.restaurantvoteboot.util.UrlUtil.RESTAURANT_URL;
import static com.artfil.restaurantvoteboot.util.ValidationUtil.assureIdConsistent;
import static com.artfil.restaurantvoteboot.util.ValidationUtil.checkNew;
import static com.artfil.restaurantvoteboot.util.ValidationUtil.checkNotFoundWithId;
import static com.artfil.restaurantvoteboot.util.ValidationUtil.checkSingleModification;

@RestController
@RequestMapping(value = RESTAURANT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "dishes")
public class RestaurantRestController {
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        return ResponseEntity.of(restaurantRepository.findById(id));
    }

    @GetMapping()
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @CacheEvict(allEntries = true)
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        checkSingleModification(restaurantRepository.delete(id), "Restaurant id=" + id + " not found");
    }

    @CacheEvict(allEntries = true)
    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {}", restaurant);
        assureIdConsistent(restaurant, id);
        checkNotFoundWithId(restaurantRepository.findById(id), "Restaurant id=" + id);
        restaurantRepository.save(restaurant);
    }

    @CacheEvict(allEntries = true)
    @Secured("ROLE_ADMIN")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(RESTAURANT_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}

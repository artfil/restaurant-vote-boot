package com.artfil.restaurantvoteboot.web;

import com.artfil.restaurantvoteboot.model.Dish;
import com.artfil.restaurantvoteboot.repository.DishRepository;
import com.artfil.restaurantvoteboot.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.artfil.restaurantvoteboot.util.UrlUtil.DISH_URL;
import static com.artfil.restaurantvoteboot.util.ValidationUtil.assureIdConsistent;
import static com.artfil.restaurantvoteboot.util.ValidationUtil.checkNew;
import static com.artfil.restaurantvoteboot.util.ValidationUtil.checkNotFoundWithId;
import static com.artfil.restaurantvoteboot.util.ValidationUtil.checkSingleModification;

@RestController
@RequestMapping(value = DISH_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class DishRestController {
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping()
    public List<Dish> getAllToday(@PathVariable int restId) {
        log.info("getAllToday for restaurants {}", restId);
        return dishRepository.getAllByDate(restId, LocalDate.now());
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Dish> get(@PathVariable int restId, @PathVariable int id) {
        log.info("get dish {} for restaurant {}", id, restId);
        return ResponseEntity.of(dishRepository.get(id, restId));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/by-date")
    public List<Dish> getAllByDate(@PathVariable int restId, @RequestParam() LocalDate date) {
        log.info("get menu by date {} for restaurant {}", date, restId);
        return dishRepository.getAllByDate(restId, date);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restId, @PathVariable int id) {
        log.info("delete {} for restaurant{}", id, restId);
        checkSingleModification(dishRepository.delete(id, restId), "Dish id=" + id + ", Rest id=" + restId + " missed");
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int restId, @PathVariable int id) {
        log.info("update {} by id {} for restaurant {}", dish, id, restId);
        assureIdConsistent(dish, id);
        checkNotFoundWithId(dishRepository.get(id, restId), "Dish id=" + id + " doesn't belong to restaurant id=" + restId);
        dish.setRestaurant(restaurantRepository.getById(restId));
        dishRepository.save(dish);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int restId) {
        log.info("create {} for restaurant {}", dish, restId);
        checkNew(dish);
        checkNotFoundWithId(restaurantRepository.findById(restId), "Restaurant id=" + restId);
        dish.setRestaurant(restaurantRepository.getById(restId));
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DISH_URL + "/{id}")
                .buildAndExpand(restId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}

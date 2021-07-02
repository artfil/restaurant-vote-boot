package com.artfil.restaurantvoteboot.web.dish;

import com.artfil.restaurantvoteboot.model.Dish;
import com.artfil.restaurantvoteboot.repository.DishRepository;
import com.artfil.restaurantvoteboot.util.JsonUtil;
import com.artfil.restaurantvoteboot.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.artfil.restaurantvoteboot.TestUtil.readFromJson;
import static com.artfil.restaurantvoteboot.TestUtil.userHttpBasic;
import static com.artfil.restaurantvoteboot.testdata.DishTestDataUtils.*;
import static com.artfil.restaurantvoteboot.testdata.RestaurantTestDataUtils.RESTAURANT_1_ID;
import static com.artfil.restaurantvoteboot.testdata.RestaurantTestDataUtils.RESTAURANT_3_ID;
import static com.artfil.restaurantvoteboot.testdata.UserTestDataUtils.admin;
import static com.artfil.restaurantvoteboot.util.UrlUtil.DISH_URL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DishRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = DISH_URL + '/';

    @Autowired
    private DishRepository dishRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_1_ID, RESTAURANT_1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish1));
    }

    @Test
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-date?date=" + dish2.getDate(), RESTAURANT_1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish2, dish1));
    }

    @Test
    void getAllToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL, RESTAURANT_1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish2, dish1));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_1_ID, RESTAURANT_1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND, RESTAURANT_1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH_1_ID, RESTAURANT_1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        assertFalse(dishRepository.get(DISH_1_ID, RESTAURANT_1_ID).isPresent());
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND, RESTAURANT_1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID, RESTAURANT_1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(dishRepository.get(DISH_1_ID, RESTAURANT_1_ID).get(), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        Dish newDish = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT_3_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish))
                .with(userHttpBasic(admin)));

        Dish created = readFromJson(action, Dish.class);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishRepository.get(newId, RESTAURANT_3_ID).get(), newDish);
    }

    @Test
    void createInvalid() throws Exception {
        Dish invalid = new Dish(null, null, LocalDate.now(), -10);
        perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        Dish invalid = new Dish(DISH_1_ID, null, LocalDate.now(), -10, "I");
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID, RESTAURANT_1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() {
        Dish invalid = new Dish(null, dish2.getName(), LocalDate.now(), 100);
        assertThrows(Exception.class, () ->
                perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid))
                        .with(userHttpBasic(admin)))
                        .andDo(print())
                        .andExpect(status().isUnprocessableEntity()));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() {
        Dish invalid = new Dish(DISH_1_ID, dish2.getName(), LocalDate.now(), 100);
        assertThrows(Exception.class, () ->
                perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID, RESTAURANT_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid))
                        .with(userHttpBasic(admin)))
                        .andDo(print())
                        .andExpect(status().isUnprocessableEntity()));
    }
}
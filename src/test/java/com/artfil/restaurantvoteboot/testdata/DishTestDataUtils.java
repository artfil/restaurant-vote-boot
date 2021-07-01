package com.artfil.restaurantvoteboot.testdata;

import com.artfil.restaurantvoteboot.TestMatcher;
import com.artfil.restaurantvoteboot.model.Dish;

import java.time.Month;

import static com.artfil.restaurantvoteboot.model.AbstractBaseEntity.START_SEQ;
import static com.artfil.restaurantvoteboot.testdata.RestaurantTestDataUtils.restaurant1;
import static com.artfil.restaurantvoteboot.testdata.RestaurantTestDataUtils.restaurant2;
import static com.artfil.restaurantvoteboot.testdata.RestaurantTestDataUtils.restaurant3;
import static java.time.LocalDate.now;
import static java.time.LocalDate.of;

public class DishTestDataUtils {
    public static final TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Dish.class, "restaurant");

    public static final int NOT_FOUND = 100;
    public static final int DISH_1_ID = START_SEQ + 5;
    public static final int DISH_2_ID = START_SEQ + 6;
    public static final int DISH_3_ID = START_SEQ + 7;
    public static final int DISH_4_ID = START_SEQ + 8;
    public static final int DISH_5_ID = START_SEQ + 9;
    public static final int DISH_6_ID = START_SEQ + 10;
    public static final int DISH_7_ID = START_SEQ + 11;

    public static final Dish dish1 = new Dish(DISH_1_ID, "Fresh Korea", now(), 500, "Tomatoes, cheese, salad");
    public static final Dish dish2 = new Dish(DISH_2_ID, "Asian soup", now(), 800, "seafood, potatoes");
    public static final Dish dish3 = new Dish(DISH_3_ID, "Bibimbap", of(2021, Month.MARCH, 8), 1000, "rice, vegetables, beef");
    public static final Dish dish4 = new Dish(DISH_4_ID, "Belgian waffles", of(2021, Month.MARCH, 8), 325, "waffles, chocolate sauce, strawberry");
    public static final Dish dish5 = new Dish(DISH_5_ID, dish4.getName(), now(), dish4.getPrice(), dish4.getDescription());
    public static final Dish dish6 = new Dish(DISH_6_ID, "Marbled beef steak", now(), 1254, "beef, BBQ sauce");
    public static final Dish dish7 = new Dish(DISH_7_ID, "Bavarian sausage", of(2021, Month.MARCH, 8), 999);

    static {
        dish1.setRestaurant(restaurant1);
        dish2.setRestaurant(restaurant1);
        dish3.setRestaurant(restaurant1);
        dish4.setRestaurant(restaurant2);
        dish5.setRestaurant(restaurant2);
        dish6.setRestaurant(restaurant2);
        dish7.setRestaurant(restaurant3);
    }

    public static Dish getNew() {
        Dish dish = new Dish(null, "new dish", now(), 555, "new description");
        dish.setRestaurant(restaurant3);
        return dish;
    }

    public static Dish getUpdated() {
        Dish dish = new Dish(DISH_1_ID, "update dish", dish1.getDate(), 111, dish1.getDescription());
        dish.setRestaurant(dish1.getRestaurant());
        return dish;
    }
}

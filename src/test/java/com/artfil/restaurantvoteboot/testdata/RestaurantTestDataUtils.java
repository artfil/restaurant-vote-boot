package com.artfil.restaurantvoteboot.testdata;

import com.artfil.restaurantvoteboot.TestMatcher;
import com.artfil.restaurantvoteboot.model.Restaurant;

import static com.artfil.restaurantvoteboot.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestDataUtils {
    public static final TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingEqualsComparator(Restaurant.class);

    public static final int NOT_FOUND = 101;
    public static final int RESTAURANT_1_ID = START_SEQ + 2;
    public static final int RESTAURANT_2_ID = START_SEQ + 3;
    public static final int RESTAURANT_3_ID = START_SEQ + 4;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT_1_ID, "Koreana");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT_2_ID, "Kriek");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT_3_ID, "Jager");

    public static Restaurant getNew() {
        return new Restaurant(null, "new restaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_2_ID, "updated restaurant");
    }
}

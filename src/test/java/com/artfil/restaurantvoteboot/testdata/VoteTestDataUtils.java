package com.artfil.restaurantvoteboot.testdata;

import com.artfil.restaurantvoteboot.TestMatcher;
import com.artfil.restaurantvoteboot.model.Vote;
import com.artfil.restaurantvoteboot.to.VoteTo;

import java.time.Month;

import static com.artfil.restaurantvoteboot.testdata.RestaurantTestDataUtils.restaurant1;
import static com.artfil.restaurantvoteboot.testdata.RestaurantTestDataUtils.restaurant2;
import static com.artfil.restaurantvoteboot.testdata.RestaurantTestDataUtils.restaurant3;
import static com.artfil.restaurantvoteboot.testdata.UserTestDataUtils.admin;
import static com.artfil.restaurantvoteboot.testdata.UserTestDataUtils.user;
import static java.time.LocalDate.now;
import static java.time.LocalDate.of;

public class VoteTestDataUtils {
    public static final TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant");
    public static final TestMatcher<VoteTo> VOTE_TO_MATCHER = TestMatcher.usingEqualsComparator(VoteTo.class);

    public static final int NOT_FOUND = 102;
    public static final int VOTE_1_ID = 1;
    public static final int VOTE_2_ID = 2;
    public static final int VOTE_3_ID = 3;

    public static final Vote vote1 = new Vote(VOTE_1_ID, now());
    public static final Vote vote2 = new Vote(VOTE_2_ID, of(2021, Month.MARCH, 8));
    public static final Vote vote3 = new Vote(VOTE_3_ID, of(2021, Month.MARCH, 8));

    static {
        vote1.setUser(admin);
        vote1.setRestaurant(restaurant2);
        vote2.setUser(user);
        vote2.setRestaurant(restaurant1);
        vote3.setUser(admin);
        vote3.setRestaurant(restaurant3);
    }

    public static Vote getNew() {
        Vote vote = new Vote(null, now());
        vote.setUser(user);
        vote.setRestaurant(restaurant3);
        return vote;
    }

    public static Vote getUpdated() {
        Vote vote = new Vote(VOTE_1_ID, now());
        vote.setUser(vote1.getUser());
        vote.setRestaurant(restaurant3);
        return vote;
    }
}

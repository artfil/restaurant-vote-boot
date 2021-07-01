package com.artfil.restaurantvoteboot.web.vote;

import com.artfil.restaurantvoteboot.model.Vote;
import com.artfil.restaurantvoteboot.repository.VoteRepository;
import com.artfil.restaurantvoteboot.util.VoteUtil;
import com.artfil.restaurantvoteboot.web.AbstractControllerTest;
import com.artfil.restaurantvoteboot.web.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.artfil.restaurantvoteboot.TestUtil.readFromJson;
import static com.artfil.restaurantvoteboot.TestUtil.userHttpBasic;
import static com.artfil.restaurantvoteboot.testdata.RestaurantTestDataUtils.RESTAURANT_1_ID;
import static com.artfil.restaurantvoteboot.testdata.RestaurantTestDataUtils.RESTAURANT_2_ID;
import static com.artfil.restaurantvoteboot.testdata.RestaurantTestDataUtils.RESTAURANT_3_ID;
import static com.artfil.restaurantvoteboot.testdata.UserTestDataUtils.ADMIN_ID;
import static com.artfil.restaurantvoteboot.testdata.UserTestDataUtils.USER_ID;
import static com.artfil.restaurantvoteboot.testdata.VoteTestDataUtils.*;
import static java.time.LocalTime.now;
import static java.time.LocalTime.of;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.artfil.restaurantvoteboot.testdata.UserTestDataUtils.user;
import static com.artfil.restaurantvoteboot.testdata.UserTestDataUtils.admin;
import static com.artfil.restaurantvoteboot.util.UrlUtil.VOTE_URL;

class ProfileVoteRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VOTE_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void getToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(VoteUtil.createTo(vote1)));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restId", Integer.toString(RESTAURANT_2_ID)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update() throws Exception { //if local time is before 11am
        Vote updated = getUpdated();
        if (now().isAfter(of(11, 0))) {
            perform(MockMvcRequestBuilders.put(REST_URL + VOTE_1_ID)
                    .param("restId", Integer.toString(RESTAURANT_3_ID))
                    .with(userHttpBasic(admin)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_UPDATE_VOTE)));
        } else {
            perform(MockMvcRequestBuilders.put(REST_URL + VOTE_1_ID)
                    .param("restId", Integer.toString(RESTAURANT_3_ID))
                    .with(userHttpBasic(admin)))
                    .andExpect(status().isOk());

            VOTE_MATCHER.assertMatch(voteRepository.getById(VOTE_1_ID, ADMIN_ID).get(), updated);
        }
    }

    @Test
    void createWithLocation() throws Exception {
        Vote newVote = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restId", Integer.toString(RESTAURANT_3_ID))
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isCreated());

        Vote created = readFromJson(action, Vote.class);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteRepository.getById(newId, USER_ID).get(), newVote);
    }

    @Test
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restId", Integer.toString(NOT_FOUND))
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + VOTE_1_ID)
                .param("restId", Integer.toString(NOT_FOUND))
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        assertThrows(Exception.class, () ->
                perform(MockMvcRequestBuilders.post(REST_URL)
                        .param("restId", Integer.toString(RESTAURANT_1_ID))
                        .with(userHttpBasic(admin)))
                        .andDo(print())
                        .andExpect(status().isUnprocessableEntity()));
    }
}
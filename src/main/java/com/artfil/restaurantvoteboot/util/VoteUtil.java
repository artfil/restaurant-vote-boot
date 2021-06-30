package com.artfil.restaurantvoteboot.util;

import com.artfil.restaurantvoteboot.model.Restaurant;
import com.artfil.restaurantvoteboot.model.Vote;
import com.artfil.restaurantvoteboot.to.VoteTo;
import com.artfil.restaurantvoteboot.util.exception.UpdateVoteException;
import lombok.experimental.UtilityClass;

import java.time.LocalTime;

import static com.artfil.restaurantvoteboot.web.GlobalExceptionHandler.EXCEPTION_UPDATE_VOTE;

@UtilityClass
public class VoteUtil {
    private static final LocalTime UPDATE_DEADLINE = LocalTime.of(11, 0);

    public static void reVotingPermission() {
        if (LocalTime.now().isAfter(UPDATE_DEADLINE))
            throw new UpdateVoteException(EXCEPTION_UPDATE_VOTE);
    }

    public static VoteTo createTo(Vote vote) {
        Restaurant rest = vote.getRestaurant();
        return new VoteTo(vote.getId(), vote.getVoteDate(), rest.id(), rest.getName());
    }
}

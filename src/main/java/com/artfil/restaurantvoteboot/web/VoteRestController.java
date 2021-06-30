package com.artfil.restaurantvoteboot.web;

import com.artfil.restaurantvoteboot.AuthorizedUser;
import com.artfil.restaurantvoteboot.model.Vote;
import com.artfil.restaurantvoteboot.repository.RestaurantRepository;
import com.artfil.restaurantvoteboot.repository.UserRepository;
import com.artfil.restaurantvoteboot.repository.VoteRepository;
import com.artfil.restaurantvoteboot.to.VoteTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

import static com.artfil.restaurantvoteboot.util.UrlUtil.VOTE_URL;
import static com.artfil.restaurantvoteboot.util.ValidationUtil.assureIdConsistent;
import static com.artfil.restaurantvoteboot.util.ValidationUtil.checkNotFoundWithId;
import static com.artfil.restaurantvoteboot.util.VoteUtil.createTo;
import static com.artfil.restaurantvoteboot.util.VoteUtil.reVotingPermission;

@RestController
@RequestMapping(value = VOTE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class VoteRestController {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping()
    public ResponseEntity<VoteTo> get(@AuthenticationPrincipal @ApiIgnore AuthorizedUser authUser) {
        log.info("get for User{}", authUser.id());
        return ResponseEntity.of(Optional.of(createTo(voteRepository.getByDate(LocalDate.now(), authUser.id()).get())));
    }

    @PutMapping(value = "/{id}")
    public void update(@AuthenticationPrincipal @ApiIgnore AuthorizedUser authUser, @PathVariable int id, @RequestParam int restId) {
        reVotingPermission();
        int userId = authUser.id();
        log.info("update vote for user {} by id restaurant {}", userId, restId);
        Vote vote = new Vote(id, LocalDate.now());
        assureIdConsistent(vote, id);
        checkNotFoundWithId(voteRepository.getById(id, userId), "Vote id=" + id + " doesn't belong to user id=" + userId);
        checkNotFoundWithId(restaurantRepository.findById(restId), "Restaurant id=" + restId);
        vote.setUser(userRepository.getById(userId));
        vote.setRestaurant(restaurantRepository.getById(restId));
        voteRepository.save(vote);
    }

    @PostMapping
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal @ApiIgnore AuthorizedUser authUser, @RequestParam int restId) {
        int userId = authUser.id();
        log.info("create vote for user {} by id restaurant {}", userId, restId);
        Vote created = new Vote(null, LocalDate.now());
        checkNotFoundWithId(restaurantRepository.findById(restId), "Restaurant id=" + restId);
        created.setUser(userRepository.getById(userId));
        created.setRestaurant(restaurantRepository.getById(restId));
        voteRepository.save(created);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(VOTE_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}

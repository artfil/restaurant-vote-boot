package com.artfil.restaurantvoteboot.repository;

import com.artfil.restaurantvoteboot.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant JOIN FETCH v.user WHERE v.id=:id AND v.user.id=:userId")
    Optional<Vote> getById(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.voteDate=:date AND v.user.id=:userId")
    Optional<Vote> getByDate(@Param("date") LocalDate date, @Param("userId") int userId);
}

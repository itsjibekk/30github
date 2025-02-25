package com.example.demoo.repo;

import com.example.demoo.models.PlayList;
import com.example.demoo.models.Track;
import com.example.demoo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayListRepo extends JpaRepository<PlayList, Long> {
    boolean existsByTitle(String title);

    @Query(value = "SELECT * FROM playlist WHERE userid = :userId", nativeQuery = true)
    List<PlayList> findByUserId(@Param("userId") Long userId);

    boolean existsByTitleAndUser(String title, User currentUser);
}

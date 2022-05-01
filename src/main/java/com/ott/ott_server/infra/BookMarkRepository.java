package com.ott.ott_server.infra;

import com.ott.ott_server.domain.BookMark;
import com.ott.ott_server.domain.Review;
import com.ott.ott_server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

    boolean existsByReviewAndUser(Review review, User user);

    List<BookMark> findByUser(User user);

}

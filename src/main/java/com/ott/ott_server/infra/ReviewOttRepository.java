package com.ott.ott_server.infra;

import com.ott.ott_server.domain.ReviewOtt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewOttRepository extends JpaRepository<ReviewOtt, Long> {
}

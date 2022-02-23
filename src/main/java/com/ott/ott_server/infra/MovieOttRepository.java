package com.ott.ott_server.infra;

import com.ott.ott_server.domain.MovieOtt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieOttRepository extends JpaRepository<MovieOtt, Long> {

}

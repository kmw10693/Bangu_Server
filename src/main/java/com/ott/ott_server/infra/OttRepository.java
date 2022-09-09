package com.ott.ott_server.infra;

import com.ott.ott_server.domain.Ott;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OttRepository extends JpaRepository<Ott, Long> {

    Optional<Ott> findByName(String name);

    Page<Ott> findAll(Pageable pageable);

}

package com.ott.ott_server.application;

import com.ott.ott_server.domain.Genre;
import com.ott.ott_server.infra.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }
}

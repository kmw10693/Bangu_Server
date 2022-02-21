package com.ott.ott_server.application;

import com.ott.ott_server.domain.Ott;
import com.ott.ott_server.infra.OttRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class OttService {

    private final OttRepository ottRepository;

    public Ott save(Ott ott) {
        return ottRepository.save(ott);
    }
}

package com.ott.ott_server.application;

import com.ott.ott_server.domain.Ott;
import com.ott.ott_server.dto.ott.request.OttRequestData;
import com.ott.ott_server.errors.OttNameDuplicationException;
import com.ott.ott_server.infra.OttRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class OttService {

    private final OttRepository ottRepository;

    @Transactional
    public Ott create(OttRequestData ottRequestData) {
        if (ottRepository.findByName(ottRequestData.getName()).isPresent())
            throw new OttNameDuplicationException(ottRequestData.getName());

        return ottRepository.save(Ott.builder().name(ottRequestData.getName()).build());
    }

    public Page<Ott> getAllOtts(Pageable pageable) {
        return ottRepository.findAll(pageable);
    }

}

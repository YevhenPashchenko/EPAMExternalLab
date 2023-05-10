package com.epam.esm.dbfiller.service.impl;

import com.epam.esm.dbfiller.repository.giftcertificates.TagRepository;
import com.epam.esm.dbfiller.entity.giftcertificates.Tag;
import com.epam.esm.dbfiller.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public void createTags(List<Tag> tags) {
        tagRepository.saveAll(tags);
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
            .orElseThrow(RuntimeException::new);
    }
}

package com.twinzy.service;

import org.springframework.stereotype.Service;

import com.twinzy.dto.SearchResponseDto;
import com.twinzy.matching.MatchingEngine;

@Service
public class SimilarityService {

    private final MatchingEngine matchingEngine;

    public SimilarityService(MatchingEngine matchingEngine) {
        this.matchingEngine = matchingEngine;
    }

    public SearchResponseDto search(byte[] imageBytes) {
        return matchingEngine.search(imageBytes);
    }
}

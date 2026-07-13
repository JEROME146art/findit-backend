package com.findit.service;

import com.findit.matcher.MatchEngine;
import com.findit.model.dto.MatchResponse;
import com.findit.model.entity.FoundItem;
import com.findit.model.entity.LostItem;
import com.findit.repository.FoundItemRepository;
import com.findit.repository.LostItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that uses the MatchEngine to find matches for lost items.
 */
@Service
public class MatchService {

    private final LostItemRepository lostRepo;
    private final FoundItemRepository foundRepo;
    private final MatchEngine matchEngine;

    public MatchService(LostItemRepository lostRepo,
                        FoundItemRepository foundRepo,
                        MatchEngine matchEngine) {
        this.lostRepo = lostRepo;
        this.foundRepo = foundRepo;
        this.matchEngine = matchEngine;
    }

    /**
     * Find all found items that match the given lost item (above threshold).
     */
    public List<MatchResponse> findMatchesForLostItem(Long lostId, double threshold) {
        // 1. Get the lost item
        LostItem lost = lostRepo.findById(lostId)
                .orElseThrow(() -> new RuntimeException("Lost item not found: " + lostId));

        // 2. Get all available found items
        List<FoundItem> founds = foundRepo.findByClaimedFalseAndResolvedFalse();

        // 3. Use the engine to score and rank
        List<MatchEngine.ScoredMatch> matches = matchEngine.findMatches(lost, founds, threshold);

        // 4. Convert to DTOs for the response
        return matches.stream()
                .map(m -> new MatchResponse(
                        lost.getId(),
                        m.foundItem().getId(),
                        m.foundItem().getTitle(),
                        m.foundItem().getDescription(),
                        m.foundItem().getLocation() != null ? m.foundItem().getLocation().toString() : "Unknown",
                        m.score()
                ))
                .collect(Collectors.toList());
    }
}
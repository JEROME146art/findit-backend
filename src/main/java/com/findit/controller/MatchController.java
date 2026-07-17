package com.findit.controller;

import com.findit.model.dto.MatchResponse;
import com.findit.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for the matching feature.
 * Base URL: /api/matches
 */
@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    /**
     * Get matches for a lost item.
     * GET /api/matches/lost/{lostId}?threshold=40
     */
    @GetMapping("/lost/{lostId}")
    public ResponseEntity<List<MatchResponse>> getMatchesForLost(
            @PathVariable Long lostId,
            @RequestParam(defaultValue = "40") double threshold) {
        List<MatchResponse> matches = matchService.findMatchesForLostItem(lostId, threshold);
        return ResponseEntity.ok(matches);
    }
}
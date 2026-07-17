package com.findit.voting.controller;

import com.findit.voting.entity.*;
import com.findit.voting.service.VotingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/voting")
public class VotingController {

    private final VotingService votingService;

    public VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    @GetMapping("/elections")
    public ResponseEntity<List<Election>> getAllElections() {
        return ResponseEntity.ok(votingService.getAllElections());
    }

    @GetMapping("/elections/{code}")
    public ResponseEntity<Election> getElectionByCode(@PathVariable String code) {
        return votingService.getElectionByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ledger")
    public ResponseEntity<List<BallotRecord>> getAuditLedger() {
        return ResponseEntity.ok(votingService.getAuditLedger());
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<?> verifyToken(@PathVariable String token) {
        Optional<BallotRecord> record = votingService.verifyBallotToken(token);
        if (record.isPresent()) {
            return ResponseEntity.ok(record.get());
        }
        return ResponseEntity.status(404).body(Map.of("message", "Receipt Token not found on public ledger."));
    }

    @PostMapping("/vote")
    public ResponseEntity<BallotRecord> castVote(@RequestBody Map<String, String> payload) {
        String electionCode = payload.get("electionCode");
        String category = payload.get("category");
        String choiceSummary = payload.get("choiceSummary");
        String token = payload.get("token");
        BallotRecord record = votingService.castBallot(electionCode, category, choiceSummary, token);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/townhall")
    public ResponseEntity<List<TownhallQuestion>> getTownhallQuestions() {
        return ResponseEntity.ok(votingService.getTownhallQuestions());
    }

    @PostMapping("/townhall")
    public ResponseEntity<TownhallQuestion> postQuestion(@RequestBody Map<String, String> payload) {
        TownhallQuestion q = votingService.postQuestion(
                payload.get("target"),
                payload.get("category"),
                payload.get("questionText"),
                payload.get("author")
        );
        return ResponseEntity.ok(q);
    }

    @PutMapping("/townhall/{id}/answer")
    public ResponseEntity<TownhallQuestion> answerQuestion(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        return votingService.answerQuestion(id, payload.get("answerText"))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

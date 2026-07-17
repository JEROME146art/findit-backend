package com.findit.voting.service;

import com.findit.voting.entity.*;
import com.findit.voting.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VotingService {

    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;
    private final BallotRecordRepository ballotRecordRepository;
    private final TownhallQuestionRepository townhallQuestionRepository;

    public VotingService(ElectionRepository electionRepository,
                         CandidateRepository candidateRepository,
                         BallotRecordRepository ballotRecordRepository,
                         TownhallQuestionRepository townhallQuestionRepository) {
        this.electionRepository = electionRepository;
        this.candidateRepository = candidateRepository;
        this.ballotRecordRepository = ballotRecordRepository;
        this.townhallQuestionRepository = townhallQuestionRepository;
    }

    public List<Election> getAllElections() {
        return electionRepository.findAll();
    }

    public Optional<Election> getElectionByCode(String code) {
        return electionRepository.findByElectionCode(code);
    }

    public Election saveElection(Election election) {
        return electionRepository.save(election);
    }

    public List<BallotRecord> getAuditLedger() {
        return ballotRecordRepository.findAll();
    }

    public Optional<BallotRecord> verifyBallotToken(String token) {
        if (!token.startsWith("#")) {
            token = "#" + token;
        }
        return ballotRecordRepository.findByReceiptTokenIgnoreCase(token);
    }

    public BallotRecord castBallot(String electionCode, String category, String choiceSummary, String token) {
        Optional<Election> electionOpt = electionRepository.findByElectionCode(electionCode);
        if (electionOpt.isPresent()) {
            Election e = electionOpt.get();
            e.setTotalVotes(e.getTotalVotes() + 1);
            electionRepository.save(e);
        }
        BallotRecord record = new BallotRecord(token, electionCode, category, choiceSummary);
        return ballotRecordRepository.save(record);
    }

    public List<TownhallQuestion> getTownhallQuestions() {
        return townhallQuestionRepository.findAllByOrderByUpvotesDesc();
    }

    public TownhallQuestion postQuestion(String target, String category, String text, String author) {
        TownhallQuestion q = new TownhallQuestion(target, category, text, author, 1, null);
        return townhallQuestionRepository.save(q);
    }

    public Optional<TownhallQuestion> answerQuestion(Long id, String answerText) {
        Optional<TownhallQuestion> qOpt = townhallQuestionRepository.findById(id);
        if (qOpt.isPresent()) {
            TownhallQuestion q = qOpt.get();
            q.setAnswerText(answerText);
            return Optional.of(townhallQuestionRepository.save(q));
        }
        return Optional.empty();
    }
}

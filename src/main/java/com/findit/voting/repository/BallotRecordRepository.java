package com.findit.voting.repository;

import com.findit.voting.entity.BallotRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BallotRecordRepository extends JpaRepository<BallotRecord, Long> {
    Optional<BallotRecord> findByReceiptTokenIgnoreCase(String receiptToken);
}

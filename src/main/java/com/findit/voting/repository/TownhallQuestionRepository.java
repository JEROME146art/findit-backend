package com.findit.voting.repository;

import com.findit.voting.entity.TownhallQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TownhallQuestionRepository extends JpaRepository<TownhallQuestion, Long> {
    List<TownhallQuestion> findAllByOrderByUpvotesDesc();
}

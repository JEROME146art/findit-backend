package com.findit.repository;

import com.findit.model.entity.FoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {

    // Find found items that are still available (not claimed, not resolved)
    List<FoundItem> findByClaimedFalseAndResolvedFalse();

    // Find items reported by a specific user
    List<FoundItem> findByReportedById(Long userId);
}
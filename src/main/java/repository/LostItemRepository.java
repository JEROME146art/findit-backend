package com.findit.repository;

import com.findit.model.entity.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostItemRepository extends JpaRepository<LostItem, Long> {

    // Find all lost items that haven't been resolved yet
    List<LostItem> findByResolvedFalse();

    // Find all lost items reported by a specific user
    List<LostItem> findByReportedById(Long userId);
}
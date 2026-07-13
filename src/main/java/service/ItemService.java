package com.findit.service;

import com.findit.model.dto.ItemRequest;
import com.findit.model.entity.*;
import com.findit.repository.FoundItemRepository;
import com.findit.repository.LostItemRepository;
import com.findit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final LostItemRepository lostRepo;
    private final FoundItemRepository foundRepo;
    private final UserRepository userRepo;

    public ItemService(LostItemRepository lostRepo,
                       FoundItemRepository foundRepo,
                       UserRepository userRepo) {
        this.lostRepo = lostRepo;
        this.foundRepo = foundRepo;
        this.userRepo = userRepo;
    }

    public Item reportItem(ItemRequest req, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if ("LOST".equalsIgnoreCase(req.getType())) {
            LostItem item = new LostItem();
            populateCommonFields(item, req, user);
            item.setLastSeenDate(req.getIncidentDate());
            return lostRepo.save(item);
        } else if ("FOUND".equalsIgnoreCase(req.getType())) {
            FoundItem item = new FoundItem();
            populateCommonFields(item, req, user);
            return foundRepo.save(item);
        } else {
            throw new RuntimeException("Invalid item type. Must be LOST or FOUND");
        }
    }

    private void populateCommonFields(Item item, ItemRequest req, User user) {
        item.setTitle(req.getTitle());
        item.setDescription(req.getDescription());
        item.setCategory(req.getCategory());
        item.setLocation(req.getLocation());
        item.setIncidentDate(req.getIncidentDate());
        item.setImageUrl(req.getImageUrl());
        item.setReportedBy(user);
    }

    // ==================== Read Methods ====================
    public List<LostItem> getAllLostItems() {
        return lostRepo.findAll();
    }

    public List<FoundItem> getAllFoundItems() {
        return foundRepo.findAll();
    }

    public LostItem getLostItem(Long id) {
        return lostRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lost item not found: " + id));
    }

    public FoundItem getFoundItem(Long id) {
        return foundRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Found item not found: " + id));
    }

    // ==================== Update Methods ====================
    /**
     * Mark a lost item as resolved (item was recovered).
     */
    public LostItem resolveLostItem(Long id) {
        LostItem item = getLostItem(id);
        item.setResolved(true);
        return lostRepo.save(item);
    }

    /**
     * Mark a found item as claimed (given back to owner).
     */
    public FoundItem claimFoundItem(Long id) {
        FoundItem item = getFoundItem(id);
        item.setClaimed(true);
        item.setResolved(true);
        return foundRepo.save(item);
    }

    // ==================== Delete Methods ====================
    public void deleteLostItem(Long id) {
        if (!lostRepo.existsById(id)) {
            throw new RuntimeException("Lost item not found: " + id);
        }
        lostRepo.deleteById(id);
    }

    public void deleteFoundItem(Long id) {
        if (!foundRepo.existsById(id)) {
            throw new RuntimeException("Found item not found: " + id);
        }
        foundRepo.deleteById(id);
    }
}
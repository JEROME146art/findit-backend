package com.findit.controller;

import com.findit.model.dto.ItemRequest;
import com.findit.model.entity.FoundItem;
import com.findit.model.entity.Item;
import com.findit.model.entity.LostItem;
import com.findit.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // ==================== CREATE ====================
    @PostMapping("/report/{userId}")
    public ResponseEntity<Item> reportItem(
            @PathVariable Long userId,
            @Valid @RequestBody ItemRequest request) {
        Item created = itemService.reportItem(request, userId);
        return ResponseEntity.ok(created);
    }

    // ==================== READ ====================
    @GetMapping("/lost")
    public ResponseEntity<List<LostItem>> getAllLostItems() {
        return ResponseEntity.ok(itemService.getAllLostItems());
    }

    @GetMapping("/found")
    public ResponseEntity<List<FoundItem>> getAllFoundItems() {
        return ResponseEntity.ok(itemService.getAllFoundItems());
    }

    @GetMapping("/lost/{id}")
    public ResponseEntity<LostItem> getLostItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getLostItem(id));
    }

    @GetMapping("/found/{id}")
    public ResponseEntity<FoundItem> getFoundItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getFoundItem(id));
    }

    // ==================== UPDATE ====================
    @PutMapping("/lost/{id}/resolve")
    public ResponseEntity<LostItem> resolveLostItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.resolveLostItem(id));
    }

    @PutMapping("/found/{id}/claim")
    public ResponseEntity<FoundItem> claimFoundItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.claimFoundItem(id));
    }

    // ==================== DELETE ====================
    @DeleteMapping("/lost/{id}")
    public ResponseEntity<Map<String, String>> deleteLostItem(@PathVariable Long id) {
        itemService.deleteLostItem(id);
        return ResponseEntity.ok(Map.of("message", "Lost item deleted successfully"));
    }

    @DeleteMapping("/found/{id}")
    public ResponseEntity<Map<String, String>> deleteFoundItem(@PathVariable Long id) {
        itemService.deleteFoundItem(id);
        return ResponseEntity.ok(Map.of("message", "Found item deleted successfully"));
    }
}
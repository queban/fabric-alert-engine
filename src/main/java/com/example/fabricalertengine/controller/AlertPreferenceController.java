package com.example.fabricalertengine.controller;

import com.example.fabricalertengine.entity.AlertPreference;
import com.example.fabricalertengine.repository.AlertPreferenceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alert-preferences")
public class AlertPreferenceController {

    private final AlertPreferenceRepository alertPreferenceRepository;

    public AlertPreferenceController(AlertPreferenceRepository alertPreferenceRepository) {
        this.alertPreferenceRepository = alertPreferenceRepository;
    }

    @GetMapping
    public List<AlertPreference> getAllAlertPreferences() {
        return alertPreferenceRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertPreference> getAlertPreferenceById(@PathVariable Long id) {
        return alertPreferenceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlertPreference> createAlertPreference(@RequestBody AlertPreference alertPreference) {
        AlertPreference saved = alertPreferenceRepository.save(alertPreference);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertPreference> updateAlertPreference(@PathVariable Long id, @RequestBody AlertPreference updatedAlertPreference) {
        return alertPreferenceRepository.findById(id)
                .map(existingAlertPreference -> {
                    existingAlertPreference.setUser(updatedAlertPreference.getUser());
                    existingAlertPreference.setFabric(updatedAlertPreference.getFabric());
                    existingAlertPreference.setTargetPrice(updatedAlertPreference.getTargetPrice());
                    AlertPreference saved = alertPreferenceRepository.save(existingAlertPreference);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlertPreference(@PathVariable Long id) {
        if (!alertPreferenceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        alertPreferenceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
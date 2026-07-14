package com.example.fabricalertengine.controller;

import com.example.fabricalertengine.entity.AlertPreference;
import com.example.fabricalertengine.entity.User;
import com.example.fabricalertengine.repository.AlertPreferenceRepository;
import com.example.fabricalertengine.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alert-preferences")
public class AlertPreferenceController {

    private final AlertPreferenceRepository alertPreferenceRepository;
    private final UserRepository userRepository;

    public AlertPreferenceController(AlertPreferenceRepository alertPreferenceRepository, UserRepository userRepository) {
        this.alertPreferenceRepository = alertPreferenceRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<AlertPreference> getAllAlertPreferences(Principal principal) {
        return alertPreferenceRepository.findByUserEmail(principal.getName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertPreference> getAlertPreferenceById(@PathVariable Long id, Principal principal) {
        return alertPreferenceRepository.findById(id)
                .filter(preference -> preference.getUser().getEmail().equals(principal.getName()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlertPreference> createAlertPreference(@Valid @RequestBody AlertPreference alertPreference, Principal principal) {
        Optional<AlertPreference> existing = alertPreferenceRepository
                .findByUserEmailAndFabricId(principal.getName(), alertPreference.getFabric().getId());

        if (existing.isPresent()) {
            AlertPreference existingPreference = existing.get();
            existingPreference.setTargetPrice(alertPreference.getTargetPrice());
            existingPreference.setAlreadyNotified(false);
            AlertPreference saved = alertPreferenceRepository.save(existingPreference);
            return ResponseEntity.ok(saved);
        }

        User owner = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Logged-in user not found: " + principal.getName()));
        alertPreference.setUser(owner);
        AlertPreference saved = alertPreferenceRepository.save(alertPreference);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertPreference> updateAlertPreference(@PathVariable Long id,
                                                                 @Valid @RequestBody AlertPreference updatedAlertPreference,
                                                                 Principal principal) {
        return alertPreferenceRepository.findById(id)
                .filter(preference -> preference.getUser().getEmail().equals(principal.getName()))
                .map(existingAlertPreference -> {
                    existingAlertPreference.setFabric(updatedAlertPreference.getFabric());
                    existingAlertPreference.setTargetPrice(updatedAlertPreference.getTargetPrice());
                    AlertPreference saved = alertPreferenceRepository.save(existingAlertPreference);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlertPreference(@PathVariable Long id, Principal principal) {
        return alertPreferenceRepository.findById(id)
                .filter(preference -> preference.getUser().getEmail().equals(principal.getName()))
                .map(preference -> {
                    alertPreferenceRepository.delete(preference);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
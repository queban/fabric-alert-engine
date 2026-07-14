package com.example.fabricalertengine.repository;

import com.example.fabricalertengine.entity.AlertPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertPreferenceRepository extends JpaRepository<AlertPreference, Long> {
    List<AlertPreference> findByUserEmail(String email);
    Optional<AlertPreference> findByUserEmailAndFabricId(String email, Long fabricId);
}
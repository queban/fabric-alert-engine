package com.example.fabricalertengine.repository;

import com.example.fabricalertengine.entity.AlertPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertPreferenceRepository extends JpaRepository<AlertPreference, Long> {
}
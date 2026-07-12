package com.example.fabricalertengine.repository;

import com.example.fabricalertengine.entity.Fabric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FabricRepository extends JpaRepository<Fabric, Long> {
}
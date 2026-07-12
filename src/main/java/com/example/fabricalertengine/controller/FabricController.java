package com.example.fabricalertengine.controller;

import com.example.fabricalertengine.entity.Fabric;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.fabricalertengine.repository.FabricRepository;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/fabrics")
public class FabricController {

    private final FabricRepository fabricRepository;

    public FabricController(FabricRepository fabricRepository) {
        this.fabricRepository = fabricRepository;
    }

    @GetMapping
    public List<Fabric> getAllFabrics() {
        return fabricRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fabric> getFabricById(@PathVariable Long id) {
        return fabricRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Fabric> createFabric(@RequestBody Fabric fabric) {
        Fabric saved = fabricRepository.save(fabric);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fabric> updateFabric(@PathVariable Long id, @RequestBody Fabric updatedFabric) {
        return fabricRepository.findById(id)
                .map(existingFabric -> {
                    existingFabric.setName(updatedFabric.getName());
                    existingFabric.setCurrentPricePerYard(updatedFabric.getCurrentPricePerYard());
                    Fabric saved = fabricRepository.save(existingFabric);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFabric(@PathVariable Long id) {
        if (!fabricRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        fabricRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

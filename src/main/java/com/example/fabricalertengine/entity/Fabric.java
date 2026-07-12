package com.example.fabricalertengine.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "fabrics")
public class Fabric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal currentPricePerYard;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getCurrentPricePerYard() { return currentPricePerYard; }
    public void setCurrentPricePerYard(BigDecimal currentPricePerYard) { this.currentPricePerYard = currentPricePerYard; }
}
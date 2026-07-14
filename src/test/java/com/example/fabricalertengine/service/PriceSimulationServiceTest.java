package com.example.fabricalertengine.service;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class PriceSimulationServiceTest {

    private final PriceSimulationService service = new PriceSimulationService(null);

    @Test
    void appliesPercentageAndRounds() {
        // 20.00 dropped by 4% -> 19.20
        BigDecimal result = service.calculateNewPrice(new BigDecimal("20.00"), -0.04);
        assertEquals(new BigDecimal("19.20"), result);
    }

    @Test
    void neverGoesBelowFloor() {
        // 5.10 dropped by 10% would be 4.59 -> clamped to the 5.00 floor
        BigDecimal result = service.calculateNewPrice(new BigDecimal("5.10"), -0.10);
        assertEquals(new BigDecimal("5.00"), result);
    }
}
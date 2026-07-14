package com.example.fabricalertengine.service;
import com.example.fabricalertengine.entity.Fabric;
import com.example.fabricalertengine.repository.FabricRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Random;
import org.springframework.scheduling.annotation.Scheduled;
import java.math.RoundingMode;
import java.util.List;


@Service
public class PriceSimulationService {

    private static final BigDecimal FLOOR = new BigDecimal("5.00");
    private final FabricRepository fabricRepository;

    private final Random random = new Random();

    public PriceSimulationService(FabricRepository fabricRepository) {
        this.fabricRepository = fabricRepository;
    }
    @Scheduled(fixedRate = 300000)
    public void simulatePrices(){
        List<Fabric> fabrics = fabricRepository.findAll();
        for (Fabric fabric : fabrics) {
            double percentChange = random.nextDouble() * 0.2 - 0.1;
            fabric.setCurrentPricePerYard(calculateNewPrice(fabric.getCurrentPricePerYard(), percentChange));
        }
        fabricRepository.saveAll(fabrics);
        System.out.println("Simulated new prices for " + fabrics.size() + " fabrics");
    }

    BigDecimal calculateNewPrice(BigDecimal currentPrice, double percentChange) {
        BigDecimal newPrice = currentPrice
                .multiply(BigDecimal.valueOf(1 + percentChange))
                .setScale(2, RoundingMode.HALF_UP);

        if (newPrice.compareTo(FLOOR) < 0) {
            return FLOOR;
        }
        return newPrice;
    }
}

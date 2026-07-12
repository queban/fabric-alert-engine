package com.example.fabricalertengine.service;

import com.example.fabricalertengine.repository.AlertPreferenceRepository;
import com.example.fabricalertengine.repository.FabricRepository;
import org.springframework.stereotype.Service;
import com.example.fabricalertengine.entity.AlertPreference;
import org.springframework.scheduling.annotation.Scheduled;


@Service
public class AlertCheckService {

    private final FabricRepository fabricRepository;
    private final AlertPreferenceRepository alertPreferenceRepository;
    private final EmailService emailService;

    public AlertCheckService(FabricRepository fabricRepository, AlertPreferenceRepository alertPreferenceRepository, EmailService emailService) {
        this.fabricRepository = fabricRepository;
        this.alertPreferenceRepository = alertPreferenceRepository;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkPrices() {
        for (AlertPreference preference : alertPreferenceRepository.findAll()) {
            boolean targetReached = preference.getFabric().getCurrentPricePerYard()
                    .compareTo(preference.getTargetPrice()) <= 0;

            if (targetReached && !preference.isAlreadyNotified()) {
                System.out.println("Alert! " + preference.getUser().getEmail()
                        + " - " + preference.getFabric().getName()
                        + " has hit their target price.");
                emailService.sendAlert(preference.getUser().getEmail(), preference.getFabric().getName());
                preference.setAlreadyNotified(true);
                alertPreferenceRepository.save(preference);
            } else if (!targetReached && preference.isAlreadyNotified()) {
                preference.setAlreadyNotified(false);
                alertPreferenceRepository.save(preference);
            }
        }

    }
}
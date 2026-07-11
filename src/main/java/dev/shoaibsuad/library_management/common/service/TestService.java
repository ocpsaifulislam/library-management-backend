package dev.shoaibsuad.library_management.common.service;

import dev.shoaibsuad.library_management.common.config.AppProperties;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    private final AppProperties appProperties;

    public TestService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public void printInfo() {
        System.out.println("Company : " + appProperties.getCompanyName());
        System.out.println("Version : " + appProperties.getVersion());
        System.out.println("Support : " + appProperties.getSupportEmail());
    }
}

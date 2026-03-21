package com.fbs.mock_evaluation_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// FIX #17 — removed unused imports:
// CommandLineRunner, Bean, Batch, Student, BatchRepository, StudentRepository

@SpringBootApplication
public class MockEvaluationSystemApplication {

    public static void main(String[] args) {
    	System.out.println("IPv6 preferred: " + System.getProperty("java.net.preferIPv6Addresses"));
        SpringApplication.run(MockEvaluationSystemApplication.class, args);
    }
}
package com.example.steps;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;

public class Hooks {

    @BeforeAll
    public static void beforeAll() {
        System.out.println("Starting Cucumber BDD API Tests with Playwright");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("Cucumber BDD API Tests completed");
        // Close Playwright instance
        ApiStepDefinitions.closePlaywright();
    }
}

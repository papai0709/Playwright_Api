package com.example.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME;

/**
 * Playwright API Test Runner
 * Runs only Playwright-based API tests using Cucumber BDD framework
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty,html:target/cucumber-reports/report.html,json:target/cucumber-reports/report.json,junit:target/cucumber-reports/report.xml")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.example.steps")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@sanity or @test")

public class CucumberTestRunner {
    // This class runs only Playwright API tests
    // All tests are defined in feature files under src/test/resources/features/
}

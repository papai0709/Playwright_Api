# BDD API Testing Framework with Playwright

This project has been converted from JUnit 5 to a Behavior-Driven Development (BDD) framework using Cucumber and Gherkin syntax.

## Project Structure

```
src/
  test/
    java/
      com/example/
        runners/
          CucumberTestRunner.java     # Main test runner
        steps/
          ApiStepDefinitions.java     # Basic API step definitions
          AdvancedApiStepDefinitions.java # Advanced API step definitions
          Hooks.java                  # Test lifecycle hooks
    resources/
      features/
        basic_api_operations.feature      # Basic CRUD operations
        advanced_api_operations.feature   # Advanced API testing scenarios
      cucumber.properties                 # Cucumber configuration
```

## Features

### Basic API Operations
- GET requests (single and multiple resources)
- POST requests (create new resources)
- PUT requests (full resource updates)
- DELETE requests
- Data-driven testing with Scenario Outlines

### Advanced API Operations
- Request with query parameters
- Custom headers handling
- PATCH requests (partial updates)
- Response header validation
- Error handling (404, etc.)
- Nested resource access
- Sequential API operations
- Response time validation
- Multiple HTTP methods testing

## Running Tests

### Run all tests
```bash
mvn test
```

### Run specific feature
```bash
mvn test -Dcucumber.filter.tags="@tag_name"
```

### Generate reports
```bash
mvn verify
```

Reports will be generated in:
- `target/cucumber-reports/` - HTML and JSON reports
- `target/cucumber-html-reports/` - Advanced HTML reports with charts

## Gherkin Syntax Examples

### Simple Scenario
```gherkin
Scenario: Get all users
  When I send a GET request to "/users"
  Then the response status should be 200
  And the response should be successful
```

### Scenario with Data Table
```gherkin
Scenario: Create a new post
  When I send a POST request to "/posts" with the following data:
    | title  | foo |
    | body   | bar |
    | userId | 1   |
  Then the response status should be 201
```

### Scenario Outline for Data-Driven Testing
```gherkin
Scenario Outline: Get posts for different users
  When I send a GET request to "/users/<userId>"
  Then the response status should be 200
  And the response should contain "<expectedName>"

  Examples:
    | userId | expectedName     |
    | 1      | Leanne Graham    |
    | 2      | Ervin Howell     |
```

## Step Definitions

The framework provides reusable step definitions for:

- **@Given** steps: Setup and preconditions
- **@When** steps: Actions and API calls
- **@Then** steps: Assertions and validations

## Benefits of BDD Approach

1. **Readable Tests**: Tests are written in plain English using Gherkin syntax
2. **Stakeholder Collaboration**: Business users can understand and contribute to test scenarios
3. **Reusable Steps**: Step definitions can be reused across multiple scenarios
4. **Living Documentation**: Feature files serve as up-to-date documentation
5. **Data-Driven Testing**: Easy to test multiple data sets using Scenario Outlines
6. **Better Reporting**: Rich HTML reports with step-by-step execution details

## Migration from JUnit 5

The original JUnit 5 tests have been converted to Gherkin feature files:
- `ApiTest.java` → `basic_api_operations.feature`
- `AdvancedApiTest.java` → `advanced_api_operations.feature`

All original functionality has been preserved and enhanced with BDD benefits.

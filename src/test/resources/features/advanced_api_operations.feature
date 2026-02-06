Feature: Advanced API Operations
  As a developer
  I want to perform advanced API testing scenarios
  So that I can validate complex API behaviors and edge cases

  Background:
    Given I have access to the "JSONPlaceholder" API with custom headers

  @sanity
  Scenario: GET request with query parameters
    When I send a GET request to "/posts" with query parameter "userId" set to "1"
    Then the response status should be 200
    And the response should be successful
    And the response should contain posts with userId 1
    And the content-type header should contain "application/json"

  @regression
  Scenario: POST request with complex JSON data
    When I send a POST request to "/posts" with the following JSON data:
      """
      {
        "title": "Test Post from Playwright",
        "body": "This is a test post created using Playwright API testing",
        "userId": 1
      }
      """
    Then the response status should be 201
    And the response should contain "Test Post from Playwright"

  @smoke
  Scenario: PATCH request for partial update
    When I send a PATCH request to "/posts/1" with the following JSON data:
      """
      {
        "title": "Patched Title"
      }
      """
    Then the response status should be 200
    And the response should contain "Patched Title"

  @sanity
  Scenario: Validate response headers
    When I send a GET request to "/users/1"
    Then the response status should be 200
    And the response headers should contain:
      | content-type | application/json |

    @sanity
  Scenario: Sequential API operations
    Given I create a post with title "Sequential Test" and body "Test body"
    When I update the post with id "1" with title "Updated Sequential Test"
    And I delete the post with id "1"
    Then all operations should be successful

  @regression
  Scenario: Response time validation
    When I send a GET request to "/users"
    Then the response status should be 200
    And the response time should be less than 5000 milliseconds


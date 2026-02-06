
Feature: Basic API Operations
  As a developer
  I want to perform basic CRUD operations on the JSONPlaceholder API
  So that I can validate the API functionality

  Background:
    Given I have access to the JSONPlaceholder API


  @sanity
  Scenario: Get all users
    When I send a GET request to "/users"
    Then the response status should be 200
    And the response should be successful
    And the response should contain user data

  @smoke
  Scenario: Get a single user
    When I send a GET request to "/users/1"
    Then the response status should be 200
    And the response should contain "Leanne Graham"

  @test
  Scenario: Create a new post
    When I send a POST request to "/posts" with the following data:
      | title  | foo |
      | body   | bar |
      | userId | 1   |
    Then the response status should be 201
    And the response should contain "foo"

  @regression
  Scenario: Update an existing post
    When I send a PUT request to "/posts/1" with the following data:
      | id     | 1             |
      | title  | updated title |
      | body   | updated body  |
      | userId | 1             |
    Then the response status should be 200
    And the response should contain "updated title"

  @regression
  Scenario: Delete a post
    When I send a DELETE request to "/posts/1"
    Then the response status should be 200

  @regression
  Scenario Outline: Get posts for different users
    When I send a GET request to "/users/<userId>"
    Then the response status should be 200
    And the response should contain "<expectedName>"

    Examples:
      | userId | expectedName     |
      | 1      | Leanne Graham    |
      | 2      | Ervin Howell     |
      | 3      | Clementine Bauch |

# Playwright API Testing with Java

This project demonstrates API testing using Playwright for Java, based on the official [Playwright API Testing documentation](https://playwright.dev/java/docs/api-testing).

## Prerequisites

- Java 17 or higher
- Maven 3.6+

## Project Structure

```
Playwright_API_Testing/
├── pom.xml
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── example/
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── ApiTest.java
└── README.md
```

## Dependencies

- **Playwright** (v1.48.0): For API testing
- **JUnit 5** (v5.10.1): For test framework

## Getting Started

### 1. Clone or navigate to the project

```bash
cd /Users/jay/Documents/Playwright_API_Testing
```

### 2. Install dependencies

```bash
mvn clean install
```

### 3. Run the tests

```bash
mvn test
```

## Example Tests

### Basic API Tests (`ApiTest.java`)

The `ApiTest.java` file includes basic example tests for:

1. **GET Request** - Fetch all users from JSONPlaceholder API
2. **GET Single Resource** - Fetch a single user by ID
3. **POST Request** - Create a new post
4. **PUT Request** - Update an existing post
5. **DELETE Request** - Delete a post

### Advanced API Tests (`AdvancedApiTest.java`)

The `AdvancedApiTest.java` file demonstrates advanced features:

1. **Query Parameters** - GET requests with URL parameters
2. **Custom Headers** - Setting custom HTTP headers
3. **Map-based Data** - Using Java Maps for request bodies
4. **PATCH Requests** - Partial updates to resources
5. **Response Headers** - Validating response headers
6. **Error Handling** - Testing 404 and other error responses
7. **Nested Resources** - Accessing nested API endpoints
8. **Sequential Requests** - Chaining multiple API calls
9. **Performance Testing** - Measuring response times

### Sample Test Code

```java
@Test
void shouldGetUsers() {
    APIResponse response = request.get("/users");
    assertEquals(200, response.status());
    assertTrue(response.ok());
    System.out.println("Response: " + response.text());
}

@Test
@DisplayName("POST request with JSON body using Map")
void testPostWithMapData() {
    Map<String, Object> data = new HashMap<>();
    data.put("title", "Test Post");
    data.put("body", "Test body");
    data.put("userId", 1);
    
    APIResponse response = request.post("/posts",
            RequestOptions.create().setData(data));
    
    assertEquals(201, response.status());
}
```

## API Endpoint Used

This project uses the free [JSONPlaceholder API](https://jsonplaceholder.typicode.com/) for testing purposes:

- Base URL: `https://jsonplaceholder.typicode.com`
- Endpoints:
  - `/users` - User resources
  - `/posts` - Post resources

## Features

- ✅ RESTful API testing with Playwright
- ✅ Support for GET, POST, PUT, DELETE requests
- ✅ Request/Response validation
- ✅ JUnit 5 integration
- ✅ Maven build configuration

## Running Individual Tests

To run a specific test class:

```bash
# Run only basic tests
mvn test -Dtest=ApiTest

# Run only advanced tests
mvn test -Dtest=AdvancedApiTest

# Run a specific test method
mvn test -Dtest=ApiTest#shouldGetUsers
```

## Test Results

After running `mvn test`, you should see:

```
Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
```

- 5 tests from `ApiTest.java` (basic API operations)
- 8 tests from `AdvancedApiTest.java` (advanced features)

## Resources

- [Playwright Java Documentation](https://playwright.dev/java/)
- [Playwright API Testing Guide](https://playwright.dev/java/docs/api-testing)
- [JSONPlaceholder API](https://jsonplaceholder.typicode.com/)

## License

This project is for educational and demonstration purposes.


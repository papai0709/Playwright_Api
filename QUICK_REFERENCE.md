# Playwright API Testing - Quick Reference Guide

## Quick Start Commands

```bash
# Navigate to project directory
cd /Users/jay/Documents/Playwright_API_Testing

# Install dependencies
mvn clean install

# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ApiTest
mvn test -Dtest=AdvancedApiTest

# Run specific test method
mvn test -Dtest=ApiTest#shouldGetUsers
```

## Common Test Patterns

### 1. Basic GET Request
```java
APIResponse response = request.get("/users");
assertEquals(200, response.status());
assertTrue(response.ok());
```

### 2. POST Request with JSON String
```java
String data = "{ \"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }";
APIResponse response = request.post("/posts",
    RequestOptions.create()
        .setHeader("Content-Type", "application/json")
        .setData(data));
assertEquals(201, response.status());
```

### 3. POST Request with Map
```java
Map<String, Object> data = new HashMap<>();
data.put("title", "Test");
data.put("body", "Content");
data.put("userId", 1);

APIResponse response = request.post("/posts",
    RequestOptions.create().setData(data));
```

### 4. PUT Request (Full Update)
```java
String data = "{ \"id\": 1, \"title\": \"updated\", \"body\": \"updated\", \"userId\": 1 }";
APIResponse response = request.put("/posts/1",
    RequestOptions.create()
        .setHeader("Content-Type", "application/json")
        .setData(data));
```

### 5. PATCH Request (Partial Update)
```java
String data = "{ \"title\": \"Patched Title\" }";
APIResponse response = request.patch("/posts/1",
    RequestOptions.create()
        .setHeader("Content-Type", "application/json")
        .setData(data));
```

### 6. DELETE Request
```java
APIResponse response = request.delete("/posts/1");
assertEquals(200, response.status());
```

### 7. Query Parameters
```java
// Method 1: In URL
APIResponse response = request.get("/posts?userId=1");

// Method 2: Using QueryParams (if needed)
APIResponse response = request.get("/posts?userId=1&_limit=5");
```

### 8. Custom Headers
```java
// During context creation
Map<String, String> headers = new HashMap<>();
headers.put("Accept", "application/json");
headers.put("Authorization", "Bearer token123");

request = playwright.request().newContext(
    new APIRequest.NewContextOptions()
        .setBaseURL("https://api.example.com")
        .setExtraHTTPHeaders(headers)
);

// Or per request
APIResponse response = request.get("/endpoint",
    RequestOptions.create()
        .setHeader("Custom-Header", "value"));
```

### 9. Response Validation
```java
// Status code
assertEquals(200, response.status());

// Status text
assertEquals("OK", response.statusText());

// Response body
String body = response.text();
assertTrue(body.contains("expected text"));

// Headers
Map<String, String> headers = response.headers();
assertTrue(headers.get("content-type").contains("application/json"));

// Check if response is OK (200-299)
assertTrue(response.ok());
```

### 10. Setup and Teardown
```java
private static Playwright playwright;
private static APIRequestContext request;

@BeforeAll
static void beforeAll() {
    playwright = Playwright.create();
    request = playwright.request().newContext(
        new APIRequest.NewContextOptions()
            .setBaseURL("https://api.example.com")
    );
}

@AfterAll
static void afterAll() {
    if (request != null) {
        request.dispose();
    }
    if (playwright != null) {
        playwright.close();
    }
}
```

## Assertions Reference

```java
// Status codes
assertEquals(200, response.status());      // OK
assertEquals(201, response.status());      // Created
assertEquals(204, response.status());      // No Content
assertEquals(400, response.status());      // Bad Request
assertEquals(401, response.status());      // Unauthorized
assertEquals(403, response.status());      // Forbidden
assertEquals(404, response.status());      // Not Found
assertEquals(500, response.status());      // Internal Server Error

// Response checks
assertTrue(response.ok());                 // Status 200-299
assertFalse(response.ok());               // Status outside 200-299
assertTrue(response.text().contains("..."));
assertNotNull(response.headers().get("content-type"));
```

## Test Organization

### Test Class Structure
```java
@DisplayName("API Tests for User Resource")
public class UserApiTest {
    
    @Test
    @DisplayName("Should retrieve all users")
    void testGetAllUsers() {
        // Test code
    }
    
    @Test
    @DisplayName("Should create a new user")
    void testCreateUser() {
        // Test code
    }
}
```

## Debugging Tips

### 1. Print Response
```java
System.out.println("Status: " + response.status());
System.out.println("Body: " + response.text());
System.out.println("Headers: " + response.headers());
```

### 2. Pretty Print JSON
```java
String json = response.text();
System.out.println(json);
```

### 3. Measure Response Time
```java
long start = System.currentTimeMillis();
APIResponse response = request.get("/endpoint");
long duration = System.currentTimeMillis() - start;
System.out.println("Response time: " + duration + "ms");
```

## Best Practices

1. **Use @BeforeAll and @AfterAll** for setup/teardown
2. **Set base URL** in request context creation
3. **Reuse APIRequestContext** across tests in a class
4. **Use meaningful test names** with @DisplayName
5. **Validate both status and response body**
6. **Handle cleanup** in @AfterAll to avoid resource leaks
7. **Use query parameters** for filtering
8. **Set appropriate headers** (Content-Type, Accept, etc.)
9. **Test error scenarios** (404, 400, etc.)
10. **Keep tests independent** - don't rely on test execution order

## Useful Resources

- [Playwright Java Docs](https://playwright.dev/java/)
- [API Testing Guide](https://playwright.dev/java/docs/api-testing)
- [APIRequestContext API](https://playwright.dev/java/docs/api/class-apirequestcontext)
- [JSONPlaceholder](https://jsonplaceholder.typicode.com/) - Free fake API for testing


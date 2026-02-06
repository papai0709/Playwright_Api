package com.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Advanced API Testing examples with Playwright
 * Demonstrates headers, authentication, query parameters, and response validation
 */
public class AdvancedApiTest {
    private static Playwright playwright;
    private static APIRequestContext request;

    @BeforeAll
    static void beforeAll() {
        playwright = Playwright.create();

        // Create API request context with custom headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("User-Agent", "Playwright-Java");

        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("https://jsonplaceholder.typicode.com")
                .setExtraHTTPHeaders(headers));
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

    @Test
    @DisplayName("GET request with query parameters")
    void testGetWithQueryParameters() {
        // Get posts for a specific user
        APIResponse response = request.get("/posts?userId=1");

        assertEquals(200, response.status());
        assertTrue(response.ok());
        assertTrue(response.text().contains("\"userId\": 1"));

        // Verify content type header
        assertTrue(response.headers().get("content-type").contains("application/json"));

        System.out.println("Posts count: " + response.text().split("\\{\"userId\":").length);
    }

    @Test
    @DisplayName("POST request with JSON body using Map")
    void testPostWithMapData() {
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Test Post from Playwright");
        data.put("body", "This is a test post created using Playwright API testing");
        data.put("userId", 1);

        APIResponse response = request.post("/posts",
                RequestOptions.create().setData(data));

        assertEquals(201, response.status());
        String responseBody = response.text();
        assertTrue(responseBody.contains("Test Post from Playwright"));

        System.out.println("Created post response: " + responseBody);
    }

    @Test
    @DisplayName("PATCH request - partial update")
    void testPatchRequest() {
        String patchData = "{ \"title\": \"Patched Title\" }";

        APIResponse response = request.patch("/posts/1",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(patchData));

        assertEquals(200, response.status());
        assertTrue(response.text().contains("Patched Title"));

        System.out.println("Patched post: " + response.text());
    }

    @Test
    @DisplayName("Validate response headers")
    void testResponseHeaders() {
        APIResponse response = request.get("/users/1");

        assertEquals(200, response.status());

        // Check response headers
        Map<String, String> headers = response.headers();
        assertNotNull(headers.get("content-type"));
        assertTrue(headers.get("content-type").contains("application/json"));

        System.out.println("Response headers:");
        headers.forEach((key, value) -> System.out.println(key + ": " + value));
    }

    @Test
    @DisplayName("Handle 404 Not Found")
    void testNotFound() {
        APIResponse response = request.get("/posts/99999");

        assertEquals(404, response.status());
        assertFalse(response.ok());

        System.out.println("404 Response: " + response.statusText());
    }

    @Test
    @DisplayName("GET nested resource")
    void testNestedResource() {
        // Get comments for a specific post
        APIResponse response = request.get("/posts/1/comments");

        assertEquals(200, response.status());
        String responseBody = response.text();
        assertTrue(responseBody.contains("\"postId\": 1"));

        System.out.println("Comments retrieved successfully");
    }

    @Test
    @DisplayName("Multiple requests in sequence")
    void testMultipleRequests() {
        // Create a post
        String createData = "{ \"title\": \"Sequential Test\", \"body\": \"Test body\", \"userId\": 1 }";
        APIResponse createResponse = request.post("/posts",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(createData));

        assertEquals(201, createResponse.status());

        // Update the post (simulated with fixed ID)
        String updateData = "{ \"title\": \"Updated Sequential Test\", \"body\": \"Updated body\", \"userId\": 1 }";
        APIResponse updateResponse = request.put("/posts/1",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(updateData));

        assertEquals(200, updateResponse.status());
        assertTrue(updateResponse.text().contains("Updated Sequential Test"));

        // Delete the post
        APIResponse deleteResponse = request.delete("/posts/1");
        assertEquals(200, deleteResponse.status());

        System.out.println("Sequential requests completed successfully");
    }

    @Test
    @DisplayName("Measure response time")
    void testResponseTime() {
        long startTime = System.currentTimeMillis();

        APIResponse response = request.get("/users");

        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        assertEquals(200, response.status());
        assertTrue(responseTime < 5000, "Response time should be less than 5 seconds");

        System.out.println("Response time: " + responseTime + "ms");
    }
}


package com.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ApiTest {
    private static Playwright playwright;
    private static APIRequestContext request;

    @BeforeAll
    static void beforeAll() { // Setup Playwright and API request context
        playwright = Playwright.create(); // Initialize Playwright
        request = playwright.request().newContext(new APIRequest.NewContextOptions() // Create a new API request context
                .setBaseURL("https://jsonplaceholder.typicode.com")); // Set the base URL for the API
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
    void shouldGetUsers() {
        APIResponse response = request.get("/users");
        assertEquals(200, response.status());
        assertTrue(response.ok());
        System.out.println("Response: " + response.text());
    }

    @Test
    void shouldGetSingleUser() {
        APIResponse response = request.get("/users/1");
        assertEquals(200, response.status());
        String responseBody = response.text();
        assertTrue(responseBody.contains("Leanne Graham"));
        System.out.println("User: " + responseBody);
    }

    @Test
    void shouldCreatePost() {
        String requestBody = "{ \"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }"; // JSON body for creating a post
        APIResponse response = request.post("/posts",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(requestBody));

        assertEquals(201, response.status());
        String responseBody = response.text();
        assertTrue(responseBody.contains("foo"));
        System.out.println("Created post: " + responseBody);
    }

    @Test
    void shouldUpdatePost() {
        String requestBody = "{ \"id\": 1, \"title\": \"updated title\", \"body\": \"updated body\", \"userId\": 1 }";
        APIResponse response = request.put("/posts/1",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(requestBody));

        assertEquals(200, response.status());
        String responseBody = response.text();
        assertTrue(responseBody.contains("updated title"));
        System.out.println("Updated post: " + responseBody);
    }

    @Test
    void shouldDeletePost() {
        APIResponse response = request.delete("/posts/1");
        assertEquals(200, response.status());
        System.out.println("Deleted post - Status: " + response.status());
    }
}


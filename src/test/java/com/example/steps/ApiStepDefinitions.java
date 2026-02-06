package com.example.steps;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ApiStepDefinitions {
    private static Playwright playwright;
    protected static APIRequestContext request;
    private static APIResponse lastResponse;
    private long requestStartTime;
    private long requestEndTime;

    public static APIResponse getLastResponse() {
        return lastResponse;
    }

    public static void setLastResponse(APIResponse response) {
        lastResponse = response;
    }

    @Before
    public void setUp() {
        if (playwright == null) {
            playwright = Playwright.create();
        }
    }

    @After
    public void tearDown() {
        if (request != null) {
            request.dispose();
            request = null;
        }
    }

    public static void closePlaywright() {
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    @Given("I have access to the JSONPlaceholder API")
    public void i_have_access_to_the_json_placeholder_api() {
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("https://jsonplaceholder.typicode.com"));
    }

    @Given("I have access to the {string} API with custom headers")
    public void i_have_access_to_the_json_placeholder_api_with_custom_headers() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("User-Agent", "Playwright-Java-BDD");

        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("https://jsonplaceholder.typicode.com")
                .setExtraHTTPHeaders(headers));
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        requestStartTime = System.currentTimeMillis();
        setLastResponse(request.get(endpoint));
        requestEndTime = System.currentTimeMillis();
    }

    @When("I send a GET request to {string} with query parameter {string} set to {string}")
    public void i_send_a_get_request_to_with_query_parameter(String endpoint, String paramName, String paramValue) {
        String urlWithParam = endpoint + "?" + paramName + "=" + paramValue;
        requestStartTime = System.currentTimeMillis();
        setLastResponse(request.get(urlWithParam));
        requestEndTime = System.currentTimeMillis();
    }


    @When("I send a POST request to {string} with the following data:")
    public void i_send_a_post_request_to_with_data_table(String endpoint, io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        Map<String, Object> requestData = new HashMap<>();

        data.forEach((key, value) -> {
            if ("userId".equals(key) || "id".equals(key)) {
                requestData.put(key, Integer.parseInt(value));
            } else {
                requestData.put(key, value);
            }
        });

        requestStartTime = System.currentTimeMillis();
        setLastResponse(request.post(endpoint, RequestOptions.create().setData(requestData))); // Default content type is application/json when using Map data
        requestEndTime = System.currentTimeMillis();
    }

    @When("I send a POST request to {string} with the following JSON data:")
    public void i_send_a_post_request_to_with_json_data(String endpoint, String jsonData) {
        requestStartTime = System.currentTimeMillis();
        setLastResponse(request.post(endpoint,
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(jsonData)));
        requestEndTime = System.currentTimeMillis();
    }

    @When("I send a PUT request to {string} with the following data:")
    public void i_send_a_put_request_to_with_data_table(String endpoint, io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        Map<String, Object> requestData = new HashMap<>();

        data.forEach((key, value) -> {
            if ("userId".equals(key) || "id".equals(key)) {
                requestData.put(key, Integer.parseInt(value));
            } else {
                requestData.put(key, value);
            }
        });

        requestStartTime = System.currentTimeMillis();
        setLastResponse(request.put(endpoint,
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(requestData)));
        requestEndTime = System.currentTimeMillis();
    }

    @When("I send a PATCH request to {string} with the following JSON data:")
    public void i_send_a_patch_request_to_with_json_data(String endpoint, String jsonData) {
        requestStartTime = System.currentTimeMillis();
        setLastResponse(request.patch(endpoint,
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(jsonData)));
        requestEndTime = System.currentTimeMillis();
    }

    @When("I send a DELETE request to {string}")
    public void i_send_a_delete_request_to(String endpoint) {
        requestStartTime = System.currentTimeMillis();
        setLastResponse(request.delete(endpoint));
        requestEndTime = System.currentTimeMillis();
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatus) {
        APIResponse response = getLastResponse();
        assertNotNull(response, "No response received");
        assertEquals(expectedStatus, response.status());
    }

    @Then("the response should be successful")
    public void the_response_should_be_successful() {
        APIResponse response = getLastResponse();
        assertNotNull(response, "No response received");
        assertTrue(response.ok());
    }

    @Then("the response should not be successful")
    public void the_response_should_not_be_successful() {
        APIResponse response = getLastResponse();
        assertNotNull(response, "No response received");
        assertFalse(response.ok());
    }

    @Then("the response should contain {string}")
    public void the_response_should_contain(String expectedText) {
        APIResponse response = getLastResponse();
        assertNotNull(response, "No response received");
        String responseBody = response.text();
        assertTrue(responseBody.contains(expectedText),
                "Response body does not contain: " + expectedText);
    }

    @Then("the response should contain user data")
    public void the_response_should_contain_user_data() {
        APIResponse response = getLastResponse();
        assertNotNull(response, "No response received");
        String responseBody = response.text();
        assertTrue(responseBody.contains("name") && responseBody.contains("email"),
                "Response does not contain expected user data fields");
    }

    @Then("the response should contain posts with userId {int}")
    public void the_response_should_contain_posts_with_user_id(int userId) {
        APIResponse response = getLastResponse();
        assertNotNull(response, "No response received");
        String responseBody = response.text();
        assertTrue(responseBody.contains("\"userId\": " + userId),
                "Response does not contain posts for userId: " + userId);
    }

    @Then("the response should contain comments with postId {int}")
    public void the_response_should_contain_comments_with_post_id(int postId) {
        APIResponse response = getLastResponse();
        assertNotNull(response, "No response received");
        String responseBody = response.text();
        assertTrue(responseBody.contains("\"postId\": " + postId),
                "Response does not contain comments for postId: " + postId);
    }

    @Then("the content-type header should contain {string}")
    public void the_content_type_header_should_contain(String expectedContentType) {
        APIResponse response = getLastResponse();
        assertNotNull(response, "No response received");
        String contentType = response.headers().get("content-type");
        assertNotNull(contentType, "Content-Type header not found");
        assertTrue(contentType.contains(expectedContentType),
                "Content-Type header does not contain: " + expectedContentType);
    }

    @Then("the response headers should contain:")
    public void the_response_headers_should_contain(io.cucumber.datatable.DataTable dataTable) {
        APIResponse response = getLastResponse();
        assertNotNull(response, "No response received");
        Map<String, String> expectedHeaders = dataTable.asMap(String.class, String.class);
        Map<String, String> actualHeaders = response.headers();

        expectedHeaders.forEach((headerName, expectedValue) -> {
            String actualValue = actualHeaders.get(headerName);
            assertNotNull(actualValue, "Header not found: " + headerName);
            assertTrue(actualValue.contains(expectedValue),
                    "Header " + headerName + " does not contain expected value: " + expectedValue);
        });
    }

    @Then("the response time should be less than {int} milliseconds")
    public void the_response_time_should_be_less_than_milliseconds(int maxTime) {
        long responseTime = requestEndTime - requestStartTime;
        assertTrue(responseTime < maxTime,
                "Response time " + responseTime + "ms exceeded maximum of " + maxTime + "ms");
    }
}

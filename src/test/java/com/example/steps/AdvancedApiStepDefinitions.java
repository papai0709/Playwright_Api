package com.example.steps;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.cucumber.java.en.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AdvancedApiStepDefinitions {

    private Map<String, APIResponse> responseStorage = new HashMap<>();
    private boolean allOperationsSuccessful = true;

    @Given("I create a post with title {string} and body {string}")
    public void i_create_a_post_with_title_and_body(String title, String body) {
        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("body", body);
        data.put("userId", 1);

        APIResponse response = ApiStepDefinitions.request.post("/posts", RequestOptions.create().setData(data));
        responseStorage.put("createPost", response);

        if (response.status() != 201) {
            allOperationsSuccessful = false;
        }
    }

    @When("I update the post with id {string} with title {string}")
    public void i_update_the_post_with_id_with_title(String postId, String newTitle) {
        String updateData = String.format("{ \"title\": \"%s\", \"body\": \"updated body\", \"userId\": 1 }", newTitle);

        APIResponse response = ApiStepDefinitions.request.put("/posts/" + postId,
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(updateData));

        responseStorage.put("updatePost", response);

        if (response.status() != 200) {
            allOperationsSuccessful = false;
        }
    }

    @When("I delete the post with id {string}")
    public void i_delete_the_post_with_id(String postId) {
        APIResponse response = ApiStepDefinitions.request.delete("/posts/" + postId);
        responseStorage.put("deletePost", response);

        if (response.status() != 200) {
            allOperationsSuccessful = false;
        }
    }

    @Then("all operations should be successful")
    public void all_operations_should_be_successful() {
        assertTrue(allOperationsSuccessful, "Not all sequential operations were successful");

        // Verify each stored response
        responseStorage.forEach((operation, response) -> {
            assertTrue(response.ok(), operation + " was not successful");
        });
    }
}

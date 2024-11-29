package com.hackfirst.flamesof5.automate_cte_onboarding;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class ProvisioningRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Step 1: Listen for HTTP requests at /provisionRequest and send to Kafka producer route (mocked)
        from("servlet:/provisionRequest")
                .log("Received provision request: ${body}")
                .marshal().json(JsonLibrary.Jackson) // Marshal incoming body to JSON
                .to("direct:kafkaProducer"); // Send to Kafka (mocked with direct endpoint in this case)

        // Step 2: Kafka producer (mocked for now, replace with actual Kafka producer later)
        from("direct:kafkaProducer")
                .log("Sending message to Kafka queue: ${body}")
                .to("direct:kafkaConsumer"); // Mock Kafka consumer (direct endpoint)

        // Step 3: Kafka consumer route that processes messages (mocked with direct)
        from("direct:kafkaConsumer")
                .log("Received message from mocked Kafka: ${body}")
                // Marshal the message to JSON format before sending it to the HTTP endpoint
                .marshal().json(JsonLibrary.Jackson) // Convert AccessRequest object to JSON
                // Call SailPoint API first (mocked)
                .log("SailPoint API called, request created for: ${body}")
                .to("http://localhost:8081/sailpoint/access-request")  // Mocked SailPoint API call
                // Call ServiceNow API second (mocked)
                .log("ServiceNow API called, request created for: ${body}")
                .to("http://localhost:8082/servicenow/access-request")  // Mocked ServiceNow API call
                .log("Request successfully sent to ServiceNow API for: ${body}");

        // Step 4: Dummy SailPoint API endpoint (to simulate the real API)
        // No consumption here, just a mock endpoint for testing
        from("jetty://http://localhost:8081/sailpoint/access-request")
                .log("SailPoint mock response: ${body}")
                .setBody(simple("SailPoint request processed for ${body}"))  // Mock response
                .setHeader("Content-Type", constant("application/json"));

        // Step 5: Dummy ServiceNow API endpoint (to simulate the real API)
        // No consumption here, just a mock endpoint for testing
        from("jetty://http://localhost:8082/servicenow/access-request")
                .log("ServiceNow mock response: ${body}")
                .setBody(simple("ServiceNow request processed for ${body}"))  // Mock response
                .setHeader("Content-Type", constant("application/json"));
    }
}

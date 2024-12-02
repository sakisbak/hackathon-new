package com.hackfirst.flamesof5.automate_cte_onboarding;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProvisioningRouteBuilder extends RouteBuilder {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void configure() throws Exception {
        // 1: Listen for HTTP requests at /provisionRequest and send to the Kafka producer route (mocked)
        from("servlet:/provisionRequest")
                .log("Received provision request: ${body}")
                .marshal().json(JsonLibrary.Jackson) // Marshal incoming body to JSON
                .to("direct:kafkaProducer"); // Send to Kafka (mocked with direct endpoint)

        // Step 2: Kafka producer (mocked for now, replace with actual Kafka producer later)
        from("direct:kafkaProducer")
                .log("Sending message to Kafka queue: ${body}")
                .to("direct:kafkaConsumer"); // Mock Kafka consumer (direct endpoint)

        // 3: Kafka consumer route that processes messages
        from("direct:kafkaConsumer")
                .log("Received message from mocked Kafka: ${body}")
                .process(new AccessRequestProcessor(jdbcTemplate))  // Processor step to handle team and access request logic
                .log("Sending request to Sailpoint API.. ")

                // 3.1: Call SailPoint API (mocked)
                .to("direct:sailpointApi")  // Send to SailPoint API (mocked)

                // 3.2: Processor For ServiceNow
                .process(new ServiceNowRequestProcessor()) // Processor for ServiceNow request
                .log("Sending ServiceNow request...")
                // 3.3: Call ServiceNow API
                .to("direct:serviceNowApi");  // Send to ServiceNow API (mocked)

        // 4: Mock SailPoint API call (202 response)
        from("direct:sailpointApi")
                .log("SailPoint API called, request created for: ${body}")
                .setHeader("CamelHttpResponseCode", constant(202))  // Set 202 response code to mock success
                .setBody(constant("SailPoint request successfully created."))  // Mock response body
                .log("SailPoint response: ${body}");

        // 5: Mock ServiceNow API call (mocked)
        from("direct:serviceNowApi")
                .log("ServiceNow API called, request created for: ${body}")
                .setHeader("CamelHttpResponseCode", constant(202))  // Set 202 response code to mock success
                .setBody(constant("ServiceNow request successfully created for software access"))  // Mock response body
                .log("ServiceNow response: ${body}");
    }
}

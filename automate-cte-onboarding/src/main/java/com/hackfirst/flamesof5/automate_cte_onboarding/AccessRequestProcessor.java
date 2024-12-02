package com.hackfirst.flamesof5.automate_cte_onboarding;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Processor to handle team-based access request creation for SailPoint
@Component
public class AccessRequestProcessor implements org.apache.camel.Processor {

    private final JdbcTemplate jdbcTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public AccessRequestProcessor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void process(org.apache.camel.Exchange exchange) throws Exception {
        // Extract the team from the incoming message (assuming it's part of the JSON)
        AccessRequest req = exchange.getIn().getBody(AccessRequest.class);  // Assuming team is the body of the message
        System.out.println("Team "+ req.getTeam());
        // Fetch the access requests for the team from the H2 database
        String query = "SELECT access_requests FROM TEMPLATE WHERE team_name = ?";
        String accessRequestsJson = jdbcTemplate.queryForObject(query, String.class, req.getTeam());
        List<String> accessRequests =  new ArrayList<>();
        try {
            // Use Jackson ObjectMapper to parse the JSON string into a List<String>
            accessRequests = objectMapper.readValue(accessRequestsJson, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Log the access requests retrieved from the database
        exchange.getIn().setHeader("accessRequests", accessRequests);

        // Check if we have access requests for this team
        if (accessRequests != null && !accessRequests.isEmpty()) {
            // Build the SailPoint request for each access request in the list
            StringBuilder sailPointRequest = new StringBuilder();
            for (String accessRequest : accessRequests) {
                sailPointRequest.append(buildSailPointRequest(accessRequest)).append("\n");
            }

            // Set the full SailPoint request in the body
            exchange.getIn().setBody(sailPointRequest.toString());
        } else {
            exchange.getIn().setBody(null);  // No requests found for this team
        }
    }

    // Method to construct the SailPoint request
    private String buildSailPointRequest(String accessRequest) {
        // For simplicity, we'll return a hardcoded SailPoint request.
        // In a real-world scenario, the request would be constructed dynamically.
        return "{\n" +
                "  \"requestedFor\": [\"2c918084660f45d6016617daa9210584\"],\n" +
                "  \"requestType\": \"GRANT_ACCESS\",\n" +
                "  \"requestedItems\": [\n" +
                "    {\n" +
                "      \"type\": \"ACCESS_PROFILE\",\n" +
                "      \"id\": \"2c9180835d2e5168015d32f890ca1581\",\n" +
                "      \"comment\": \"Requesting access profile for John Doe\",\n" +
                "      \"clientMetadata\": {\n" +
                "        \"requestedAppName\": \"" + accessRequest + "\",\n" +
                "        \"requestedAppId\": \"2c91808f7892918f0178b78da4a305a1\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"clientMetadata\": {\n" +
                "    \"requestedAppId\": \"2c91808f7892918f0178b78da4a305a1\",\n" +
                "    \"requestedAppName\": \"" + accessRequest + "\"\n" +
                "  }\n" +
                "}";
    }
}
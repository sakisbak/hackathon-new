package com.hackfirst.flamesof5.automate_cte_onboarding;

// Processor to prepare ServiceNow request for software access
public class ServiceNowRequestProcessor implements org.apache.camel.Processor {

    @Override
    public void process(org.apache.camel.Exchange exchange) throws Exception {
        // Extract the software access request (assuming it's a field in the message)
        String softwareRequest = exchange.getIn().getBody(String.class);  // Assuming software access request is the body of the message

        // Build the ServiceNow request (mocked, example)
        String serviceNowRequest = buildServiceNowRequest(softwareRequest);

        // Set the ServiceNow request in the body
        exchange.getIn().setBody(serviceNowRequest);
    }

    // Method to construct the ServiceNow request
    private String buildServiceNowRequest(String softwareRequest) {
        return  "{\n" +
                "  \"requestedFor\": \"2c918084660f45d6016617daa9210584\",\n" +
                "  \"requestType\": \"SOFTWARE_ACCESS\",\n" +
                "  \"requestedItems\": [\n" +
                "    {\n" +
                "      \"type\": \"SOFTWARE_PACKAGE\",\n" +
                "      \"id\": \"2c9180835d2e5168015d32f890ca1581\",\n" +
                "      \"softwareName\": \"IntelliJ Idea\",\n" +
                "      \"clientMetadata\": {\n" +
                "        \"softwareName\": \"IntelliJ Idea\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"SOFTWARE_PACKAGE\",\n" +
                "      \"id\": \"2c9180835d2e5168015d32f890ca1582\",\n" +
                "      \"softwareName\": \"Java 21\",\n" +
                "      \"clientMetadata\": {\n" +
                "        \"softwareName\": \"Java 21\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"SOFTWARE_PACKAGE\",\n" +
                "      \"id\": \"2c9180835d2e5168015d32f890ca1583\",\n" +
                "      \"softwareName\": \"SQL Developer\",\n" +
                "      \"clientMetadata\": {\n" +
                "        \"softwareName\": \"SQL Developer\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }
}
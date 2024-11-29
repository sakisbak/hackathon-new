package com.hackfirst.flamesof5.automate_cte_onboarding;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProvisionRequestController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @PostMapping("/provisionRequest")
    public String provisionRequest(@RequestBody AccessRequest request) {
        // Send the request to Camel route, which will place it into the Kafka queue
        producerTemplate.sendBody("direct:kafkaProducer", request);
        return "Provision request received and placed in queue.";
    }
}
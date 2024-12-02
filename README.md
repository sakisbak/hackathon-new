CURL to test on local


curl --location 'http://localhost:8080/provisionRequest' \
--header 'Content-Type: application/json' \
--data '{
    "empId": "12345",
    "projectName": "Project X",
    "team": "payments-dev"
}'




### **Final Application Overview:**

The application listens for HTTP requests, simulates message passing via Kafka (mocked), and makes HTTP calls to either the **SailPoint API** or **ServiceNow API** without checking for the `team` field. The application follows these steps:

### **Key Components:**

1. **Spring Boot Application**:
   - Uses **Spring Boot** for creating the RESTful API and managing the application's lifecycle.

2. **Camel Routes**:
   - **Route 1**: Exposes a `POST` HTTP endpoint `/provisionRequest`, where third-party systems send requests with the following fields:
     ```json
     {
       "empId": "12345",
       "projectName": "Project ABC"
     }
     ```
   - **Route 2**: Once the request is received, the application simulates the Kafka producer step by sending the message to a **mock Kafka queue** (using a direct endpoint for testing).
   - **Route 3**: The application simulates consuming messages from the Kafka queue and triggers **HTTP requests** to the **SailPoint API** and **ServiceNow API**. Both of these API calls are made irrespective of any conditions or fields like `team`.

3. **Mocking Kafka**:
   - Since Kafka is not actually running, the application uses a **mock Kafka consumer** and **direct endpoint** to simulate message handling and API calling.
   - The Kafka producer sends data to the mock Kafka consumer, and the message is processed from there.

4. **Camel HTTP Component**:
   - The application uses **camel-http** to make HTTP requests to both **SailPoint** and **ServiceNow** APIs. The requests are sent without any conditions based on the message contents, so both APIs are called for each request.

5. **Jetty Mock for API Endpoints**:
   - **Camel Jetty** is used to simulate the **SailPoint** and **ServiceNow** API endpoints on `localhost`.
   - This mock setup allows testing without actual external API servers.

### **Flow of the Application:**

1. **HTTP POST Request**:
   - A third-party system sends a POST request to `/provisionRequest` with a request body like:
     ```json
     {
       "empId": "12345",
       "projectName": "Project ABC"
     }
     ```

2. **Kafka Mocking**:
   - The request is serialized into JSON and forwarded to a **mock Kafka producer** route, which simulates putting the message into a Kafka queue.
   - It is then passed to the **mock Kafka consumer** route for processing.

3. **API Calls**:
   - In the **mock Kafka consumer** route, the application calls both the **SailPoint API** and **ServiceNow API** unconditionally, i.e., for every incoming message.
   - The request body is logged and sent to the respective API endpoints.

4. **Logging**:
   - Camel logs every action, such as receiving requests, sending messages to Kafka, and making the API calls to SailPoint and ServiceNow.

### **Dependencies**:
- **Spring Boot**: For application setup and dependency management.
- **Apache Camel**: For routing and integration between systems.
- **Camel Jetty**: For mocking the SailPoint and ServiceNow APIs.
- **Camel HTTP**: For making HTTP calls to mock APIs.
- **Gradle**: For managing dependencies and building the project.

### **Final Workflow**:

1. A third-party system sends an HTTP request to `/provisionRequest`.
2. The request is marshaled into JSON and passed to a mocked Kafka producer route.
3. The mock Kafka consumer receives the message and makes HTTP calls to both the **SailPoint API** and **ServiceNow API**.
4. Logs are generated to show the details of each action (API call, message handling, etc.).

This setup ensures the application works without an actual Kafka service or real API endpoints and allows easy testing of the integration logic for both the SailPoint and ServiceNow API calls.

# Gen-AI Training for Java Developers

## Project Setup
1. Clone the repository.
2. Edit `application.yaml` to add the following environment variables:
   ```yaml
    app:
      gen-ai:
        openai-client:
          key: ${OPEN_AI_KEY}
          endpoint: ${OPEN_AI_ENDPOINT}
          deployment-name: ${OPEN_AI_DEPLOYMENT_NAME}
   ```
3. Tune other application settings if necessary in the `application.yaml` file.
```yaml
    app:
      gen-ai:
        temperature: 0.8
        history-limit: 5
```
4. Run the SpringBoot Application. The application would start on the port 8080 by default.
5. Check available models by calling http://localhost:8080/available-models
6. Test the application by sending various of REST requests, you could find already prepared requests in the `Task<task_number>_request_test.http` file.
> [!NOTE]  
> If you want to continue cheating within the same context, you should use the chat/with-history endpoint and keep the same `chatId` in the request body with the PUT request.

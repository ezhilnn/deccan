package io.deccan.controlplane.connector.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.controlplane.connector.entity.Connector;
import io.deccan.controlplane.connector.capability.model.ConnectorCapability;
import io.deccan.controlplane.connector.enums.ConnectorType;
import io.deccan.controlplane.connector.repository.ConnectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConnectorBootstrap implements CommandLineRunner {

    private final ConnectorRepository repository;
    private final ObjectMapper mapper;

    @Override
    public void run(String... args) {

        createHttpConnector();
        createPostgresConnector();
        createKafkaConnector();
        createRedisConnector();
        createMinioConnector();
        createOpenAiConnector();
        createOllamaConnector();
        createQdrantConnector();
        createWebhookConnector();
    }

    private void createHttpConnector() {

        Connector http = create(
                "http",
                "HTTP Request",
                ConnectorType.ACTION,
                """
                {
                  "properties":{
                    "url":{"type":"string"},
                    "method":{"type":"string"},
                    "headers":{"type":"object"},
                    "body":{"type":"object"}
                  }
                }
                """
        );

        if (http.getCapabilities().isEmpty()) {
            http.getCapabilities().add(
                    capability(
                            "execute",
                            "Execute HTTP request"));

            http.getCapabilities().add(
                    capability(
                            "response",
                            "Return HTTP response"));

            repository.save(http);
        }
    }

    private void createPostgresConnector() {

        Connector connector = create(
                "postgres",
                "PostgreSQL",
                ConnectorType.DATABASE,
                """
                {
                  "properties":{
                    "query":{"type":"string"}
                  }
                }
                """
        );

        if (connector.getCapabilities().isEmpty()) {
            connector.getCapabilities().add(
                    capability(
                            "query",
                            "Execute SQL query"));

            repository.save(connector);
        }
    }

    private void createKafkaConnector() {

        Connector connector = create(
                "kafka",
                "Kafka",
                ConnectorType.MESSAGING,
                """
                {
                  "properties":{
                    "topic":{"type":"string"}
                  }
                }
                """
        );

        if (connector.getCapabilities().isEmpty()) {
            connector.getCapabilities().add(
                    capability(
                            "publish",
                            "Publish Kafka message"));

            connector.getCapabilities().add(
                    capability(
                            "consume",
                            "Consume Kafka messages"));

            repository.save(connector);
        }
    }

    private void createRedisConnector() {

        Connector connector = create(
                "redis",
                "Redis",
                ConnectorType.STORAGE,
                """
                {
                  "properties":{
                    "key":{"type":"string"}
                  }
                }
                """
        );

        if (connector.getCapabilities().isEmpty()) {
            connector.getCapabilities().add(
                    capability(
                            "get",
                            "Get value"));

            connector.getCapabilities().add(
                    capability(
                            "set",
                            "Set value"));

            repository.save(connector);
        }
    }

    private void createMinioConnector() {

        Connector connector = create(
                "minio",
                "MinIO",
                ConnectorType.STORAGE,
                """
                {
                  "properties":{
                    "bucket":{"type":"string"},
                    "object":{"type":"string"}
                  }
                }
                """
        );

        if (connector.getCapabilities().isEmpty()) {
            connector.getCapabilities().add(
                    capability(
                            "upload",
                            "Upload object"));

            connector.getCapabilities().add(
                    capability(
                            "download",
                            "Download object"));

            repository.save(connector);
        }
    }

    private void createOpenAiConnector() {

        Connector connector = create(
                "openai",
                "OpenAI",
                ConnectorType.AI,
                """
                {
                  "properties":{
                    "model":{"type":"string"},
                    "prompt":{"type":"string"}
                  }
                }
                """
        );

        if (connector.getCapabilities().isEmpty()) {
            connector.getCapabilities().add(
                    capability(
                            "chat",
                            "Generate completion"));

            repository.save(connector);
        }
    }

    private void createOllamaConnector() {

        Connector connector = create(
                "ollama",
                "Ollama",
                ConnectorType.AI,
                """
                {
                  "properties":{
                    "model":{"type":"string"},
                    "prompt":{"type":"string"}
                  }
                }
                """
        );

        if (connector.getCapabilities().isEmpty()) {
            connector.getCapabilities().add(
                    capability(
                            "chat",
                            "Generate local completion"));

            repository.save(connector);
        }
    }

    private void createQdrantConnector() {

        Connector connector = create(
                "qdrant",
                "Qdrant",
                ConnectorType.AI,
                """
                {
                  "properties":{
                    "collection":{"type":"string"},
                    "query":{"type":"string"}
                  }
                }
                """
        );

        if (connector.getCapabilities().isEmpty()) {
            connector.getCapabilities().add(
                    capability(
                            "search",
                            "Vector similarity search"));

            repository.save(connector);
        }
    }

    private void createWebhookConnector() {

        Connector connector = create(
                "webhook",
                "Webhook",
                ConnectorType.TRIGGER,
                """
                {
                  "properties":{
                    "path":{"type":"string"}
                  }
                }
                """
        );

        if (connector.getCapabilities().isEmpty()) {
            connector.getCapabilities().add(
                    capability(
                            "trigger",
                            "Receive webhook event"));

            repository.save(connector);
        }
    }

    private Connector create(
            String name,
            String displayName,
            ConnectorType type,
            String schema) {

        if (repository.existsByNameAndVersion(
                name,
                "1.0.0")) {

            return repository
                    .findByNameAndVersion(
                            name,
                            "1.0.0")
                    .orElseThrow();
        }

        Connector connector = new Connector();

        connector.setName(name);
        connector.setDisplayName(displayName);
        connector.setType(type);
        connector.setVersion("1.0.0");
        connector.setEnabled(true);

        try {
            connector.setConfigurationSchema(
                    mapper.readTree(schema));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return repository.save(connector);
    }

    private ConnectorCapability capability(
            String name,
            String description) {

        ConnectorCapability capability =
                new ConnectorCapability();

        capability.setName(name);
        capability.setDescription(description);

        return capability;
    }
}
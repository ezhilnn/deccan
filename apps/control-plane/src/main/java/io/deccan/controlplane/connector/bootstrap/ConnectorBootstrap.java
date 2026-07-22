package io.deccan.controlplane.connector.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.controlplane.connector.entity.Connector;
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
        create(
                "http",
                "HTTP Request",
                ConnectorType.ACTION,
                """
                {
                  "properties": {
                    "url":{"type":"string"},
                    "method":{"type":"string"},
                    "headers":{"type":"object"},
                    "body":{"type":"object"}
                  }
                }
                """
        );
    }

    private void createPostgresConnector() {
        create(
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
    }

    private void createKafkaConnector() {
        create(
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
    }

    private void createRedisConnector() {
        create(
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
    }

    private void createMinioConnector() {
        create(
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
    }

    private void createOpenAiConnector() {
        create(
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
    }

    private void createOllamaConnector() {
        create(
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
    }

    private void createQdrantConnector() {
        create(
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
    }

    private void createWebhookConnector() {
        create(
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
    }

    private void create(
            String name,
            String displayName,
            ConnectorType type,
            String schema) {

        if(repository.existsByNameAndVersion(
                name,
                "1.0.0")) {

            return;
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

        repository.save(connector);

    }

}
package io.deccan.worker.connector.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.worker.connector.Connector;
import io.deccan.worker.connector.ConnectorResult;
import io.deccan.worker.dto.response.ExecutionTaskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseConnector
        implements Connector {

    private final JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper;

    @Override
    public String type() {

        return "database";

    }

    @Override
    public ConnectorResult execute(
            ExecutionTaskResponse task) {

        try {

            DatabaseRequest request =
                    objectMapper.treeToValue(
                            task.getConfiguration(),
                            DatabaseRequest.class);

            String sql =
                    request.getSql().trim();

            if(sql.toUpperCase().startsWith("SELECT")){

                List<Map<String,Object>> rows =
                        jdbcTemplate.queryForList(sql);

                DatabaseResponse response =
                        DatabaseResponse.builder()
                                .rows(rows)
                                .affectedRows(rows.size())
                                .build();

                return ConnectorResult.builder()
                        .success(true)
                        .output(
                                objectMapper.valueToTree(response))
                        .build();

            }

            int count =
                    jdbcTemplate.update(sql);

            DatabaseResponse response =
                    DatabaseResponse.builder()
                            .affectedRows(count)
                            .build();

            return ConnectorResult.builder()
                    .success(true)
                    .output(
                            objectMapper.valueToTree(response))
                    .build();

        }
        catch (Exception ex){

            log.error(
                    "Database connector failed.",
                    ex);

            return ConnectorResult.builder()
                    .success(false)
                    .errorMessage(
                            ex.getMessage())
                    .build();

        }

    }

}
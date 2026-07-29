package io.deccan.worker.connector.database;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class DatabaseResponse {

    private int affectedRows;

    private List<Map<String,Object>> rows;

}
package io.deccan.worker.connector.database;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DatabaseRequest {

    private String datasource;

    private String sql;

    private List<Object> parameters;

}
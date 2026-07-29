package io.deccan.worker.connector.http;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class HttpRequestConfig {

    private String method;

    private String url;

    private Map<String,String> headers;

    private JsonNode body;

}
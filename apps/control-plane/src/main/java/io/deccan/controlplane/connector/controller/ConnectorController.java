package io.deccan.controlplane.connector.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.connector.dto.request.CreateConnectorRequest;
import io.deccan.controlplane.connector.dto.response.ConnectorResponse;
import io.deccan.controlplane.connector.mapper.ConnectorMapper;
import io.deccan.controlplane.connector.service.ConnectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/connectors")
@RequiredArgsConstructor
public class ConnectorController {

    private final ConnectorService service;

    private final ConnectorMapper mapper;

    @PreAuthorize("hasAuthority('connector.write')")
    @PostMapping
    public ApiResponse<ConnectorResponse> create(

            @Valid
            @RequestBody
            CreateConnectorRequest request){

        ConnectorResponse response=
                mapper.toResponse(

                        service.createConnector(

                                request.getOrganizationId(),

                                request.getName(),

                                request.getDisplayName(),

                                request.getType(),

                                request.getVersion(),

                                request.getConfigurationSchema()

                        )

                );

        return ApiResponse
                .<ConnectorResponse>builder()
                .status(201)
                .message("Connector created successfully")
                .data(response)
                .build();

    }

    @PreAuthorize("hasAuthority('connector.read')")
    @GetMapping
    public ApiResponse<List<ConnectorResponse>> getAll(){

        List<ConnectorResponse> response=

                service.getConnectors()
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        return ApiResponse
                .<List<ConnectorResponse>>builder()
                .status(200)
                .message("Connectors fetched successfully")
                .data(response)
                .build();

    }

}
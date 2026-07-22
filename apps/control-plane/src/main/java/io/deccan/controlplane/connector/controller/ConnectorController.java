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
import java.util.UUID;

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

        ConnectorResponse response =
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

        List<ConnectorResponse> response =
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

    @PreAuthorize("hasAuthority('connector.read')")
    @GetMapping("/{connectorId}")
    public ApiResponse<ConnectorResponse> get(

            @PathVariable UUID connectorId){

        return ApiResponse
                .<ConnectorResponse>builder()
                .status(200)
                .message("Connector fetched successfully")
                .data(
                        mapper.toResponse(
                                service.getConnector(connectorId)))
                .build();

    }

    @PreAuthorize("hasAuthority('connector.write')")
    @PutMapping("/{connectorId}")
    public ApiResponse<ConnectorResponse> update(

            @PathVariable UUID connectorId,

            @Valid
            @RequestBody
            CreateConnectorRequest request){

        return ApiResponse
                .<ConnectorResponse>builder()
                .status(200)
                .message("Connector updated successfully")
                .data(

                        mapper.toResponse(

                                service.updateConnector(

                                        connectorId,
                                        request.getDisplayName(),
                                        request.getType(),
                                        request.getConfigurationSchema(),
                                        true

                                )

                        )

                )
                .build();

    }

    @PreAuthorize("hasAuthority('connector.write')")
    @DeleteMapping("/{connectorId}")
    public ApiResponse<Void> delete(

            @PathVariable UUID connectorId){

        service.deleteConnector(connectorId);

        return ApiResponse
                .<Void>builder()
                .status(200)
                .message("Connector deleted successfully")
                .build();

    }
        @PreAuthorize("hasAuthority('connector.read')")
        @GetMapping("/{name}/versions")
        public ApiResponse<List<ConnectorResponse>> versions(

                @PathVariable String name){

        List<ConnectorResponse> response =
                service.getConnectorVersions(name)
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        return ApiResponse
                .<List<ConnectorResponse>>builder()
                .status(200)
                .message("Connector versions fetched successfully")
                .data(response)
                .build();

        }

}
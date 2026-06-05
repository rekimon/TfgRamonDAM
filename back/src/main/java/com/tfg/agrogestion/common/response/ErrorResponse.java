package com.tfg.agrogestion.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final int status;
    private final String error;
    private final String mensaje;
    private final String path;
    private final LocalDateTime timestamp;
    private final Map<String, String> erroresCampos;
}

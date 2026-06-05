package com.tfg.agrogestion.integration.homeassistant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeAssistantSensorDTO {

    private String state;

    @JsonProperty("attributes")
    private Map<String, Object> attributes;

    public Double getStateAsDouble() {
        try {
            return Double.parseDouble(this.state);
        } catch (Exception e) {
            return null;
        }
    }
}
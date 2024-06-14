package ru.cloudcom.pf.generator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Chtz {

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("service_code")
    private String serviceCode;

    @JsonProperty("steps")
    private List<Step> steps;

    public Step lastStep() {
        return steps.get(steps.size() - 1);
    }
}
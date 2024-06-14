package ru.cloudcom.pf.generator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class ChtzObject {

    @JsonProperty("is_visible_pdf")
    private Boolean isVisiblePdf;

}

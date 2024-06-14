package ru.cloudcom.pf.generator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.cloudcom.pf.generator.utils.ClearUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Field extends ChtzObject {

    @JsonProperty("number")
    private String number;

    @JsonProperty("name")
    private String name;

    @JsonProperty("xpath")
    private String xpath;

    @JsonProperty("type")
    private String type;

    public String formatName() {
        String[] path = xpath.split("/");
        String result = number + "_" + path[path.length - 1];
        return ClearUtil.clear(result);
    }

    public String formatXpath() {
        String regex = "\\/?\\/?(\\w+\\/)+\\w+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(xpath);
        String result;
        if (matcher.find()) {
            result = matcher.group(0);
        } else {
            result = "default";
        }

        if (!xpath.startsWith("//")) {
            if (xpath.startsWith("/")) {
                result = '/' + result;
            } else {
                result = "//" + result;
            }
        }
        return ClearUtil.clear(result);

    }
}
package ru.cloudcom.pf.generator.utils;

import org.springframework.stereotype.Component;

@Component
public class ClearUtil {
    public static String clear(String string){
        return string.replaceAll("\n", "").replaceAll(" ", "").replaceAll(" ", "").strip();
    }
    public static String clearBlockName(String blockName){
        return blockName.split("\n")[0];
    }
}

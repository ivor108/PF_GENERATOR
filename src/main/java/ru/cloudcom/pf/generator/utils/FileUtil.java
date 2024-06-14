package ru.cloudcom.pf.generator.utils;

import com.google.common.io.ByteStreams;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FileUtil {
    public InputStream getFileFromResourceAsStream(String fileName) {
        var classLoader = getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    public byte[] getFileFromResourceAsByteArray(String fileName) throws IOException {
        var classLoader = getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return ByteStreams.toByteArray(inputStream);
        }

    }
}

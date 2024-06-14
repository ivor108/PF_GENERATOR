package ru.cloudcom.pf.generator.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FileConverterUtil {

    @Autowired
    FileUtil fileUtil;

    private static final String HEADER_NAME = "content-disposition";
    private static final String HEADER_VALUE = "attachment; filename=";

    public ResponseEntity<byte[]> convertWithHeaders(String filepath, MediaType mediaType) throws IOException {
        byte[] contents = IOUtils.toByteArray(fileUtil.getFileFromResourceAsStream(filepath));
        String filename = filepath.split("/")[filepath.split("/").length - 1];
        var headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.add(HEADER_NAME, HEADER_VALUE + filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    public ResponseEntity<byte[]> convertWithHeaders(byte[] contents, String filename, MediaType mediaType) {
        var headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.add(HEADER_NAME, HEADER_VALUE + filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }
}

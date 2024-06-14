package ru.cloudcom.pf.generator.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cloudcom.pf.generator.dto.ParserResponse;
import ru.cloudcom.pf.generator.entity.ChtzEntity;
import ru.cloudcom.pf.generator.repository.ChtzRepository;
import ru.cloudcom.pf.generator.service.JRXMLGenerator;
import ru.cloudcom.pf.generator.utils.FileConverterUtil;

import javax.persistence.EntityNotFoundException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@Api(tags = "pfGenerator")
public class PfGeneratorController {

    @Autowired
    JRXMLGenerator generator;

    @Autowired
    public FileConverterUtil fileConverterUtil;

    @Autowired
    ChtzRepository chtzRepository;

    @ApiOperation(value = "Сгенерировать печатную форму", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ПФ успешно сгенерирована"),
            @ApiResponse(code = 400, message = "Неверный формат запроса"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервиса")
    })
    @PostMapping(value = "/print-form", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> getPrintForm(
        @RequestBody ParserResponse parserResponse
    ) throws XMLStreamException, IOException {
        String filename = "gu_" + parserResponse.getChtz().getServiceCode() + ".jrxml";
        return fileConverterUtil.convertWithHeaders(generator.generatePrintForm(parserResponse), filename, MediaType.APPLICATION_XML);
    }


    @ApiOperation(value = "Сгенерировать печатную форму по ЧТЗ из БД")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ПФ успешно сгенерирована"),
            @ApiResponse(code = 400, message = "Неверный формат запроса"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервиса")
    })
    @GetMapping("/{id}/get")
    public ResponseEntity<byte[]> getPrintFormById(@PathVariable String id)
            throws XMLStreamException, IOException {
        Optional<ChtzEntity> chtzEntity = chtzRepository.findById(id);
        if (chtzEntity.isPresent()){
            ParserResponse parserResponse = new ParserResponse(chtzEntity.get());
            String filename = "gu_" + parserResponse.getChtz().getServiceCode() + ".jrxml";
            return fileConverterUtil.convertWithHeaders(generator.generatePrintForm(parserResponse), filename, MediaType.APPLICATION_XML);
        }
        throw new EntityNotFoundException(id);
    }
}

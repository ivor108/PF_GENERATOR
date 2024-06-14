package ru.cloudcom.pf.generator.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.cloudcom.pf.generator.dto.ParserResponse;

@Slf4j
@RestController
@Api(tags = "pfGenerator")
public class PfGeneratorController {

    @ApiOperation(value = "Сгенерировать печатную форму", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ПФ успешно сгенерирована"),
            @ApiResponse(code = 400, message = "Неверный формат запроса"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервиса")
    })
    @PostMapping(value = "/print-form", consumes = MediaType.APPLICATION_JSON_VALUE)
    public byte[] getPrintForm(
        @RequestBody ParserResponse parserResponse
    ){
        return new byte[] { -56, -123, -109, -109, -106, 64, -26, -106, -103, -109, -124, 90 };
    }
}

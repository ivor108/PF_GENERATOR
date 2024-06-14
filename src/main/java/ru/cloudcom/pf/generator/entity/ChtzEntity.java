package ru.cloudcom.pf.generator.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.cloudcom.pf.generator.dto.ParserResponse;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "chtz")
@NoArgsConstructor
@Getter
public class ChtzEntity {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "chtz")
    private String chtz;
    @Column(name = "version")
    private String version;
    @Column(name = "created")
    private LocalDateTime created;

    public ChtzEntity(ParserResponse parserResponse) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        this.id = parserResponse.getId();
        this.chtz = ow.writeValueAsString(parserResponse.getChtz());
        this.version = parserResponse.getVersion();
        this.created = LocalDateTime.parse(parserResponse.getCreated());
    }
}

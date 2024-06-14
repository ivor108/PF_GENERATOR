package ru.cloudcom.pf.generator.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.cloudcom.pf.generator.dto.Block;
import ru.cloudcom.pf.generator.dto.Field;
import ru.cloudcom.pf.generator.dto.ParserResponse;
import ru.cloudcom.pf.generator.dto.Step;
import ru.cloudcom.pf.generator.utils.ClearUtil;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@Service
public class JRXMLGenerator {


    @Autowired
    JRXMLConstructor jrxmlConstructor;

    @Value("${report.countSymbolsInLine}")
    private Integer countSymbolsInLine;

    @Value("${report.countSymbolsInTopic}")
    private Integer countSymbolsInTopic;

    @Value("${report.elementHeight}")
    private Integer elementHeight;

    @Value("${report.topicTextExtraRowHeight}")
    private Integer topicTextExtraRowHeight;

    @Value("${report.staticTextExtraRowHeight}")
    private Integer staticTextExtraRowHeight;

    public byte[] generatePrintForm(ParserResponse parserResponse) throws XMLStreamException, IOException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        var outputStream = new ByteArrayOutputStream();
        var out = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        var writer = factory.createXMLStreamWriter(out);
        jrxmlConstructor.writeStartJRXML(writer);
        jrxmlConstructor.writeTagFromProperty(writer, "report.defaultImports");
        jrxmlConstructor.writeTagFromProperty(writer, "report.defaultFields");
        printFields(writer, parserResponse);
        jrxmlConstructor.writeTagFromProperty(writer, "report.defaultVariables");
        jrxmlConstructor.writeTagFromProperty(writer, "report.header");
        jrxmlConstructor.standardDetailStart(writer, 800);
        printDetails(parserResponse, writer);
        jrxmlConstructor.writeTagFromProperty(writer, "report.fileSubreport");
        writer.writeEndElement();
        jrxmlConstructor.writeTagFromProperty(writer, "report.footer");
        writer.writeEndDocument();
        writer.flush();
        outputStream.close();
        writer.close();
        return outputStream.toByteArray();
    }

    private void printDetails(ParserResponse parserResponse, XMLStreamWriter writer) {
        for (Step step : parserResponse.getChtz().getSteps()) {
            if (step.getIsVisiblePdf()) {
                for (Block block : step.getBlocks()) {
                    if (block.getIsVisiblePdf()) {
                        boolean isFirst = true;
                        int heightHeadline = getHeightHeadline(ClearUtil.clearBlockName(block.getName()));
                        if ((block.getFields().isEmpty() || !block.getIsVisiblePdf()) && (!block.getName().equals("") && !block.getName().equals("-"))) {
                            jrxmlConstructor.createDetailWithHeadline(writer, heightHeadline, ClearUtil.clearBlockName(block.getName()), block);
                        } else {
                            for (Field field : block.getFields()) {
                                if (field.getIsVisiblePdf()) {
                                    if (isFirst && !block.getName().equals("") && !block.getName().equals("-")) {
                                        jrxmlConstructor.createDetailWithHeadlineAndStandardLine(writer, field, getHeightLine(field), ClearUtil.clearBlockName(block.getName()), heightHeadline, block);
                                        isFirst = false;
                                    } else {
                                        jrxmlConstructor.createDetailWithStandardLine(writer, field, getHeightLine(field));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void printFields(XMLStreamWriter writer, ParserResponse parserResponse) {
        for (Step stap : parserResponse.getChtz().getSteps()) {
            for (Block block : stap.getBlocks()) {
                for (Field field : block.getFields()) {
                    if (field.getIsVisiblePdf()) {
                        jrxmlConstructor.writeFieldWithPath(writer, field.formatName(), field.formatXpath(), null);
                    }
                }
            }
        }
    }

    private int getHeightHeadline(String headline) {
        String[] headlineTextWords = headline.split(" ");
        int rowCount = 1;
        int rowBuffer = 0;
        for (String headlineTextWord : headlineTextWords) {
            int currentWordLength = headlineTextWord.length();
            if (headlineTextWord.length() >= countSymbolsInTopic) {
                rowCount += currentWordLength / countSymbolsInTopic;
                rowBuffer = currentWordLength % countSymbolsInTopic;
            } else {
                rowBuffer += 1 + headlineTextWord.length(); // " " + word
                if (rowBuffer > countSymbolsInTopic) {
                    rowCount++;
                    rowBuffer = headlineTextWord.length(); // without " "
                }
            }
        }
        return elementHeight + (rowCount - 1) * topicTextExtraRowHeight;
    }

    private int getHeightLine(Field field) {
        String LineText = field.getName();
        String[] lineTextWords = LineText.split(" ");
        int rowCount = 1;
        int rowBuffer = 0;
        for (String lineTextWord : lineTextWords) {
            int currentWordLength = lineTextWord.length();
            if (lineTextWord.length() >= countSymbolsInLine) {
                rowCount += currentWordLength / countSymbolsInLine;
                rowBuffer = currentWordLength % countSymbolsInLine;
            } else {
                rowBuffer += 1 + lineTextWord.length(); // " " + word
                if (rowBuffer > countSymbolsInLine) {
                    rowCount++;
                    rowBuffer = lineTextWord.length(); // without " "
                }
            }
        }
        return elementHeight + (rowCount - 1) * staticTextExtraRowHeight;
    }

}

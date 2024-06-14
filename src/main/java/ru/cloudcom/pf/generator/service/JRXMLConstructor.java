package ru.cloudcom.pf.generator.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.cloudcom.pf.generator.dto.Block;
import ru.cloudcom.pf.generator.dto.Field;

import javax.xml.stream.XMLStreamWriter;


@Service
public class JRXMLConstructor {


    @Autowired
    private Environment env;

    @Value("${report.elementHeight}")
    private Integer elementHeight;

    public void writeStartJRXML(XMLStreamWriter writer) {
        try {
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("jasperReport");
            writer.writeAttribute("xmlns", "http://jasperreports.sourceforge.net/jasperreports");
            writer.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            writer.writeAttribute("xsi:schemaLocation", "http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd");
            writer.writeAttribute("name", "new_name");
            writer.writeAttribute("language", "java");
            writer.writeAttribute("pageWidth", env.getProperty("report.pageWidth"));
            writer.writeAttribute("pageHeight", env.getProperty("report.pageHeight"));
            writer.writeAttribute("columnWidth", env.getProperty("report.columnWidth"));
            writer.writeAttribute("leftMargin", env.getProperty("report.leftMargin"));
            writer.writeAttribute("rightMargin", env.getProperty("report.rightMargin"));
            writer.writeAttribute("topMargin", env.getProperty("report.topMargin"));
            writer.writeAttribute("bottomMargin", env.getProperty("report.bottomMargin"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void writeFieldWithPath(XMLStreamWriter writer, String fieldName, String fieldPath, String fieldType) {
        if (fieldType == null)
            fieldType = "java.lang.String";
        if (fieldName == null)
            fieldName = "nullField";
        if (fieldPath == null)
            fieldPath = fieldName;

        try {
            writer.writeStartElement("field");
            writer.writeAttribute("name", fieldName);
            writer.writeAttribute("class", fieldType);
            writer.writeStartElement("fieldDescription");
            writer.writeCData(fieldPath);
            writer.writeEndElement();
            writer.writeEndElement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void standardHeadline(XMLStreamWriter writer, int count, String headline, String headlineHeight) {

        if (headline == null) headline = "";

        int headline_Y = elementHeight * count;

        try {
            writer.writeStartElement("staticText");
            writer.writeEmptyElement("reportElement");
            writer.writeAttribute("x", env.getProperty("report.headline_X"));
            writer.writeAttribute("y", Integer.toString(headline_Y));
            writer.writeAttribute("width", env.getProperty("report.headline_width"));
            writer.writeAttribute("height", headlineHeight);
            writer.writeStartElement("textElement");
            writer.writeEmptyElement("font");
            writer.writeAttribute("fontName", env.getProperty("report.fontName"));
            writer.writeAttribute("size", env.getProperty("report.fontTopicSize"));
            writer.writeAttribute("isBold", env.getProperty("report.isBoldTopic"));
            writer.writeEndElement();
            writer.writeStartElement("text");
            writer.writeCData(headline);
            writer.writeEndElement();
            writer.writeEndElement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void standardDetailStart(XMLStreamWriter writer, Integer detailHeight) {
        try {
            writer.writeStartElement("detail");
            writeTagFromProperty(writer, "report.beginningSubreport");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void standardBandStart(XMLStreamWriter writer, Integer detailHeight, String expression) {
        try {
            writer.writeStartElement("band");
            writer.writeAttribute("height", detailHeight.toString());
            writer.writeAttribute("splitType", env.getProperty("report.splitType"));
            if (!expression.equals("")) {
                writer.writeStartElement("printWhenExpression");
                writer.writeCData(expression);
                writer.writeEndElement();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void standardBandEnd(XMLStreamWriter writer) {
        try {
            writer.writeEndElement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //метод генерирует статик текст + текст филд, в текстфилд выводится переменная
    public void createStandardLine(XMLStreamWriter writer, String nameStaticText, String nameField, String textFieldExpression, Integer elementY, Integer elementH) {
        try {
            String convertDateFormat = "$V{dateUtils}.dateFormatting($F{%s})";
            String convertValueToYesOrNo = "$V{stringUtils}.convertTrueOrFalseToYesOrNo($F{%s})";
            writeStaticText(writer, nameField, elementY.toString(), elementH.toString(), nameStaticText);
            if (nameStaticText.contains("?"))
                textFieldExpression = String.format(convertValueToYesOrNo, nameField);
            if (nameStaticText.toLowerCase().contains("дата"))
                textFieldExpression = String.format(convertDateFormat, nameField);
            writeTextField(writer, nameField, ((Integer) (elementY + elementH - elementHeight)).toString(), textFieldExpression);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void writeStaticText(XMLStreamWriter writer, String nameField, String staticText_Y, String staticTextHeight, String staticText_text) {

        if (staticText_text == null) staticText_text = "";

        try {
            writer.writeStartElement("staticText");
            writer.writeStartElement("reportElement");
            writer.writeAttribute("positionType", env.getProperty("report.positionType"));
            writer.writeAttribute("x", env.getProperty("report.staticText_X"));
            writer.writeAttribute("y", staticText_Y);
            writer.writeAttribute("width", env.getProperty("report.staticText_width"));
            writer.writeAttribute("height", staticTextHeight);
            writer.writeAttribute("isRemoveLineWhenBlank", env.getProperty("report.isRemoveLineWhenBlank"));
            writer.writeStartElement("printWhenExpression");
            writer.writeCData("$F{" + nameField + "} != null");
            writer.writeEndElement();
            writer.writeEndElement();
            writer.writeEmptyElement("box");
            writer.writeAttribute("bottomPadding", env.getProperty("report.bottomPadding"));
            writer.writeStartElement("textElement");
            writer.writeEmptyElement("font");
            writer.writeAttribute("fontName", env.getProperty("report.fontName"));
            writer.writeAttribute("size", env.getProperty("report.fontTextSize"));
            writer.writeEndElement();
            writer.writeStartElement("text");
            writer.writeCData(staticText_text);
            writer.writeEndElement();
            writer.writeEndElement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void writeTextField(XMLStreamWriter writer, String nameField, String textField_Y, String textField_text) {
        String TextFieldExpression = "";
        if (textField_text == null) {
            TextFieldExpression = "$F{" + nameField + "}";
        } else {
            TextFieldExpression = textField_text;
        }

        try {
            writer.writeStartElement("textField");
            writer.writeAttribute("isStretchWithOverflow", env.getProperty("report.isStretchWithOverflow"));
            writer.writeAttribute("isBlankWhenNull", env.getProperty("report.isBlankWhenNull"));
            writer.writeStartElement("reportElement");
            writer.writeAttribute("positionType", env.getProperty("report.positionType"));
            writer.writeAttribute("x", env.getProperty("report.textField_X"));
            writer.writeAttribute("y", textField_Y);
            writer.writeAttribute("width", env.getProperty("report.textField_width"));
            writer.writeAttribute("height", env.getProperty("report.elementHeight"));
            writer.writeAttribute("isRemoveLineWhenBlank", env.getProperty("report.isRemoveLineWhenBlank"));
            writer.writeStartElement("printWhenExpression");
            writer.writeCData("$F{" + nameField + "} != null");
            writer.writeEndElement();
            writer.writeEndElement();
            writer.writeEmptyElement("box");
            writer.writeAttribute("bottomPadding", env.getProperty("report.bottomPadding"));
            writer.writeStartElement("textElement");
            writer.writeEmptyElement("font");
            writer.writeAttribute("fontName", env.getProperty("report.fontName"));
            writer.writeAttribute("size", env.getProperty("report.fontTextSize"));
            writer.writeEmptyElement("paragraph");
            writer.writeAttribute("leftIndent", env.getProperty("report.textFieldLeftIndent"));
            writer.writeEndElement();
            writer.writeStartElement("textFieldExpression");
            writer.writeCData(TextFieldExpression);
            writer.writeEndElement();
            writer.writeEndElement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeTagFromProperty(XMLStreamWriter writer, String tagName) {
        String tag = env.getProperty(tagName);
        try {
            writer.writeDTD(tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createDetailWithStandardLine(XMLStreamWriter writer, Field field, Integer elementH) {
        standardBandStart(writer, elementH, "$F{" + field.formatName() + "} != null");
        createStandardLine(writer, field.getName() + ":", field.formatName(), null, 0, elementH);
        standardBandEnd(writer);
    }

    public void createDetailWithHeadline(XMLStreamWriter writer, Integer elementH, String headline, Block block) {
        standardBandStart(writer, elementH, getHeadlineExpression(block));
        standardHeadline(writer, 0, headline, String.valueOf(elementH));
        standardBandEnd(writer);
    }

    public void createDetailWithHeadlineAndStandardLine(XMLStreamWriter writer, Field field, Integer elementH, String headline, Integer headlineHeight, Block block) {
        standardBandStart(writer, elementH + headlineHeight, getHeadlineExpression(block));
        standardHeadline(writer, 0, headline, String.valueOf(headlineHeight));
        createStandardLine(writer, field.getName() + ":", field.formatName(), null, headlineHeight, elementH);
        standardBandEnd(writer);
    }

    private String getHeadlineExpression(Block block) {
        StringBuilder result = new StringBuilder();
        for (Field field : block.getFields()) {
            if (field.getIsVisiblePdf()) {
                result.append("$F{").append(field.formatName()).append("} != null").append(" || ");
            }
        }
        if (!result.toString().isEmpty()) {
            result.delete(result.length() - 3, result.length());
        }
        return result.toString();
    }

}
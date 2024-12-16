package com.ryses.wagon.conversion.converter.domain.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class XliffTransUnitContextNode {

    @Getter
    @AllArgsConstructor
    public enum XliffUnitContextType
    {
        RECORD_TITLE("recordtitle");
        private String value;
    }

    @JacksonXmlProperty(isAttribute = true, localName = "context-type")
    private XliffUnitContextType type;

    @JacksonXmlText
    private String content;
}

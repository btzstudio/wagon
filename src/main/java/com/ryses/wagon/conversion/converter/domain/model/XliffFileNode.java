package com.ryses.wagon.conversion.converter.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

@Getter
@JsonInclude(Include.NON_NULL)
public class XliffFileNode {

    @JacksonXmlProperty(isAttribute = true)
    private String original;

    @JacksonXmlProperty(isAttribute = true, localName = "source-language")
    private String sourceLanguage;

    @JacksonXmlProperty(isAttribute = true, localName = "target-language")
    private String targetLanguage;

    @JacksonXmlProperty(isAttribute = true)
    private String datatype = "plaintext";

    @JacksonXmlProperty(localName = "body")
    private XliffBodyNode bodyNode;

    public XliffFileNode(XliffBodyNode bodyNode, String original, String sourceLanguage, String targetLanguage) {
        this.bodyNode = bodyNode;
        this.original = original;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
    }
}

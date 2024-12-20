package com.ryses.wagon.conversion.converter.domain.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;

@Getter
@JacksonXmlRootElement(localName = "xliff")
public final class XliffNode {

    @JacksonXmlProperty(isAttribute = true)
    private String xmlns = "urn:oasis:names:tc:xliff:document:1.2";

    @JacksonXmlProperty(localName = "file")
    private XliffFileNode fileNode;

    @JacksonXmlProperty(isAttribute = true)
    private String version = "1.2";

    public XliffNode(final XliffFileNode fileNode) {
        this.fileNode = fileNode;
    }
}

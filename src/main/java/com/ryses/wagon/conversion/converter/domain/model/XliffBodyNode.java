package com.ryses.wagon.conversion.converter.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class XliffBodyNode {

    @JacksonXmlProperty(localName = "trans-unit")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Collection<XliffTransUnitNode> transUnitNodes;
}

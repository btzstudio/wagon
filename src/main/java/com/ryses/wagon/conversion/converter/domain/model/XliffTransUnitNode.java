package com.ryses.wagon.conversion.converter.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class XliffTransUnitNode {

    @JacksonXmlProperty(isAttribute = true)
    private String id;

    @JacksonXmlProperty(isAttribute = true, localName = "resname")
    private String resourceName;

    @JacksonXmlProperty()
    private String source;

    @JacksonXmlProperty()
    private String target;

    @JsonIgnore
    @JacksonXmlProperty(localName = "context")
    private XliffTransUnitContextNode context;
}

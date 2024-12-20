package com.ryses.wagon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.ryses.wagon.conversion.I18nConversionTask;
import com.ryses.wagon.conversion.I18nFileConversionVoter;
import com.ryses.wagon.conversion.domain.ConversionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.function.Function;

@Configuration
public class SpringConfig {

    private final I18nFileConversionVoter i18nFileConverter;

    @Autowired
    public SpringConfig(I18nFileConversionVoter i18nFileConverter) {
        this.i18nFileConverter = i18nFileConverter;
    }

    @Bean
    public Function<ConversionRequest, I18nConversionTask> createI18nConversionTaskFactory() {
        return this::createI18nConversionTask;
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public I18nConversionTask createI18nConversionTask(ConversionRequest request) {
        var task = new I18nConversionTask(i18nFileConverter);
        task.setConversionRequest(request);
        return task;
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public XmlMapper buildXmlMapper() {
        var xmlMapper = new XmlMapper();
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return xmlMapper;
    }
}

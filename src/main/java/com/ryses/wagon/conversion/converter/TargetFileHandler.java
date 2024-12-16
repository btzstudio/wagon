package com.ryses.wagon.conversion.converter;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.ryses.wagon.conversion.ConversionTool;
import com.ryses.wagon.conversion.converter.domain.model.*;
import com.ryses.wagon.conversion.domain.ConversionRequest;
import com.ryses.wagon.conversion.domain.I18nConversionResult;
import com.ryses.wagon.conversion.domain.I18nFileComponent;
import com.ryses.wagon.version.domain.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.*;

@Service
public class TargetFileHandler implements I18nConversionHandler {

    private final XmlMapper xmlMapper;

    @Autowired
    public TargetFileHandler(@Lazy XmlMapper xmlMapper) {
        this.xmlMapper = xmlMapper;
    }

    @Override
    public Collection<I18nConversionResult> handle(final ConversionRequest request, final File exportDir) {
        var files = request.getDirectory()
            .listFiles((_, name) -> name.matches("^(?<domain>[^.]+)\\.(?!" + request.getSourceLocale().getLanguage() + ")([a-z]{2})\\.ya?ml$"))
        ;

        if (files == null) {
            return List.of(I18nConversionResult.Zero);
        }

        var details = new ArrayList<I18nConversionResult>();
        var yaml = new Yaml();

        for (File file : Arrays.stream(files).toList()) {
            var component = ConversionTool.getComponentParts(file);
            var path = Path.of(file.toURI());

            try {
                Map<Object, Object> yamlItems = yaml.load(Files.readString(path));
                var items = ConversionTool.flatMap(yamlItems, "");
                var transUnits = items.entrySet()
                        .stream()
                        .map(entry -> new XliffTransUnitNode(
                                entry.getKey().toString(),
                                entry.getKey().toString(),
                                entry.getKey().toString(),
                                entry.getValue().toString(),
                                new XliffTransUnitContextNode(
                                        XliffTransUnitContextNode.XliffUnitContextType.RECORD_TITLE,
                                        MessageFormat.format("The {0} {1} translation", entry.getKey().toString(), Locale.of(component.getLocale()).getDisplayLanguage())
                                )
                        ))
                        .toList()
                    ;

                var fileName = this.getSourceFileName(component.getDomain(), request.getVersion());
                var sourceLocale = request.getSourceLocale().getLanguage();

                var xliffNode = new XliffNode(
                        new XliffFileNode(
                                new XliffBodyNode(transUnits),
                                "/" + fileName,
                                sourceLocale,
                                component.getLocale()
                        )
                );

                var outputFile = new File(exportDir.getAbsolutePath() + File.separator + this.getTargetFileName(component, request.getVersion()));
                xmlMapper.writeValue(outputFile, xliffNode);

                details.add(new I18nConversionResult(component, items.size(), outputFile, false, request, exportDir));

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        return details;
    }

    private String getSourceFileName(String domain, Version version) {
        if (version != null) {
            return MessageFormat.format("{0}.{1}.xlf", domain, version.toString().toLowerCase());
        }

        return MessageFormat.format("/{0}.xlf", domain);
    }

    private String getTargetFileName(I18nFileComponent component, Version version) {
        if (version != null) {
            return MessageFormat.format("{0}.{1}.{2}.xlf", component.getDomain(), version.toString().toLowerCase(), component.getLocale());
        }

        return MessageFormat.format("/{0}.{1}.xlf", component.getDomain(), component.getLocale());
    }
}

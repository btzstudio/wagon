package com.ryses.wagon.conversion;

import com.ryses.wagon.conversion.converter.I18nConversionHandler;
import com.ryses.wagon.conversion.domain.ConversionRequest;
import com.ryses.wagon.conversion.domain.I18nConversionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
public class I18nFileConversionVoter {

    private final Collection<I18nConversionHandler> handlers;

    @Autowired
    public I18nFileConversionVoter(final Collection<I18nConversionHandler> handlers) {
        this.handlers = handlers;
    }

    public Collection<I18nConversionResult> convert(final ConversionRequest request) {
        var uuid = UUID.randomUUID().toString();
        var exportDir = new File(System.getProperty("user.home") +  File.separator + uuid);

        if (!exportDir.mkdir()) {
            throw new RuntimeException("Unable to create directory: " + exportDir);
        }

        var results = new ArrayList<I18nConversionResult>();

        handlers.forEach(handler -> results.addAll(handler.handle(request, exportDir)));

        return results;
    }
}

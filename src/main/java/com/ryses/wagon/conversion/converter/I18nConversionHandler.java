package com.ryses.wagon.conversion.converter;

import com.ryses.wagon.conversion.domain.ConversionRequest;
import com.ryses.wagon.conversion.domain.I18nConversionResult;

import java.io.File;
import java.util.Collection;

public interface I18nConversionHandler {
    Collection<I18nConversionResult> handle(final ConversionRequest request, final File exportDir);
}

package com.ryses.wagon.conversion.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@Getter
@AllArgsConstructor
public final class I18nConversionResult {
    public static final I18nConversionResult Zero = new I18nConversionResult(null, 0, null, false, null ,null);

    private final I18nFileComponent component;
    private final int unitCount;
    private final File file;
    private final boolean source;
    private final ConversionRequest request;
    private final File exportDirectory;

}

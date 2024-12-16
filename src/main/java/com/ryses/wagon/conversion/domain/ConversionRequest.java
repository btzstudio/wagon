package com.ryses.wagon.conversion.domain;

import com.ryses.wagon.version.domain.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.util.Locale;

@Getter
@AllArgsConstructor
public class ConversionRequest {
    private final File directory;
    private final Version version;
    private final Locale sourceLocale;
}

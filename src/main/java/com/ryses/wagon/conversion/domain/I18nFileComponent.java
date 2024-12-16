package com.ryses.wagon.conversion.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class I18nFileComponent {
    private String fileName;
    private String domain;
    private String locale;
    private String subType;
    private String extension;

    public Optional<String> getSubType() {
        return Optional.ofNullable(subType);
    }
}

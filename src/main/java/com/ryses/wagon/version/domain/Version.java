package com.ryses.wagon.version.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Version {
    private File directory = null;
    private boolean selected = false;

    @Override
    public String toString() {
        if (directory == null) {
            return "DEFAULT";
        }

        return directory.getName().toUpperCase();
    }
}

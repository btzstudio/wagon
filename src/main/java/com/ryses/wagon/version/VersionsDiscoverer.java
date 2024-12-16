package com.ryses.wagon.version;

import com.ryses.wagon.version.domain.Version;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class VersionsDiscoverer implements Function<File, Optional<List<Version>>> {

    @Override
    public Optional<List<Version>> apply(final File directory) {
        var parent = Optional.ofNullable(directory.getParentFile());

        do {
            if (parent.isEmpty()) {
                return Optional.empty();
            }

            var versionDirectory = parent.filter(this.filterVersion());

            var versions = versionDirectory
                .map(File::getParentFile)
                .map(File::listFiles)
                .map(Arrays::asList)
                    .map(directories -> directories.stream().filter(this.filterVersion()).toList())
            ;

            if (versions.isPresent()) {
                return versions
                    .map(dirs -> dirs.stream()
                        .map(version -> new Version(version, versionDirectory.get().getName().contentEquals(version.getName())))
                        .collect(Collectors.toList()
                    )
                );
            }

            parent = parent.map(File::getParentFile);
        } while (true);
    }

    private Predicate<File> filterVersion() {
        return directory -> directory.getName().matches("^v[0-9]{1}$");
    }
}

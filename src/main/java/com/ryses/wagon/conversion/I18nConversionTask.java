package com.ryses.wagon.conversion;

import com.ryses.wagon.conversion.domain.ConversionRequest;
import com.ryses.wagon.conversion.domain.I18nConversionResult;
import javafx.concurrent.Task;

import java.util.Collection;

public class I18nConversionTask extends Task<Collection<I18nConversionResult>> {
    private final I18nFileConversionVoter fileConverter;
    private ConversionRequest request;

    public I18nConversionTask(final I18nFileConversionVoter fileConverter) {
        this.fileConverter = fileConverter;
    }

    public void setConversionRequest(final ConversionRequest request) {
        this.request = request;
    }

    @Override
    protected Collection<I18nConversionResult> call() throws Exception {
        return fileConverter.convert(request);
    }
}

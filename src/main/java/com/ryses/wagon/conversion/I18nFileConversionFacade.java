package com.ryses.wagon.conversion;

import com.ryses.wagon.conversion.domain.ConversionRequest;
import com.ryses.wagon.conversion.domain.I18nConversionResult;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class I18nFileConversionFacade {

    private final Function<ConversionRequest, I18nConversionTask> i18nConversionTaskFactory;

    @Autowired
    public I18nFileConversionFacade(Function<ConversionRequest, I18nConversionTask> i18nConversionTaskFactory) {
        this.i18nConversionTaskFactory = i18nConversionTaskFactory;
    }

    public CompletableFuture<Collection<I18nConversionResult>> convert(final ConversionRequest request) {
        final var service = new javafx.concurrent.Service<Collection<I18nConversionResult>>() {

            @Override
            protected Task<Collection<I18nConversionResult>> createTask() {
                return i18nConversionTaskFactory.apply(request);
            }
        };

        var future = new CompletableFuture<Collection<I18nConversionResult>>();

        service.setOnSucceeded(_ -> {
            var results = service.getValue();
            future.complete(results);
        });

        service.setOnFailed(_ -> {
            System.out.println(service.getException().getMessage());
            future.completeExceptionally(service.getException());
        });

        service.start();
        return future;
    }
}

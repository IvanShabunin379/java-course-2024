package edu.java.retry;

import edu.java.exceptions.ServerErrorException;
import java.time.Duration;
import java.util.Set;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class RetryBuilder {
    private int maxAttempts = 0;
    private Duration duration = Duration.ZERO;
    private Set<Integer> statusCodes = Set.of(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.BAD_GATEWAY.value(),
        HttpStatus.SERVICE_UNAVAILABLE.value()
    );

    public RetryBuilder withMaxAttempts(int maxAttempts) {
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("Numbers of attempts should be positive.");
        }

        this.maxAttempts = maxAttempts;
        return this;
    }

    public RetryBuilder withDuration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public RetryBuilder withStatusCodes(Set<Integer> statusCodes) {
        this.statusCodes = statusCodes;
        return this;
    }

    public Retry buildWithConstantStrategy() {
        return Retry.fixedDelay(maxAttempts, duration)
            .filter(this::isServerErrorException)
            .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> retrySignal.failure()));
    }

    public Retry buildWithExponentialStrategy() {
        return Retry.fixedDelay(maxAttempts, duration)
            .filter(this::isServerErrorException)
            .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> retrySignal.failure()));
    }

    public Retry buildWithLinealStrategy() {
        return new Retry() {
            @Override
            public Publisher<?> generateCompanion(Flux<RetrySignal> flux) {
                return flux.flatMap((retrySignal -> {
                    Throwable throwable = retrySignal.failure();

                    if (isServerErrorException(throwable)) {
                        long currentAttempt = retrySignal.totalRetries() + 1;
                        if (currentAttempt <= maxAttempts) {
                            return Mono.delay(duration.multipliedBy(currentAttempt))
                                .thenReturn(retrySignal.totalRetries());
                        }
                    }
                    return Mono.error(throwable);
                }));
            }
        };
    }

    private boolean isServerErrorException(Throwable throwable) {
        return (throwable instanceof ServerErrorException)
            && (statusCodes.contains(((ServerErrorException) throwable).getHttpStatus().value()));
    }
}

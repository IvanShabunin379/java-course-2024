package edu.java.bot.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class ProcessedTelegramMessagesCounter {
    private final Counter counter;

    public ProcessedTelegramMessagesCounter(MeterRegistry meterRegistry) {
        counter = meterRegistry.counter("processed_tg_messages_count");
    }

    public void incrementCounter() {
        counter.increment();
    }
}

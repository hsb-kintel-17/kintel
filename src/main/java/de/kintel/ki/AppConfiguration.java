package de.kintel.ki;

import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
    @Bean
    public EventBus getEventBus() {
        return new EventBus(getClass().getName());
    }
}

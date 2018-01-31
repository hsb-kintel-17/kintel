/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.event;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
/**
 * A component, that will catch all the {@link DeadEvent}s.
 * DeadEvents are generated, if an event is posted on the {@link EventBus}, that has no consumer.
 *
 */
public class DeadEventProcessor {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DeadEventProcessor.class);

    @Autowired
    private EventBus eventBus;

    @PostConstruct
    public void init() {
        eventBus.register(this);
    }

    @Subscribe
    public void processDeadEvent(DeadEvent deadEvent) {
        logger.error("DEADEVENT DETECTED:{}", deadEvent.getEvent().getClass());
    }
}

package org.acme.rules.micrometer.impl;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.concurrent.ConcurrentMap;

public class RuleMetrics extends AbstractMetrics {
    RuleMetrics(MeterRegistry registry, ConcurrentMap<Meter.Id, Object> gaugesTable) {
        super(registry, gaugesTable);
    }
}

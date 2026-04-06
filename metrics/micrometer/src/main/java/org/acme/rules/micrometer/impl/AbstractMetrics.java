package org.acme.rules.micrometer.impl;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.vertx.micrometer.Label;
import io.vertx.micrometer.MetricsDomain;
import io.vertx.micrometer.impl.MicrometerMetrics;
import io.vertx.micrometer.impl.meters.Counters;
import io.vertx.micrometer.impl.meters.Gauges;
import io.vertx.micrometer.impl.meters.Summaries;
import io.vertx.micrometer.impl.meters.Timers;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;

/**
 * Abstract class for metrics container.
 *
 * @author Joel Takvorian
 */
public abstract class AbstractMetrics implements MicrometerMetrics {

    protected final MeterRegistry registry;
    protected final String category;
    protected final ConcurrentMap<Meter.Id, Object> gaugesTable;

    AbstractMetrics(MeterRegistry registry, ConcurrentMap<Meter.Id, Object> gaugesTable) {
        this.registry = registry;
        this.gaugesTable = gaugesTable;
        this.category = null;
    }

    AbstractMetrics(MeterRegistry registry, String category, ConcurrentMap<Meter.Id, Object> gaugesTable) {
        this.registry = registry;
        this.category = category;
        this.gaugesTable = gaugesTable;
    }

    AbstractMetrics(MeterRegistry registry, MetricsDomain domain, ConcurrentMap<Meter.Id, Object> gaugesTable) {
        this.registry = registry;
        this.category = (domain == null) ? null : domain.toCategory();
        this.gaugesTable = gaugesTable;
    }

    /**
     * @return the Micrometer registry used with this measured object.
     */
    @Override
    public MeterRegistry registry() {
        return registry;
    }

    @Override
    public String baseName() {
        return category == null ? null : "vertx." + category + ".";
    }

    Counters counters(String name, String description, Label... keys) {
        return new Counters(baseName() + name, description, registry, keys);
    }

    Gauges<LongAdder> longGauges(String name, String description, Label... keys) {
        return new Gauges<>(gaugesTable, baseName() + name, description, LongAdder::new, LongAdder::doubleValue, registry, keys);
    }

    Gauges<AtomicReference<Double>> doubleGauges(String name, String description, Label... keys) {
        return new Gauges<>(gaugesTable, baseName() + name, description, () -> new AtomicReference<>(0d), AtomicReference::get, registry, keys);
    }

    Summaries summaries(String name, String description, Label... keys) {
        return new Summaries(baseName() + name, description, registry, keys);
    }

    Timers timers(String name, String description, Label... keys) {
        return new Timers(baseName() + name, description, registry, keys);
    }
}
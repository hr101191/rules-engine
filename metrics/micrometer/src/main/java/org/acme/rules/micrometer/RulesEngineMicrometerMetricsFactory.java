package org.acme.rules.micrometer;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.metrics.MetricsOptions;
import io.vertx.core.spi.VertxMetricsFactory;
import io.vertx.core.spi.metrics.VertxMetrics;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.backends.BackendRegistries;
import io.vertx.micrometer.backends.BackendRegistry;
import io.vertx.micrometer.impl.VertxMetricsImpl;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class RulesEngineMicrometerMetricsFactory implements VertxMetricsFactory {

    private static final Map<MeterRegistry, ConcurrentHashMap<Meter.Id, Object>> tables = new WeakHashMap<>(1);

    private final MeterRegistry micrometerRegistry;

    public RulesEngineMicrometerMetricsFactory() {
        this(null);
    }

    /**
     * Build a factory passing the Micrometer MeterRegistry to be used by Vert.x.
     *
     * This is useful in several scenarios, such as:
     * <ul>
     *   <li>if there is already a MeterRegistry used in the application
     * that should be used by Vert.x as well.</li>
     *   <li>to define some backend configuration that is not covered in this module
     * (example: reporting to non-covered backends such as New Relic)</li>
     *   <li>to use Micrometer's CompositeRegistry</li>
     * </ul>
     *
     * This setter is mutually exclusive with setInfluxDbOptions/setPrometheusOptions/setJmxMetricsOptions
     * and takes precedence over them.
     *
     * @param micrometerRegistry the registry to use
     */
    public RulesEngineMicrometerMetricsFactory(MeterRegistry micrometerRegistry) {
        this.micrometerRegistry = micrometerRegistry;
    }

    @Override
    public VertxMetrics metrics(VertxOptions vertxOptions) {
        MetricsOptions metricsOptions = vertxOptions.getMetricsOptions();
        MicrometerMetricsOptions options;
        if (metricsOptions instanceof MicrometerMetricsOptions) {
            options = (MicrometerMetricsOptions) metricsOptions;
        } else {
            options = new MicrometerMetricsOptions(metricsOptions.toJson());
        }
        MeterRegistry meterRegistry = this.micrometerRegistry;
        if (meterRegistry == null) {
            meterRegistry = options.getMicrometerRegistry();
        }
        BackendRegistry backendRegistry = BackendRegistries.setupBackend(options, meterRegistry);
        ConcurrentHashMap<Meter.Id, Object> gaugesTable;
        synchronized (tables) {
            gaugesTable = tables.computeIfAbsent(backendRegistry.getMeterRegistry(), mr -> new ConcurrentHashMap<>());
        }
        VertxMetricsImpl metrics = new VertxMetricsImpl(options, backendRegistry, gaugesTable);
        metrics.init();
        return metrics;
    }

    @Override
    public MetricsOptions newOptions(MetricsOptions options) {
        if (options instanceof MicrometerMetricsOptions) {
            return new MicrometerMetricsOptions((MicrometerMetricsOptions) options);
        } else {
            return VertxMetricsFactory.super.newOptions(options);
        }
    }

    @Override
    public MetricsOptions newOptions() {
        return newOptions((JsonObject) null);
    }

    @Override
    public MetricsOptions newOptions(JsonObject jsonObject) {
        return jsonObject == null ? new MicrometerMetricsOptions() : new MicrometerMetricsOptions(jsonObject);
    }

}

package org.acme.rules.task;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.impl.cpu.CpuCoreSensor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

@Slf4j
public class TaskExecutorImpl implements TaskExecutor {

    private final EventBus eventBus;

    @Builder
    public TaskExecutorImpl(Vertx vertx) {
        this.eventBus = vertx.eventBus();
        try {
            this.eventBus.registerCodec(ExecutableTaskEventBusCodec.INSTANCE);
            log.info("Codec [{}] is registered successfully with Vert.x EventBus.", ExecutableTaskEventBusCodec.INSTANCE.name());
        } catch (IllegalStateException ignored) {}
        try (ExecutorService executorService = Executors.newSingleThreadExecutor()) {
            vertx.deployVerticle(
                            TaskExecutionVerticle.class,
                            new DeploymentOptions()
                                    .setThreadingModel(majorVersionFromJavaSpecificationVersion() >= 21 ? ThreadingModel.VIRTUAL_THREAD : ThreadingModel.WORKER)
                                    .setInstances(Math.max(CpuCoreSensor.availableProcessors() * 2, 20))
                    )
                    .toCompletionStage()
                    .thenApplyAsync(Function.identity(), executorService)
                    .toCompletableFuture()
                    .join();
        }
    }

    @Override
    public <T> Future<T> execute(Callable<T> task) {
        return eventBus.<T>request("local-task-execution-event-bus-address", task, new DeliveryOptions().setLocalOnly(true).setCodecName(ExecutableTaskEventBusCodec.INSTANCE.name()))
                .compose(msg -> {
                    try {
                        T result = msg.body();
                        return Future.succeededFuture(result);
                    } catch (Exception ex) {
                        return Future.failedFuture(ex);
                    }
                });
    }

    @Override
    public <T> Future<List<T>> execute(List<? extends Callable<T>> tasks) {
        if (tasks == null) return Future.failedFuture("tasks cannot be null");
        List<Future<T>> futures = new ArrayList<>();
        for (Callable<T> task : tasks) {
            futures.add(execute(task));
        }
        return Future.join(futures)
                .compose(compositeFuture -> Future.succeededFuture(new ArrayList<>(compositeFuture.list())));
    }

    private int majorVersionFromJavaSpecificationVersion() {
        return majorVersion(System.getProperty("java.specification.version", "17"));
    }

    private int majorVersion(String javaSpecVersion) {
        String[] components = javaSpecVersion.split("\\.");
        int[] version = new int[components.length];
        for (int i = 0; i < components.length; i++) {
            version[i] = Integer.parseInt(components[i]);
        }
        if (version[0] == 1) {
            assert version[1] > 6;
            return version[1];
        } else {
            return version[0];
        }
    }
}

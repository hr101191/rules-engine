package org.acme.rules.task;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class TaskExecutionVerticle<T> extends AbstractVerticle {

    public void start(Promise<Void> startPromise) throws Exception {
        if (super.vertx.getOrCreateContext().isEventLoopContext()) {
            startPromise.fail("This verticle does not support Vert.x eventloop.");
        } else {
            super.vertx.eventBus()
                    .<Callable<T>>localConsumer("local-task-execution-event-bus-address")
                    .handler(new TaskExecutionHandler<>());
            startPromise.complete();
        }
    }

    private static class TaskExecutionHandler<T> implements Handler<Message<Callable<T>>> {

        @Override
        public void handle(Message<Callable<T>> message) {
            try {
                Callable<T> task = message.body();
                T result = task.call();
                message.reply(result, new DeliveryOptions().setLocalOnly(true).setCodecName(ExecutableTaskEventBusCodec.INSTANCE.name()));
            } catch (Exception ex) {
                log.error("Failed to execute task. Stacktrace: ", ex);
                message.fail(500, ex.getMessage());
            }
        }

    }

}

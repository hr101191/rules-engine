package org.acme.rules.task;

import io.vertx.core.Future;

import java.util.List;
import java.util.concurrent.Callable;

public interface TaskExecutor {

    static TaskExecutorImpl.TaskExecutorImplBuilder builder() {
        return new TaskExecutorImpl.TaskExecutorImplBuilder();
    }

    <T> Future<T> execute(Callable<T> task);

    <T> Future<List<T>> execute(List<? extends Callable<T>> tasks);

}

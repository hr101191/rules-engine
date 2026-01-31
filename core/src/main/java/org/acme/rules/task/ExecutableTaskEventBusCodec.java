package org.acme.rules.task;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

record ExecutableTaskEventBusCodec<T>(String name) implements MessageCodec<T, T> {

    public static final MessageCodec<?, ?> INSTANCE = new ExecutableTaskEventBusCodec<>(ExecutableTaskEventBusCodec.class.getName());

    @Override
    public void encodeToWire(Buffer buffer, T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T decodeFromWire(int i, Buffer buffer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T transform(T instance) {
        return instance;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }

}

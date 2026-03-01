package org.acme.rules.cel;

import dev.cel.runtime.CelRuntimeBuilder;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;

@VertxGen
@FunctionalInterface
public interface CelRuntimeBuilderCustomizer {

    @GenIgnore
    void customize(CelRuntimeBuilder celRuntimeBuilder);
    
}

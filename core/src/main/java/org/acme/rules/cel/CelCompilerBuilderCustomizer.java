package org.acme.rules.cel;

import dev.cel.compiler.CelCompilerBuilder;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;

@VertxGen
@FunctionalInterface
public interface CelCompilerBuilderCustomizer {

    @GenIgnore
    void customize(CelCompilerBuilder celCompilerBuilder);

}

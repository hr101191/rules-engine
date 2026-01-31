package org.acme.rules.cel;

import dev.cel.compiler.CelCompilerBuilder;

@FunctionalInterface
public interface CelCompilerBuilderCustomizer {

    void customize(CelCompilerBuilder celCompilerBuilder);

}

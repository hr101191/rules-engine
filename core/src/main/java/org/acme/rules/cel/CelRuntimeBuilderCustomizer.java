package org.acme.rules.cel;

import dev.cel.runtime.CelRuntimeBuilder;

@FunctionalInterface
public interface CelRuntimeBuilderCustomizer {

    void customize(CelRuntimeBuilder celRuntimeBuilder);
    
}

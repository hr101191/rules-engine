package org.acme.rules.cel;

import dev.cel.common.CelOptions;
import dev.cel.extensions.*;
import dev.cel.runtime.CelRuntimeBuilder;
import dev.cel.runtime.CelRuntimeFactory;
import org.acme.rules.cel.extension.ExtendedCelExtensions;

public class CelRuntimeBuilderContainer {

    public static final CelRuntimeBuilder INSTANCE = CelRuntimeFactory.standardCelRuntimeBuilder()
            .addLibraries(
                    CelExtensions.encoders(CelOptions.DEFAULT),
                    CelExtensions.lists(),
                    CelExtensions.math(CelOptions.DEFAULT),
                    CelExtensions.optional(),
                    CelExtensions.regex(),
                    CelExtensions.sets(CelOptions.DEFAULT),
                    CelExtensions.strings(),
                    CelExtensions.comprehensions(),
                    ExtendedCelExtensions.list(),
                    ExtendedCelExtensions.timestamp()
            );

}

package org.acme.rules.cel.extension;

import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.Int64Value;
import com.google.protobuf.Timestamp;
import dev.cel.checker.CelCheckerBuilder;
import dev.cel.common.CelFunctionDecl;
import dev.cel.common.CelOverloadDecl;
import dev.cel.common.types.ListType;
import dev.cel.common.types.SimpleType;
import dev.cel.compiler.CelCompilerLibrary;
import dev.cel.extensions.CelExtensionLibrary;
import dev.cel.runtime.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

@Immutable
public final class ExtendedCelTimestampExtensions implements CelCompilerLibrary, CelRuntimeLibrary, CelExtensionLibrary.FeatureSet {

    public enum Function {
        NOW(
                CelFunctionDecl.newFunctionDeclaration(
                "now",
                        CelOverloadDecl.newBuilder()
                                .setIsInstanceFunction(false)
                                .setOverloadId("ext_timestamp_now")
                                .setDoc("ext_timestamp_now")
                                .setResultType(SimpleType.TIMESTAMP)
                                .build()
                ),
                CelFunctionBinding.from("ext_timestamp_now", List.of(), args -> {
                    Instant now = Instant.now();
                    return Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()).build();
                })
        );

        private final CelFunctionDecl functionDecl;
        private final ImmutableSet<CelFunctionBinding> functionBindings;

        String getFunction() {
            return functionDecl.name();
        }

        Function(CelFunctionDecl functionDecl, CelFunctionBinding... functionBindings) {
            this.functionDecl = functionDecl;
            this.functionBindings = ImmutableSet.copyOf(functionBindings);
        }
    }

    private final ImmutableSet<ExtendedCelTimestampExtensions.Function> functions;

    ExtendedCelTimestampExtensions() {
        this(ImmutableSet.copyOf(ExtendedCelTimestampExtensions.Function.values()));
    }

    ExtendedCelTimestampExtensions(Set<ExtendedCelTimestampExtensions.Function> functions) {
        this.functions = ImmutableSet.copyOf(functions);
    }

    private static final CelExtensionLibrary<ExtendedCelTimestampExtensions> LIBRARY =
            new CelExtensionLibrary<ExtendedCelTimestampExtensions>() {
                private final ExtendedCelTimestampExtensions version0 = new ExtendedCelTimestampExtensions();

                @Override
                public String name() {
                    return "extended_lists";
                }

                @Override
                public ImmutableSet<ExtendedCelTimestampExtensions> versions() {
                    return ImmutableSet.of(version0);
                }
            };

    static CelExtensionLibrary<ExtendedCelTimestampExtensions> library() {
        return LIBRARY;
    }

    @Override
    public int version() {
        return 0;
    }

    @Override
    public ImmutableSet<CelFunctionDecl> functions() {
        return functions.stream().map(f -> f.functionDecl).collect(toImmutableSet());
    }

    @Override
    public void setCheckerOptions(CelCheckerBuilder checkerBuilder) {
        functions.forEach(function -> checkerBuilder.addFunctionDeclarations(function.functionDecl));
    }

    @Override
    public void setRuntimeOptions(CelRuntimeBuilder runtimeBuilder) {
        functions.forEach(function -> runtimeBuilder.addFunctionBindings(function.functionBindings));
    }

}
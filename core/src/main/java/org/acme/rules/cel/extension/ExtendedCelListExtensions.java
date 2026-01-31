package org.acme.rules.cel.extension;

import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.Int64Value;
import dev.cel.checker.CelCheckerBuilder;
import dev.cel.common.CelFunctionDecl;
import dev.cel.common.CelOverloadDecl;
import dev.cel.common.types.ListType;
import dev.cel.common.types.SimpleType;
import dev.cel.compiler.CelCompilerLibrary;
import dev.cel.extensions.CelExtensionLibrary;
import dev.cel.runtime.*;

import java.util.*;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

@Immutable
public final class ExtendedCelListExtensions implements CelCompilerLibrary, CelRuntimeLibrary, CelExtensionLibrary.FeatureSet {

    public enum Function {
        SUM(
                CelFunctionDecl.newFunctionDeclaration(
                "sum",
                        CelOverloadDecl.newMemberOverload(
                                "ext_list_sum",
                                "ext_list_sum",
                                SimpleType.DYN,
                                ListType.create(SimpleType.DYN)
                        )
                ),
                CelFunctionBinding.from("ext_list_sum", List.class, ExtendedCelListExtensions::listSum)
        ),
        MIN(
                CelFunctionDecl.newFunctionDeclaration(
                        "min",
                        CelOverloadDecl.newMemberOverload(
                                "ext_list_min",
                                "ext_list_min",
                                SimpleType.DYN,
                                ListType.create(SimpleType.DYN)
                        )
                ),
                CelFunctionBinding.from("ext_list_min", List.class, ExtendedCelListExtensions::listMin)
        ),
        MAX(
                CelFunctionDecl.newFunctionDeclaration(
                        "max",
                        CelOverloadDecl.newMemberOverload(
                                "ext_list_max",
                                "ext_list_max",
                                SimpleType.DYN,
                                ListType.create(SimpleType.DYN)
                        )
                ),
                CelFunctionBinding.from("ext_list_max", List.class, ExtendedCelListExtensions::listMax)
        ),
        INDEX_OF(
                CelFunctionDecl.newFunctionDeclaration(
                        "indexOf",
                        CelOverloadDecl.newMemberOverload(
                                "ext_list_index_of",
                                "ext_list_index_of",
                                SimpleType.INT,
                                ListType.create(SimpleType.DYN),
                                SimpleType.DYN
                        )
                ),
                CelFunctionBinding.from("ext_list_sum", List.class, Object.class, ExtendedCelListExtensions::listIndexOf)
        ),
        LAST_INDEX_OF(
                CelFunctionDecl.newFunctionDeclaration(
                        "lastIndexOf",
                        CelOverloadDecl.newMemberOverload(
                                "ext_list_last_index_of",
                                "ext_list_last_index_of",
                                SimpleType.INT,
                                ListType.create(SimpleType.DYN),
                                SimpleType.DYN
                        )
                ),
                CelFunctionBinding.from("ext_list_last_index_of", List.class, Object.class, ExtendedCelListExtensions::listLastIndexOf)
        ),
        CONTAINS(
                CelFunctionDecl.newFunctionDeclaration(
                        "contains",
                        CelOverloadDecl.newMemberOverload(
                                "ext_list_contains",
                                "ext_list_contains",
                                SimpleType.BOOL,
                                ListType.create(SimpleType.DYN),
                                SimpleType.DYN
                        )
                ),
                CelFunctionBinding.from("ext_list_contains", List.class, Object.class, ExtendedCelListExtensions::listContains)
        ),
        CONTAINS_ANY(
                CelFunctionDecl.newFunctionDeclaration(
                        "containsAny",
                        CelOverloadDecl.newMemberOverload(
                                "ext_list_contains_any",
                                "ext_list_contains_any",
                                SimpleType.BOOL,
                                ListType.create(SimpleType.DYN),
                                ListType.create(SimpleType.DYN)
                        )
                ),
                CelFunctionBinding.from("ext_list_contains_any", List.class, List.class, ExtendedCelListExtensions::listContainsAny)
        ),
        CONTAINS_ALL(
                CelFunctionDecl.newFunctionDeclaration(
                        "containsAll",
                        CelOverloadDecl.newMemberOverload(
                                "ext_list_contains_all",
                                "ext_list_contains_all",
                                SimpleType.BOOL,
                                ListType.create(SimpleType.DYN),
                                ListType.create(SimpleType.DYN)
                        )
                ),
                CelFunctionBinding.from("ext_list_contains_all", List.class, List.class, ExtendedCelListExtensions::listContainsAll)
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

    private final ImmutableSet<ExtendedCelListExtensions.Function> functions;

    ExtendedCelListExtensions() {
        this(ImmutableSet.copyOf(ExtendedCelListExtensions.Function.values()));
    }

    ExtendedCelListExtensions(Set<ExtendedCelListExtensions.Function> functions) {
        this.functions = ImmutableSet.copyOf(functions);
    }

    private static final CelExtensionLibrary<ExtendedCelListExtensions> LIBRARY =
            new CelExtensionLibrary<ExtendedCelListExtensions>() {
                private final ExtendedCelListExtensions version0 = new ExtendedCelListExtensions();

                @Override
                public String name() {
                    return "extended_lists";
                }

                @Override
                public ImmutableSet<ExtendedCelListExtensions> versions() {
                    return ImmutableSet.of(version0);
                }
            };

    static CelExtensionLibrary<ExtendedCelListExtensions> library() {
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

    private static Object listSum(List<?> list) {
        boolean listContainsDouble = false;
        long longResult = 0L;
        double doubleResult = 0D;
        for (Object item : list) {
            if (item instanceof Number number) {
                if (number instanceof Double) {
                    listContainsDouble = true;
                    doubleResult += number.doubleValue();
                } else {
                    long value = number.longValue();
                    longResult += value;
                }
            }
        }
        return listContainsDouble ? doubleResult + longResult : Int64Value.of(longResult);
    }

    private static Object listMin(List<?> list) throws CelEvaluationException {
        List<Number> numbers = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof Number number) {
                numbers.add(number);
            } else {
                throw CelEvaluationExceptionBuilder.newBuilder("listMin failure: List contains non numeric element").build();
            }
        }
        return numbers.stream()
                .min(Comparator.comparing(Number::doubleValue))
                .map(n -> n instanceof Double ? n.doubleValue() : Int64Value.of(n.longValue()))
                .orElse(Int64Value.of(0L));
    }

    private static Object listMax(List<?> list) throws CelEvaluationException {
        List<Number> numbers = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof Number number) {
                numbers.add(number);
            } else {
                throw CelEvaluationExceptionBuilder.newBuilder("listMin failure: List contains non numeric element").build();
            }
        }
        return numbers.stream()
                .max(Comparator.comparing(Number::doubleValue))
                .map(n -> n instanceof Double ? n.doubleValue() : Int64Value.of(n.longValue()))
                .orElse(Int64Value.of(0L));
    }

    private static Object listIndexOf(List<?> list, Object object) {
        return list.indexOf(object);
    }

    private static Object listLastIndexOf(List<?> list, Object object) {
        return list.lastIndexOf(object);
    }

    private static Object listContains(List<Object> list, Object object) {
        return list.contains(object);
    }

    private static Object listContainsAny(List<Object> list, List<Object> valuesToCheckFor) {
        return list.stream().anyMatch(valuesToCheckFor::contains);
    }

    private static Object listContainsAll(List<Object> list, List<Object> valuesToCheckFor) {
        return new HashSet<>(list).containsAll(new HashSet<>(valuesToCheckFor));
    }

}
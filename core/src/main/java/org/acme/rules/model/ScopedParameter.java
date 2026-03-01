package org.acme.rules.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.json.annotations.JsonGen;
import io.vertx.core.json.JsonObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

@DataObject
@JsonGen(inheritConverter = true)
@Value.Immutable
@Value.Modifiable
@Value.Style(
        nullableAnnotation = "org.jspecify.annotations.Nullable",
        jdk9Collections = true,
        builtinContainerAttributes = false,
        passAnnotations = {
                DataObject.class,
                JsonGen.class
        },
        get = {
                "get*",
                "is*"
        }
)
@JsonSerialize(as = ImmutableScopedParameter.class)
@JsonDeserialize(as = ImmutableScopedParameter.class)
public interface ScopedParameter {

    static ImmutableScopedParameter.Builder builder() {
        return ImmutableScopedParameter.builder();
    }

    @NotBlank
    String getName();

    @NotBlank
    String getExpression();

    @NotNull
    @Value.Default
    @Schema(defaultValue = "CEL")
    default RuleExpressionType getExpressionType() {
        return RuleExpressionType.CEL;
    }

    @Value.Auxiliary
    @GenIgnore
    @Schema(hidden = true)
    default boolean isImmutable() {
        return this instanceof ImmutableScopedParameter;
    }

    default ScopedParameter toImmutable() {
        return ImmutableScopedParameter.copyOf(this);
    }

    default ScopedParameter toModifiable() {
        return ModifiableScopedParameter.create().from(this);
    }

    default JsonObject toJson() {
        ModifiableScopedParameter modifiableScopedParameter = ModifiableScopedParameter.create().from(this);
        JsonObject jsonObject = new JsonObject();
        ModifiableScopedParameterConverter.toJson(modifiableScopedParameter, jsonObject);
        return jsonObject;
    }

    class JsonFactory {

        public static JsonObject serialize(ScopedParameter scopedParameter) {
            ModifiableScopedParameter modifiableScopedParameter = ModifiableScopedParameter.create().from(scopedParameter);
            JsonObject jsonObject = new JsonObject();
            ModifiableScopedParameterConverter.toJson(modifiableScopedParameter, jsonObject);
            return jsonObject;
        }

        public static ScopedParameter deserialize(JsonObject jsonObject) {
            ModifiableScopedParameter modifiableScopedParameter = ModifiableScopedParameter.create();
            ModifiableScopedParameterConverter.fromJson(jsonObject, modifiableScopedParameter);
            return modifiableScopedParameter;
        }

    }

}

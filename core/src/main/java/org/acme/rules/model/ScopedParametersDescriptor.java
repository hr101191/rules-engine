package org.acme.rules.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.json.annotations.JsonGen;
import io.vertx.core.json.JsonObject;
import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;
import org.jspecify.annotations.Nullable;

import java.util.List;

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
@JsonSerialize(as = ImmutableScopedParametersDescriptor.class)
@JsonDeserialize(as = ImmutableScopedParametersDescriptor.class)
public interface ScopedParametersDescriptor {

    static ImmutableScopedParametersDescriptor.Builder builder() {
        return ImmutableScopedParametersDescriptor.builder();
    }

    @Nullable
    RuleExpressionType getExpressionType();

    @Nullable
    List<@Valid ScopedParameter> getParameters();

    @Value.Auxiliary
    @GenIgnore
    @Schema(hidden = true)
    default boolean isImmutable() {
        return this instanceof ImmutableScopedParametersDescriptor;
    }

    default ScopedParametersDescriptor toImmutable() {
        return ImmutableScopedParametersDescriptor.copyOf(this);
    }

    default ScopedParametersDescriptor toModifiable() {
        return ModifiableScopedParametersDescriptor.create().from(this);
    }

    default JsonObject toJson() {
        ModifiableScopedParametersDescriptor modifiableScopedParametersDescriptor = ModifiableScopedParametersDescriptor.create().from(this);
        JsonObject jsonObject = new JsonObject();
        ModifiableScopedParametersDescriptorConverter.toJson(modifiableScopedParametersDescriptor, jsonObject);
        return jsonObject;
    }

    class JsonFactory {

        public static JsonObject serialize(ScopedParametersDescriptor scopedParametersDescriptor) {
            ModifiableScopedParametersDescriptor modifiableScopedParametersDescriptor = ModifiableScopedParametersDescriptor.create().from(scopedParametersDescriptor);
            JsonObject jsonObject = new JsonObject();
            ModifiableScopedParametersDescriptorConverter.toJson(modifiableScopedParametersDescriptor, jsonObject);
            return jsonObject;
        }

        public static ScopedParametersDescriptor deserialize(JsonObject jsonObject) {
            ModifiableScopedParametersDescriptor modifiableScopedParametersDescriptor = ModifiableScopedParametersDescriptor.create();
            ModifiableScopedParametersDescriptorConverter.fromJson(jsonObject, modifiableScopedParametersDescriptor);
            return modifiableScopedParametersDescriptor;
        }

    }

}

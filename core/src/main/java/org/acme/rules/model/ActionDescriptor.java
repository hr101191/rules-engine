package org.acme.rules.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.json.annotations.JsonGen;
import io.vertx.core.json.JsonObject;
import jakarta.validation.constraints.NotEmpty;
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
@JsonSerialize(as = ImmutableActionDescriptor.class)
@JsonDeserialize(as = ImmutableActionDescriptor.class)
public interface ActionDescriptor {

    static ImmutableActionDescriptor.Builder builder() {
        return ImmutableActionDescriptor.builder();
    }

    @NotEmpty
    String getActionName();

    @Value.Auxiliary
    @GenIgnore
    @Schema(hidden = true)
    default boolean isImmutable() {
        return this instanceof ImmutableActionDescriptor;
    }

    default ActionDescriptor toImmutable() {
        return ImmutableActionDescriptor.copyOf(this);
    }

    default ActionDescriptor toModifiable() {
        return ModifiableActionDescriptor.create().from(this);
    }

    default JsonObject toJson() {
        ModifiableActionDescriptor modifiableActionDescriptor = ModifiableActionDescriptor.create().from(this);
        JsonObject jsonObject = new JsonObject();
        ModifiableActionDescriptorConverter.toJson(modifiableActionDescriptor, jsonObject);
        return jsonObject;
    }

    class JsonFactory {

        public static JsonObject serialize(ActionDescriptor actionDescriptor) {
            ModifiableActionDescriptor modifiableActionDescriptor = ModifiableActionDescriptor.create().from(actionDescriptor);
            JsonObject jsonObject = new JsonObject();
            ModifiableActionDescriptorConverter.toJson(modifiableActionDescriptor, jsonObject);
            return jsonObject;
        }

        public static ActionDescriptor deserialize(JsonObject jsonObject) {
            ModifiableActionDescriptor modifiableActionDescriptor = ModifiableActionDescriptor.create();
            ModifiableActionDescriptorConverter.fromJson(jsonObject, modifiableActionDescriptor);
            return modifiableActionDescriptor;
        }

    }

}

package org.acme.rules.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.json.annotations.JsonGen;
import io.vertx.core.json.JsonObject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

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
@JsonSerialize(as = ImmutableCompositeRuleDescriptor.class)
@JsonDeserialize(as = ImmutableCompositeRuleDescriptor.class)
public interface CompositeRuleDescriptor extends RuleDescriptor {

    static ImmutableCompositeRuleDescriptor.Builder builder() {
        return ImmutableCompositeRuleDescriptor.builder();
    }

    List<@Valid RuleDescriptor> getRules();

    @NotNull
    Operator getOperator();

    @Value.Auxiliary
    @GenIgnore
    @Schema(hidden = true)
    default boolean isImmutable() {
        return this instanceof ImmutableCompositeRuleDescriptor;
    }

    default CompositeRuleDescriptor toImmutable() {
        return ImmutableCompositeRuleDescriptor.copyOf(this);
    }

    default CompositeRuleDescriptor toModifiable() {
        return ModifiableCompositeRuleDescriptor.create().from(this);
    }

    default JsonObject toJson() {
        ModifiableCompositeRuleDescriptor modifiableCompositeRuleDescriptor = ModifiableCompositeRuleDescriptor.create().from(this);
        JsonObject jsonObject = new JsonObject();
        ModifiableCompositeRuleDescriptorConverter.toJson(modifiableCompositeRuleDescriptor, jsonObject);
        return jsonObject;
    }

    class JsonFactory {

        public static JsonObject serialize(CompositeRuleDescriptor compositeRuleDescriptor) {
            ModifiableCompositeRuleDescriptor modifiableCompositeRuleDescriptor = ModifiableCompositeRuleDescriptor.create().from(compositeRuleDescriptor);
            JsonObject jsonObject = new JsonObject();
            ModifiableCompositeRuleDescriptorConverter.toJson(modifiableCompositeRuleDescriptor, jsonObject);
            return jsonObject;
        }

        public static CompositeRuleDescriptor deserialize(JsonObject jsonObject) {
            ModifiableCompositeRuleDescriptor modifiableCompositeRuleDescriptor = ModifiableCompositeRuleDescriptor.create();
            ModifiableCompositeRuleDescriptorConverter.fromJson(jsonObject, modifiableCompositeRuleDescriptor);
            return modifiableCompositeRuleDescriptor;
        }

    }

}

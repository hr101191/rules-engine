package org.acme.rules.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.json.annotations.JsonGen;
import io.vertx.core.json.JsonObject;
import jakarta.validation.constraints.NotBlank;
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
@JsonSerialize(as = ImmutableExpressionRuleDescriptor.class)
@JsonDeserialize(as = ImmutableExpressionRuleDescriptor.class)
public interface ExpressionRuleDescriptor extends RuleDescriptor {

    static ImmutableExpressionRuleDescriptor.Builder builder() {
        return ImmutableExpressionRuleDescriptor.builder();
    }

    @NotBlank
    RuleExpressionType getRuleExpressionType();

    @NotBlank
    String getExpression();

    @Value.Auxiliary
    @GenIgnore
    @Schema(hidden = true)
    default boolean isImmutable() {
        return this instanceof ImmutableExpressionRuleDescriptor;
    }

    default ExpressionRuleDescriptor toImmutable() {
        return ImmutableExpressionRuleDescriptor.copyOf(this);
    }

    default ExpressionRuleDescriptor toModifiable() {
        return ModifiableExpressionRuleDescriptor.create().from(this);
    }

    default JsonObject toJson() {
        ModifiableExpressionRuleDescriptor modifiableExpressionRuleDescriptor = ModifiableExpressionRuleDescriptor.create().from(this);
        JsonObject jsonObject = new JsonObject();
        ModifiableExpressionRuleDescriptorConverter.toJson(modifiableExpressionRuleDescriptor, jsonObject);
        return jsonObject;
    }

    class JsonFactory {

        public static JsonObject serialize(ExpressionRuleDescriptor expressionRuleDescriptor) {
            ModifiableExpressionRuleDescriptor modifiableExpressionRuleDescriptor = ModifiableExpressionRuleDescriptor.create().from(expressionRuleDescriptor);
            JsonObject jsonObject = new JsonObject();
            ModifiableExpressionRuleDescriptorConverter.toJson(modifiableExpressionRuleDescriptor, jsonObject);
            return jsonObject;
        }

        public static ExpressionRuleDescriptor deserialize(JsonObject jsonObject) {
            ModifiableExpressionRuleDescriptor modifiableExpressionRuleDescriptor = ModifiableExpressionRuleDescriptor.create();
            ModifiableExpressionRuleDescriptorConverter.fromJson(jsonObject, modifiableExpressionRuleDescriptor);
            return modifiableExpressionRuleDescriptor;
        }

    }

}

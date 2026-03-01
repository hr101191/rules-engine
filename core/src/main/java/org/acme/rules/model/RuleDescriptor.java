package org.acme.rules.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.json.JsonObject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.DiscriminatorMapping;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@DataObject
//@JsonTypeInfo(use=JsonTypeInfo.Id.NONE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value =  ExpressionRuleDescriptor.class, name = "EXPRESSION"),
        @JsonSubTypes.Type(value =  CompositeRuleDescriptor.class, name = "COMPOSITE")
})
@Schema(
        discriminatorProperty = "type",
        discriminatorMapping = {
                @DiscriminatorMapping(value = "EXPRESSION", schema = ExpressionRuleDescriptor.class),
                @DiscriminatorMapping(value = "COMPOSITE", schema = CompositeRuleDescriptor.class)
        },
        oneOf = {
                ExpressionRuleDescriptor.class,
                CompositeRuleDescriptor.class
        }
)
public interface RuleDescriptor {

    @NotBlank
    String getRuleName();

    @Nullable
    @Valid
    ScopedParametersDescriptor getLocalParametersDescriptor();

    @Value.Default.Boolean(true)
    @Schema(defaultValue = "true")
    boolean isEnabled();

    @Nullable
    List<@Valid ActionDescriptor> getActions();

    @Nullable
    Map<String, Object> getMetadata();

    @Value.Auxiliary
    @GenIgnore
    @Schema(hidden = true)
    default boolean isImmutable() {
        return this instanceof ImmutableExpressionRuleDescriptor || this instanceof ImmutableCompositeRuleDescriptor;
    }

    default RuleDescriptor toImmutable() {
        if (this instanceof ExpressionRuleDescriptor expressionRuleDescriptor) {
            return ImmutableExpressionRuleDescriptor.copyOf(expressionRuleDescriptor);
        } else if (this instanceof CompositeRuleDescriptor compositeRuleDescriptor) {
            return ImmutableCompositeRuleDescriptor.copyOf(compositeRuleDescriptor);
        }
        throw new IllegalArgumentException("Unknown rule descriptor type: " + this.getClass().getName());
    }

    default RuleDescriptor toModifiable() {
        if (this instanceof ExpressionRuleDescriptor expressionRuleDescriptor) {
            return ModifiableExpressionRuleDescriptor.create().from(expressionRuleDescriptor);
        } else if (this instanceof CompositeRuleDescriptor compositeRuleDescriptor) {
            return ModifiableCompositeRuleDescriptor.create().from(compositeRuleDescriptor);
        }
        throw new IllegalArgumentException("Unknown rule descriptor type: " + this.getClass().getName());
    }

    default JsonObject toJson() {
        if (this instanceof ExpressionRuleDescriptor expressionRuleDescriptor) {
            return expressionRuleDescriptor.toJson();
        } else if (this instanceof CompositeRuleDescriptor compositeRuleDescriptor) {
            return compositeRuleDescriptor.toJson();
        }
        throw new IllegalArgumentException("Unknown rule descriptor type: " + this.getClass().getName());
    }

    class JsonFactory {

        public static JsonObject serialize(RuleDescriptor ruleDescriptor) {
            if (ruleDescriptor instanceof ExpressionRuleDescriptor expressionRuleDescriptor) {
                return expressionRuleDescriptor.toJson();
            } else if (ruleDescriptor instanceof CompositeRuleDescriptor compositeRuleDescriptor) {
                return compositeRuleDescriptor.toJson();
            }
            throw new IllegalArgumentException("Unknown rule descriptor type: " + ruleDescriptor.getClass().getName());
        }

        public static RuleDescriptor deserialize(JsonObject jsonObject) {
            if (jsonObject.containsKey("type")) {
                String type = jsonObject.getString("type");
                if ("EXPRESSION".equals(type)) {
                    return ExpressionRuleDescriptor.JsonFactory.deserialize(jsonObject);
                } else if ("COMPOSITE".equals(type)) {
                    return CompositeRuleDescriptor.JsonFactory.deserialize(jsonObject);
                } else {
                    throw new IllegalArgumentException("Unknown rule descriptor type: " + type);
                }
            }
            if (jsonObject.containsKey("expression")) {
                return ExpressionRuleDescriptor.JsonFactory.deserialize(jsonObject);
            }
            return CompositeRuleDescriptor.JsonFactory.deserialize(jsonObject);
        }

    }

}


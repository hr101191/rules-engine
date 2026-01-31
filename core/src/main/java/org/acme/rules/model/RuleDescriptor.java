package org.acme.rules.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.eclipse.microprofile.openapi.annotations.media.DiscriminatorMapping;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

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
public sealed interface RuleDescriptor permits ExpressionRuleDescriptor, CompositeRuleDescriptor {

    @Schema(hidden = true)
    String getRuleName();

    @Schema(hidden = true)
    ScopedParametersDescriptor getLocalParametersDescriptor();

    @Schema(hidden = true)
    boolean isEnabled();

    @Schema(hidden = true)
    List<ActionDescriptor> getActions();

    @Schema(hidden = true)
    RuleTraceMetadata getRuleTraceMetadata();
}

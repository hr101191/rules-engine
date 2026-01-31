package org.acme.rules.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public final class ExpressionRuleDescriptor implements RuleDescriptor {

    @NotBlank
    private String ruleName;

    @Valid
    private ScopedParametersDescriptor localParametersDescriptor;

    @NotNull
    private RuleExpressionType ruleExpressionType;

    @NotBlank
    private String expression;

    private boolean enabled;

    private List<@Valid ActionDescriptor> actions;

    private RuleTraceMetadata ruleTraceMetadata;

}

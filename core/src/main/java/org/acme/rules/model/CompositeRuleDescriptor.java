package org.acme.rules.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public final class CompositeRuleDescriptor implements RuleDescriptor{

    @NotBlank
    private String ruleName;

    @Valid
    private ScopedParametersDescriptor localParametersDescriptor;

    @NotNull
    private Operator operator;

    @NotEmpty
    private List<@Valid RuleDescriptor> rules;

    private boolean enabled;

    private List<@Valid ActionDescriptor> actions;

    private RuleTraceMetadata ruleTraceMetadata;

}

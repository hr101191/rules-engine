package org.acme.rules.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ScopedParameter {

    @NotBlank
    private String name;

    @NotBlank
    private String expression;

    @NotNull
    @Builder.Default
    private RuleExpressionType expressionType = RuleExpressionType.CEL;

}

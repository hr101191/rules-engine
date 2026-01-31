package org.acme.rules.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ScopedParametersDescriptor {

    @NotEmpty
    private RuleExpressionType expressionType;

    private List<@Valid ScopedParameter> parameters;

}

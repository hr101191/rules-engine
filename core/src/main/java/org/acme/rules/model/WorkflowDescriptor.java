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
public class WorkflowDescriptor {

    @NotEmpty
    private String workflowName;

    private String description;

    private ScopedParametersDescriptor globalParametersDescriptor;

    private List<@Valid RuleDescriptor> rules;

    private List<String> workflowsToInject;

    private boolean enabled;

}

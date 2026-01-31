package org.acme.rules.workflow;

import dev.cel.runtime.CelRuntime;
import lombok.Builder;
import lombok.Getter;
import org.acme.rules.cel.CelCompilerBuilderCustomizer;
import org.acme.rules.compiler.RuleCompiler;
import org.acme.rules.model.RuleDescriptor;
import org.acme.rules.model.WorkflowDescriptor;
import org.acme.rules.parameter.ParameterExtractor;
import org.acme.rules.rule.Rule;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class Workflow {

    @Getter
    private final String realm;

    @Getter
    private final WorkflowDescriptor workflowDescriptor;

    @Getter
    private final long lastUpdated;

    @Getter
    private final ParameterExtractor parameterExtractor;

    @Getter
    private final List<Rule> rules;

    @Builder
    public Workflow(@NonNull String realm, @NonNull WorkflowDescriptor workflowDescriptor, long lastUpdated, @NonNull CelRuntime celRuntime, List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers) {
        this.realm = Objects.requireNonNull(realm);
        this.workflowDescriptor = Objects.requireNonNull(workflowDescriptor);
        this.lastUpdated = lastUpdated;
        List<String> globalParamNames = new ArrayList<>();
        if (workflowDescriptor.getGlobalParametersDescriptor() != null) {
            workflowDescriptor.getGlobalParametersDescriptor().getParameters().forEach(scopedParameter -> globalParamNames.add(scopedParameter.getName()));
            this.parameterExtractor = ParameterExtractor.buildParameterExtractor(workflowDescriptor.getGlobalParametersDescriptor(), celRuntime, celCompilerBuilderCustomizers);
        } else {
            this.parameterExtractor = null;
        }
        this.rules = new ArrayList<>();
        for (RuleDescriptor ruleDescriptor : workflowDescriptor.getRules()) {
            rules.add(RuleCompiler.INSTANCE.compile(ruleDescriptor, celRuntime, celCompilerBuilderCustomizers, globalParamNames));
        }
    }

    public Map<String, Object> extractGlobalParameters(Map<String, Object> input) {
        if (workflowDescriptor.getGlobalParametersDescriptor() != null && parameterExtractor != null) {
            return parameterExtractor.extract(input, new HashMap<>());
        } else {
            return new HashMap<>();
        }
    }

    public boolean isEnabled() {
        return workflowDescriptor.isEnabled();
    }
}

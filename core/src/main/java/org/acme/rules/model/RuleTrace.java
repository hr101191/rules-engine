package org.acme.rules.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Getter
@Setter
@ToString
public class RuleTrace {

    private String ruleName;
    private boolean result;
    private boolean shortCircuited;
    private long durationNanos;
    private RuleExecutionStatus status;
    private Map<String, Object> metadata;
    private List<RuleTrace> children;

    public RuleTrace(String ruleName) {
        this.ruleName = ruleName;
    }

    public Stream<RuleTrace> flattened() {
        return Stream.concat(Stream.of(this), children == null ? Stream.empty() : children.stream().flatMap(RuleTrace::flattened));
    }

}

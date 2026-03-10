package org.acme.rules.rule;

import io.vertx.codegen.annotations.VertxGen;
import org.acme.rules.model.RuleExecutionStatus;
import org.acme.rules.model.RuleTrace;

@VertxGen
public interface EvaluationContext {


    static EvaluationContext create() {
        return new EvaluationContextImpl();
    }

    void enterRule(String ruleName);

    RuleTrace exitRule(boolean result, long durationNanos, RuleExecutionStatus status);

    RuleTrace exitRule(boolean result, boolean shortCircuited, long durationNanos, RuleExecutionStatus status);

    RuleTrace getRootTrace();

    RuleTrace getCurrentTrace();
}

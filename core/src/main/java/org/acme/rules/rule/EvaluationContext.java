package org.acme.rules.rule;

import org.acme.rules.model.RuleExecutionStatus;
import org.acme.rules.model.RuleTrace;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class EvaluationContext {

    private final Deque<RuleTrace> stack;
    private RuleTrace root;

    public static EvaluationContext create() {
        return new EvaluationContext();
    }

    public EvaluationContext() {
        this.stack = new ArrayDeque<>();
    }

    void enterRule(String ruleName) {
        RuleTrace ruleTrace = new RuleTrace(ruleName);
        if (stack.isEmpty()) {
            root = ruleTrace;
        } else {
            RuleTrace firstElementFromStack = stack.peek();
            if (firstElementFromStack.getChildren() == null) {
                firstElementFromStack.setChildren(new ArrayList<>());
            }
            firstElementFromStack.getChildren().add(ruleTrace);
        }
        stack.push(ruleTrace);
    }

    RuleTrace exitRule(boolean result, long durationNanos, RuleExecutionStatus status) {
        return exitRule(result, false, durationNanos, status);
    }

    RuleTrace exitRule(boolean result, boolean shortCircuited, long durationNanos, RuleExecutionStatus status) {
        RuleTrace ruleTrace = stack.pop();
        ruleTrace.setResult(result);
        ruleTrace.setShortCircuited(shortCircuited);
        ruleTrace.setDurationNanos(durationNanos);
        ruleTrace.setStatus(status);
        return ruleTrace;
    }

    RuleTrace getRootTrace() {
        return root;
    }

    RuleTrace getCurrentTrace() {
        return stack.peek();
    }

}

package org.acme.rules.rule;

import org.acme.rules.model.ModifiableRuleTrace;
import org.acme.rules.model.RuleExecutionStatus;
import org.acme.rules.model.RuleTrace;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class EvaluationContextImpl implements EvaluationContext {

    private final Deque<ModifiableRuleTrace> stack;
    private RuleTrace root;

    public EvaluationContextImpl() {
        this.stack = new ArrayDeque<>();
    }

    @Override
    public void enterRule(String ruleName) {
        ModifiableRuleTrace ruleTrace = ModifiableRuleTrace.create();
        ruleTrace.setRuleName(ruleName);
        if (stack.isEmpty()) {
            root = ruleTrace;
        } else {
            ModifiableRuleTrace firstElementFromStack = stack.peek();
            if (firstElementFromStack.getChildren() == null) {
                firstElementFromStack.setChildren(new ArrayList<>());
            }
            firstElementFromStack.getChildren().add(ruleTrace);
        }
        stack.push(ruleTrace);
    }

    @Override
    public RuleTrace exitRule(boolean result, long durationNanos, RuleExecutionStatus status) {
        return exitRule(result, false, durationNanos, status);
    }

    @Override
    public RuleTrace exitRule(boolean result, boolean shortCircuited, long durationNanos, RuleExecutionStatus status) {
        ModifiableRuleTrace ruleTrace = stack.pop();
        ruleTrace.setResult(result);
        ruleTrace.setShortCircuited(shortCircuited);
        ruleTrace.setDurationNanos(durationNanos);
        ruleTrace.setStatus(status);
        return ruleTrace;
    }

    @Override
    public RuleTrace getRootTrace() {
        return root;
    }

    @Override
    public RuleTrace getCurrentTrace() {
        return stack.peek();
    }

}

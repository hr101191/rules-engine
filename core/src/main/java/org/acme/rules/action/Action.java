package org.acme.rules.action;

import java.util.Map;

public sealed interface Action permits BaseAction {

    void execute(boolean evaluationResult, Map<String, Object> input, Map<String, Object> globalParams);

}

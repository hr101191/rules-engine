package org.acme.rules;

public class DemoWorkflow {

    public static final String WORKFLOW = """
            {
                "acme": [
                    {
                        "workflowName": "workflow1",
                        "description": "workflow1",
                        "enabled": true,
                        "rules": [
                            {
                                "type": "COMPOSITE",
                                "ruleName": "composite rule and",
                                "enabled": true,
                                "operator": "AND",
                                "rules": [
                                    {
                                        "type": "EXPRESSION",
                                        "ruleName": "amount more than 10 in list (long)",
                                        "enabled": true,
                                        "ruleExpressionType": "CEL",
                                        "expression": "[1, 2, 3, 4, 5].sum() > 10"
                                    },
                                    {
                                        "type": "EXPRESSION",
                                        "ruleName": "amount more than 10 in list (double)",
                                        "enabled": true,
                                        "ruleExpressionType": "CEL",
                                        "expression": "[1.0, 2, 3, 4.0, 5].sum() > 10.0"
                                    },
                                    {
                                        "type": "EXPRESSION",
                                        "ruleName": "list contains",
                                        "enabled": true,
                                        "ruleExpressionType": "CEL",
                                        "expression": "[1.0, 2, 3, 4.0, 5].contains(5)"
                                    },
                                    {
                                        "type": "EXPRESSION",
                                        "ruleName": "list contains any",
                                        "enabled": true,
                                        "ruleExpressionType": "CEL",
                                        "expression": "[1.0, 2, 3, 4.0, 5].containsAny([22, 2, 1.1])"
                                    },
                                    {
                                        "type": "EXPRESSION",
                                        "ruleName": "list contains all",
                                        "enabled": true,
                                        "ruleExpressionType": "CEL",
                                        "expression": "[1.0, 2, 3, 4.0, 5].containsAny([2, 3])"
                                    }
                                ]
                            },
                            {
                                "type": "COMPOSITE",
                                "ruleName": "composite rule and (should fail)",
                                "enabled": true,
                                "operator": "AND",
                                "rules": [
                                    {
                                        "type": "EXPRESSION",
                                        "ruleName": "amount more than 10 in list (long)",
                                        "enabled": true,
                                        "ruleExpressionType": "CEL",
                                        "expression": "[1, 2, 3].sum() > 10"
                                    },
                                    {
                                        "type": "EXPRESSION",
                                        "ruleName": "amount more than 10 in list (double)",
                                        "enabled": true,
                                        "ruleExpressionType": "CEL",
                                        "expression": "[1.0, 2, 3, 4.0, 5].sum() > 10.0"
                                    }
                                ]
                            },
                            {
                                "type": "COMPOSITE",
                                "ruleName": "composite rule and also (should fail + short circuit)",
                                "enabled": true,
                                "operator": "AND_ALSO",
                                "rules": [
                                    {
                                        "type": "EXPRESSION",
                                        "ruleName": "amount more than 10 in list (long)",
                                        "enabled": true,
                                        "ruleExpressionType": "CEL",
                                        "expression": "[1, 2, 3].sum() > 10"
                                    },
                                    {
                                        "type": "EXPRESSION",
                                        "ruleName": "amount more than 10 in list (double)",
                                        "enabled": true,
                                        "ruleExpressionType": "CEL",
                                        "expression": "[1.0, 2, 3, 4.0, 5].sum() > 10.0"
                                    }
                                ]
                            },
                            {
                                "type": "COMPOSITE",
                                "ruleName": "composite rule or",
                                "enabled": true,
                                "operator": "OR",
                                "rules": [
                                    {
                                        "type": "EXPRESSION",
                                        "ruleName": "amount more than 10 in list (long)",
                                        "enabled": true,
                                        "ruleExpressionType": "CEL",
                                        "expression": "[1, 2, 3, 4, 5].sum() > 10"
                                    },
                                    {
                                        "type": "EXPRESSION",
                                        "ruleName": "amount more than 10 in list (double)",
                                        "enabled": true,
                                        "ruleExpressionType": "CEL",
                                        "expression": "[1.0, 2, 3, 4.0, 5].sum() > 10.0"
                                    }
                                ]
                            },
                            {
                                "type": "COMPOSITE",
                                "ruleName": "composite rule or else (should short circuit)",
                                "enabled": true,
                                "operator": "OR_ELSE",
                                "rules": [
                                    {
                                        "type": "EXPRESSION",
                                        "ruleName": "amount more than 10 in list (long)",
                                        "enabled": true,
                                        "ruleExpressionType": "CEL",
                                        "expression": "[1, 2, 3, 4, 5].sum() > 10"
                                    },
                                    {
                                        "type": "EXPRESSION",
                                        "ruleName": "amount more than 10 in list (double)",
                                        "enabled": true,
                                        "ruleExpressionType": "CEL",
                                        "expression": "[1.0, 2, 3, 4.0, 5].sum() > 10.0"
                                    }
                                ]
                            },
                            {
                                "type": "EXPRESSION",
                                "ruleName": "transaction amount > 20",
                                "enabled": true,
                                "ruleExpressionType": "CEL",
                                "expression": "root.txnAmt > 20.0"
                            }
                        ]
                    }
                ]
            }
            """;
}

package org.acme.rules.compiler.factory.parameter;

import org.acme.rules.compiler.factory.ParameterExtractorFactory;
import org.acme.rules.model.RuleExpressionType;
import org.acme.rules.model.ScopedParametersDescriptor;
import org.acme.rules.parameter.JsParameterExtractor;
import org.acme.rules.parameter.ParameterExtractor;
import org.jspecify.annotations.NonNull;

public class JsParameterExtractorFactory implements ParameterExtractorFactory {

    @Override
    public boolean supports(@NonNull ScopedParametersDescriptor scopedParametersDescriptor) {
        return scopedParametersDescriptor.getExpressionType().equals(RuleExpressionType.JS);
    }

    @Override
    public ParameterExtractor create(@NonNull ScopedParametersDescriptor scopedParametersDescriptor) {
        return new JsParameterExtractor(scopedParametersDescriptor);
    }

}

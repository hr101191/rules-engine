package org.acme.rules.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RuleTraceMetadata {

    private boolean returnOnTrue;
    private boolean returnOnFalse;
    private Map<String, Object> metadata;

}

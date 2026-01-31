package org.acme;

import io.quarkus.runtime.Startup;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.rules.RuleEngine;

public class RuleEngineManager {

    @Startup
    @ApplicationScoped
    public RuleEngine ruleEngine(Vertx vertx) {
        return RuleEngine.builder().vertx(vertx.getDelegate()).build();
    }

}

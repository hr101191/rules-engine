package org.acme;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.AsyncResultUni;
import io.vertx.core.Future;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.acme.rules.RuleEngine;
import org.acme.rules.model.RuleTrace;
import org.acme.rules.workflow.WorkflowKey;
import org.jboss.resteasy.reactive.RestPath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/api")
public class TestResource {

    private final RuleEngine ruleEngine;

    public TestResource(RuleEngine ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    @POST
    @Path("/{realm}/{workflow}")
    public Uni<List<RuleTrace>> execute(@RestPath String realm, @RestPath String workflow, Map<String, Object> context) {
        Map<String, Object> input = new HashMap<>();
        input.put("root", context);
        return AsyncResultUni.toUni(handler -> {
            ruleEngine.executeWorkflow(WorkflowKey.builder().realm(realm).workflowName(workflow).build(), input)
                    .onComplete(asyncResult -> {
                        if (asyncResult.succeeded()) {
                            handler.handle(Future.succeededFuture(asyncResult.result()));
                        } else {
                            handler.handle(Future.failedFuture(asyncResult.cause()));
                        }
                    });
        });
    }
}

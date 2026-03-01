package org.acme.rules.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.json.annotations.JsonGen;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

@DataObject
@JsonGen(inheritConverter = true)
@Value.Modifiable
@Value.Style(
        nullableAnnotation = "org.jspecify.annotations.Nullable",
        jdk9Collections = true,
        builtinContainerAttributes = false,
        passAnnotations = {
                DataObject.class,
                JsonGen.class
        },
        get = {
                "get*",
                "is*"
        }
)
@JsonSerialize(as = ModifiableRuleTrace.class)
@JsonDeserialize(as = ModifiableRuleTrace.class)
public interface RuleTrace {

    String getRuleName();
    boolean isResult();
    boolean isShortCircuited();
    long getDurationNanos();

    @Nullable
    RuleExecutionStatus getStatus();

    @Nullable
    List<RuleTrace> getChildren();


    default JsonObject toJson() {
        ModifiableRuleTrace modifiableRuleTrace = ModifiableRuleTrace.create().from(this);
        JsonObject jsonObject = new JsonObject();
        ModifiableRuleTraceConverter.toJson(modifiableRuleTrace, jsonObject);
        return jsonObject;
    }

    class JsonFactory {

        public static JsonObject serialize(RuleTrace ruleTrace) {
            ModifiableRuleTrace modifiableRuleTrace = ModifiableRuleTrace.create().from(ruleTrace);
            JsonObject jsonObject = new JsonObject();
            ModifiableRuleTraceConverter.toJson(modifiableRuleTrace, jsonObject);
            return jsonObject;
        }

        public static RuleTrace deserialize(JsonObject jsonObject) {
            ModifiableRuleTrace modifiableRuleTrace = ModifiableRuleTrace.create();
            ModifiableRuleTraceConverter.fromJson(jsonObject, modifiableRuleTrace);
            return modifiableRuleTrace;
        }

    }

}

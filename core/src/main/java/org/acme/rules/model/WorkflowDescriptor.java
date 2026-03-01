package org.acme.rules.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.json.annotations.JsonGen;
import io.vertx.core.json.JsonObject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

@DataObject
@JsonGen(inheritConverter = true)
@Value.Immutable
@Value.Modifiable
@Value.Style(
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
@JsonSerialize(as = ImmutableWorkflowDescriptor.class)
@JsonDeserialize(as = ImmutableWorkflowDescriptor.class)
public interface WorkflowDescriptor {

    @NotEmpty
    String getWorkflowName();

    @Nullable
    String getDescription();

    @Nullable
    ScopedParametersDescriptor getGlobalParametersDescriptor();

    List<@Valid RuleDescriptor> getRules();

    @Nullable
    List<String> getWorkflowsToInject();

    @Value.Default.Boolean(true)
    @Schema(defaultValue = "true")
    boolean isEnabled();

    @Value.Auxiliary
    @GenIgnore
    @Schema(hidden = true)
    default boolean isImmutable() {
        return this instanceof ImmutableWorkflowDescriptor;
    }

    default WorkflowDescriptor toImmutable() {
        return ImmutableWorkflowDescriptor.copyOf(this);
    }

    default WorkflowDescriptor toModifiable() {
        return ModifiableWorkflowDescriptor.create().from(this);
    }

    default JsonObject toJson() {
        ModifiableWorkflowDescriptor modifiableWorkflowDescriptor = ModifiableWorkflowDescriptor.create().from(this);
        JsonObject jsonObject = new JsonObject();
        ModifiableWorkflowDescriptorConverter.toJson(modifiableWorkflowDescriptor, jsonObject);
        return jsonObject;
    }

    class JsonFactory {

        public static JsonObject serialize(WorkflowDescriptor workflowDescriptor) {
            ModifiableWorkflowDescriptor modifiableWorkflowDescriptor = ModifiableWorkflowDescriptor.create().from(workflowDescriptor);
            JsonObject jsonObject = new JsonObject();
            ModifiableWorkflowDescriptorConverter.toJson(modifiableWorkflowDescriptor, jsonObject);
            return jsonObject;
        }

        public static WorkflowDescriptor deserialize(JsonObject jsonObject) {
            ModifiableWorkflowDescriptor modifiableWorkflowDescriptor = ModifiableWorkflowDescriptor.create();
            ModifiableWorkflowDescriptorConverter.fromJson(jsonObject, modifiableWorkflowDescriptor);
            return modifiableWorkflowDescriptor;
        }

    }

}

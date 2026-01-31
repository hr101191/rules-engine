package org.acme.rules.workflow;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

@Getter
@Setter
@Builder
@ToString
public class WorkflowKey {

    private String workflowName;
    private String realm;

    public WorkflowKey(@NonNull String workflowName, @NonNull String realm) {
        this.workflowName = Objects.requireNonNull(workflowName);
        this.realm = Objects.requireNonNull(realm);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkflowKey that)) return false;
        return workflowName.equals(that.workflowName) && realm.equals(that.realm);
    }

    @Override
    public int hashCode() {
        int result = workflowName.hashCode();
        result = 31 * result + realm.hashCode();
        return result;
    }

}

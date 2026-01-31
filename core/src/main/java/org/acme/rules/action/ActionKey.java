package org.acme.rules.action;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ActionKey {

    private String actionName;

    public ActionKey(String actionName) {
        this.actionName = actionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionKey that)) return false;
        return actionName.equals(that.actionName);
    }

    @Override
    public int hashCode() {
        return actionName.hashCode();
    }
    
}

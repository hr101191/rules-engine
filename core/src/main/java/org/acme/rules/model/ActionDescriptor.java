package org.acme.rules.model;


import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ActionDescriptor {

    @NotEmpty
    private String actionName;

    public ActionDescriptor(String actionName) {
        this.actionName = actionName;
    }

}

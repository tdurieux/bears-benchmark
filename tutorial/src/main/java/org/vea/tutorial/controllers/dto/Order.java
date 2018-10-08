package org.vea.tutorial.controllers.dto;

import org.immutables.value.Value;

@ValueStyle
@Value.Immutable
public interface Order {
    long getId();

    static ImmutableOrder.Builder builder() {
        return ImmutableOrder.builder();
    }
}

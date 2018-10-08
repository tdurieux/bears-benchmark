package org.vea.tutorial.controllers.dto;


import org.immutables.value.Value;

import javax.annotation.Nullable;


@Value.Immutable
@ValueStyle
public interface User {
    String getFirstName();
    String getLastName();
    String getNationality();
    @Nullable
    Integer getAge();
    boolean isAdmin();

    static ImmutableUser.Builder builder() {
        return ImmutableUser.builder();
    }
}

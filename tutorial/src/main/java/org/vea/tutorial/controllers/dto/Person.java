package org.vea.tutorial.controllers.dto;


import org.immutables.value.Value;

@Value.Immutable
@ValueStyle
public interface Person {
    Father getFather();
    String getName();
    static ImmutablePerson.Builder builder(){
        return ImmutablePerson.builder();
    }
}

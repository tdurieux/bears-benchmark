package org.vea.tutorial.controllers.dto;

import org.immutables.value.Value;

@Value.Immutable
@ValueStyle
@Value.Style(allParameters = true)
public interface Father {
    String getName();
}

package org.vea.tutorial.controllers.dto;

import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@ValueStyle
public interface Product {
    String getName();

    BigDecimal getPrice();

    boolean isStock();

    static ImmutableProduct.Builder builder(){
        return ImmutableProduct.builder();
    }
}

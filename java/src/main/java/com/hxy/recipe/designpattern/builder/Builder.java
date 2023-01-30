package com.hxy.recipe.designpattern.builder;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.function.BiConsumer;

public class Builder<T> {

    @Getter(AccessLevel.PRIVATE)
    private final T obj;

    private Builder(T obj) {
        this.obj = obj;
    }

    public <T, R> Builder<T> with(BiConsumer<T, R> function, R params) {
        Builder<T> builder = (Builder<T>) this;
        function.accept(builder.getObj(), params);
        return builder;
    }

    public T build() {
        return obj;
    }

    public static <T> Builder<T> newBuilder(T obj) {
        return new Builder<>(obj);
    }

}

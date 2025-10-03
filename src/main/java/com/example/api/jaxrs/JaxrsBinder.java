package com.example.api.jaxrs;

import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;

public class JaxrsBinder {
    private final Multibinder<Object> multibinder;

    private JaxrsBinder(Binder binder) {
        this.multibinder = Multibinder.newSetBinder(binder, Object.class, JaxrsResource.class);
    }

    public static JaxrsBinder jaxrsBinder(Binder binder) {
        return new JaxrsBinder(binder);
    }

    public void bind(Class<?> resourceClass) {
        multibinder.addBinding().to(resourceClass).in(com.google.inject.Singleton.class);
    }
}

package de.agilecoders.wicket.akka.util;

import akka.actor.TypedProps;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public class TypedPropsFactory<T> {

    private final Class<? super T> interfaceClass;
    private final Class<T> implementationClass;

    public TypedPropsFactory(Class<? super T> interfaceClass, Class<T> implementationClass) {
        this.interfaceClass = interfaceClass;
        this.implementationClass = implementationClass;
    }

    public TypedProps<T> create() {
        return TProps.create(interfaceClass, implementationClass);
    }

}

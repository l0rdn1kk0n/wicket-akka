package de.agilecoders.wicket.akka.util;

import akka.actor.TypedProps;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public final class TProps {

    public static <T> TypedProps<T> create(Class<? super T> interfaceClass, Class<T> implClass) {
        return new TypedProps<T>(interfaceClass, implClass);
    }

}

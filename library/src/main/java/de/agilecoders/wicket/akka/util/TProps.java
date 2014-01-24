package de.agilecoders.wicket.akka.util;

import akka.actor.TypedProps;

/**
 * Helper class to create {@link akka.actor.TypedProps} instances.
 *
 * @author miha
 */
public final class TProps {

    /**
     * creates a new {@link akka.actor.TypedProps} instance.
     *
     * @param interfaceClass the interface class
     * @param implClass the implementation class
     * @param <T> the type of implementation class
     * @return new implementation instance
     */
    public static <T> TypedProps<T> create(Class<? super T> interfaceClass, Class<T> implClass) {
        return new TypedProps<T>(interfaceClass, implClass);
    }

}

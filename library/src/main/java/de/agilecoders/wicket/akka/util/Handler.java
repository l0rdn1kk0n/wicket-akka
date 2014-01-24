package de.agilecoders.wicket.akka.util;

/**
 * A task that handles an event.
 * Implementors define a single method with one argument of type T called
 * <tt>handle</tt>.
 *
 * <p>The <tt>Handler</tt> interface is similar to {@link
 * java.lang.Runnable}, in that both are designed for classes whose
 * instances are potentially executed by another thread.  A
 * <tt>Runnable</tt>, however, does not receive an argument.
 *
 * @author miha
 */
public interface Handler<T> {

    /**
     * @param event the event to handle.
     */
    void handle(T event);
}

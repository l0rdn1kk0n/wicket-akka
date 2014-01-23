package de.agilecoders.wicket.akka.util;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public interface Handler<T> {
    void handle(T event);
}

package de.agilecoders.wicket.akka.models;

import akka.actor.ActorRef;
import de.agilecoders.wicket.akka.Akka;
import de.agilecoders.wicket.akka.util.Handler;
import org.apache.wicket.model.IModel;

/**
 * An {@link de.agilecoders.wicket.akka.models.EventModel} subscribes itself to a given event channel and updates
 * its model object with received event message. An event message is received asynchronously in a different thread.
 *
 * @author miha
 */
public class EventModel<T> implements IModel<T> {

    private final ActorRef ref;
    private volatile T value;

    /**
     * Construct using {@code null} as initial value.
     *
     * @param channel the channel to listen on
     */
    public EventModel(Class<T> channel) {
        this(channel, null);
    }

    /**
     * Construct.
     *
     * @param channel      the channel to listen on
     * @param initialValue the initial value for this model
     */
    public EventModel(Class<T> channel, T initialValue) {
        this.value = initialValue;
        this.ref = Akka.instance().subscribeEvent(newHandler(), channel);
    }

    /**
     * @return a new handler instance.
     */
    protected Handler<T> newHandler() {
        return new Handler<T>() {
            @Override
            public void handle(T param) {
                value = param;
            }
        };
    }

    @Override
    public T getObject() {
        return value;
    }

    @Override
    public void setObject(T object) {
        value = object;
    }

    @Override
    public void detach() {
        value = null;

        Akka.instance().unsubscribeEvent(ref);
        Akka.instance().stop(ref);
    }
}

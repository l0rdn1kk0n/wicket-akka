package de.agilecoders.wicket.akka.models;

import akka.actor.ActorRef;
import de.agilecoders.wicket.akka.Akka;
import de.agilecoders.wicket.akka.util.Handler;
import org.apache.wicket.model.IModel;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public class EventModel<T> implements IModel<T> {

    private final Class<T> channel;
    private final ActorRef ref;
    private volatile T value;

    public EventModel(Class<T> channel) {
        this(channel, null);
    }

    public EventModel(Class<T> channel, T initialValue) {
        this.channel = channel;
        this.value = initialValue;
        this.ref = Akka.instance().subscribeEvent(new Handler<T>(){
            @Override
            public void handle(T param) {
                value = param;
            }
        }, channel);
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
        Akka.instance().unsubscribeEvent(ref, channel);
    }
}

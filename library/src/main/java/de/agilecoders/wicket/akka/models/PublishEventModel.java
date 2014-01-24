package de.agilecoders.wicket.akka.models;

import de.agilecoders.wicket.akka.Akka;
import de.agilecoders.wicket.akka.util.ModelChangedEvent;
import org.apache.wicket.model.IModel;

/**
 * An {@link de.agilecoders.wicket.akka.models.PublishEventModel} publishes all model object change events on given
 * channel.
 *
 * @author miha
 */
public class PublishEventModel<T> implements IModel<T> {

    private final Class<T> channel;
    private volatile T value;

    /**
     * Construct using {@code null} as initial value.
     *
     * @param channel the channel to listen on
     */
    public PublishEventModel(Class<T> channel) {
        this(channel, null);
    }

    /**
     * Construct.
     *
     * @param channel      the channel to listen on
     * @param initialValue the initial value for this model
     */
    public PublishEventModel(Class<T> channel, T initialValue) {
        this.channel = channel;
        this.value = initialValue;
    }

    @Override
    public T getObject() {
        return value;
    }

    @Override
    public void setObject(T object) {
        value = object;

        Akka.instance().publishEvent(new ModelChangedEvent<T>(channel, value));
    }

    @Override
    public void detach() {
        value = null;
    }
}

package de.agilecoders.wicket.akka.models;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public abstract class TellActorModel<R, M> extends ActorModel<R> {

    public TellActorModel() {
        super();
    }

    public final void tell(M message) {
        getActorRef().tell(message, getSender());
    }
}

package de.agilecoders.wicket.akka.models;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import org.apache.wicket.model.IModel;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public abstract class ActorModel<Reply> implements IModel<Reply> {

    private final ActorRef actor;
    private volatile Reply result;

    ActorModel() {
        actor = newActor();
        result = null;
    }

    @Override
    public Reply getObject() {
        return result;
    }

    @Override
    public void setObject(Reply object) {
        result = object;
    }

    @Override
    public void detach() {
        result = null;
        actor.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }

    protected abstract ActorRef newActor();

    protected final ActorRef getActorRef() {
        return actor;
    }

    protected ActorRef getSender() {
        return ActorRef.noSender();
    }
}

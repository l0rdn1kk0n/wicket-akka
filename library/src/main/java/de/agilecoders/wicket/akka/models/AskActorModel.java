package de.agilecoders.wicket.akka.models;

import java.util.concurrent.Future;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public abstract class AskActorModel<R, M> extends DAskActorModel<R, M> {

    private final M message;

    public AskActorModel(M message) {
        super();

        this.message = message;
    }

    public final Future<R> ask() {
        return ask(message);
    }

    public final scala.concurrent.Future<R> askWithScalaFuture() {
        return askWithScalaFuture(message);
    }

}

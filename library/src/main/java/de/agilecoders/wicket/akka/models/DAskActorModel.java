package de.agilecoders.wicket.akka.models;

import akka.pattern.Patterns;
import de.agilecoders.wicket.akka.util.Handler;
import de.agilecoders.wicket.akka.util.Util;

import java.util.concurrent.Future;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public abstract class DAskActorModel<R, M> extends ActorModel<R> {

    private final Handler<R> handler;

    public DAskActorModel() {
        super();

        this.handler = new Handler<R>() {
            @Override
            public void handle(R param) {
                setObject(param);
            }
        };
    }

    public final Future<R> ask(M message) {
        return Util.toJavaFuture(internalAsk(message), handler);
    }
    public final scala.concurrent.Future<R> askWithScalaFuture(M message) {
        return Util.toScalaFuture(internalAsk(message), handler);
    }

    private scala.concurrent.Future<Object> internalAsk(M message) {
        return Patterns.ask(getActorRef(), message, timeout());
    }

    protected int timeout() {
        return 3000;
    }
}

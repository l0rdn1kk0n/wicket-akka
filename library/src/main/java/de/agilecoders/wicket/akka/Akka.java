package de.agilecoders.wicket.akka;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Scheduler;
import akka.actor.TypedActor;
import akka.actor.TypedActorExtension;
import akka.actor.TypedProps;
import akka.actor.UntypedActor;
import akka.event.EventStream;
import akka.japi.Creator;
import com.typesafe.config.Config;
import de.agilecoders.wicket.akka.util.Handler;
import org.apache.wicket.util.lang.Args;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public final class Akka {

    private static Akka instance = null;

    public static synchronized Akka initialize(ActorSystem system) {
        if (instance == null) {
            instance = new Akka(system);
        }

        return instance;
    }

    public static Akka instance() {
        return Args.notNull(instance, "please call Akka#initialize() before.");
    }

    private final ActorSystem system;
    private final TypedActorExtension typedActorExtension;

    private Akka(ActorSystem actorSystem) {
        system = actorSystem;
        typedActorExtension = TypedActor.get(actorSystem);
    }

    public ActorSystem system() {
        return system;
    }

    public TypedActorExtension typedActorExtension() {
        return typedActorExtension;
    }

    public EventStream eventStream() {
        return system().eventStream();
    }

    public void publishEvent(Object event, ActorRef subscriber) {
        eventStream().publish(event, subscriber);
    }

    public void publishEvent(Object event) {
        eventStream().publish(event);
    }

    public void unsubscribeEvent(ActorRef subscriber) {
        eventStream().unsubscribe(subscriber);
    }

    public void unsubscribeEvent(ActorRef subscriber, Class<?> channel) {
        eventStream().unsubscribe(subscriber, channel);
    }

    public void subscribeEvent(ActorRef subscriber, Class<?> channel) {
        eventStream().subscribe(subscriber, channel);
    }

    public <T> ActorRef subscribeEvent(final Handler<T> handler, final Class<T> channel) {
        ActorRef ref = system.actorOf(new Props().withCreator(new Creator<Actor>() {
            @Override
            public Actor create() throws Exception {
                return new UntypedActor() {
                    @Override
                    public void onReceive(Object message) throws Exception {
                        if (message != null && message.getClass().equals(channel)) {
                            handler.handle(channel.cast(message));
                        } else {
                            unhandled(message);
                        }
                    }
                };
            }
        }), "event-subscription");

        eventStream().subscribe(ref, channel);
        return ref;
    }

    public Scheduler scheduler() {
        return system().scheduler();
    }

    public Config config() {
        return settings().config();
    }

    public ActorSystem.Settings settings() {
        return system().settings();
    }

    public <T> T typedActorOf(TypedProps<T> props, String name) {
        return typedActorExtension().typedActorOf(props, name);
    }

    public <T> T typedActorOf(TypedProps<T> props) {
        return typedActorExtension().typedActorOf(props);
    }

    public <T> T typedActorOf(TypedProps<T> props, ActorRef actorRef) {
        return typedActorExtension().typedActorOf(props, actorRef);
    }

}
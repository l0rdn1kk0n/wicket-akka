package de.agilecoders.wicket.akka;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
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
import scala.concurrent.duration.Duration;

/**
 * Wrapper of {@link akka.actor.ActorSystem} that provides some helper methods to interact with the actor system.
 *
 * @author miha
 */
public final class Akka {

    private static Akka instance = null;

    /**
     * initializes the akka instance
     *
     * @param system the actor system to wrap
     * @return newly initialized akka instance
     */
    public static synchronized Akka initialize(ActorSystem system) {
        if (instance == null) {
            instance = new Akka(system);
        }

        return instance;
    }

    /**
     * @return akka instance
     */
    public static Akka instance() {
        return Args.notNull(instance, "please call Akka#initialize() before.");
    }

    private final ActorSystem system;
    private final TypedActorExtension typedActorExtension;

    /**
     * Private constructor that initializes the typed actor extension.
     *
     * @param actorSystem the actor system to wrap.
     */
    private Akka(ActorSystem actorSystem) {
        system = actorSystem;
        typedActorExtension = TypedActor.get(actorSystem);
    }

    /**
     * @return the assigned actor system
     */
    public ActorSystem system() {
        return system;
    }

    /**
     * @return the typed actor extension, which is responsible of creating and stopping typed actors
     */
    public TypedActorExtension typedActorExtension() {
        return typedActorExtension;
    }

    /**
     * @return akka based event stream
     */
    public EventStream eventStream() {
        return system().eventStream();
    }

    /**
     * Publishes the given Event to the given Subscriber
     *
     * @param event the event to publish
     * @param subscriber the subscriber that should receive the event
     */
    public void publishEvent(Object event, ActorRef subscriber) {
        eventStream().publish(event, subscriber);
    }

    /**
     * Publishes the specified Event to this bus
     *
     * @param event the event to publish
     */
    public void publishEvent(Object event) {
        eventStream().publish(event);
    }

    /**
     * Attempts to deregister the subscriber from all channels it may be subscribed to
     *
     * @param subscriber the subscriber to deregister
     */
    public void unsubscribeEvent(ActorRef subscriber) {
        eventStream().unsubscribe(subscriber);
    }

    /**
     * Attempts to deregister the subscriber from the specified channel
     *
     * @param subscriber the subscriber to deregister
     * @param channel    the channel to deregister subscriber from
     */
    public void unsubscribeEvent(ActorRef subscriber, Class<?> channel) {
        eventStream().unsubscribe(subscriber, channel);
    }

    /**
     * Attempts to register the subscriber to the specified channel.
     *
     * @param subscriber the subscriber to register
     * @param channel the channel the subscriber should watch
     */
    public void subscribeEvent(ActorRef subscriber, Class<?> channel) {
        eventStream().subscribe(subscriber, channel);
    }

    /**
     * Attempts to register the subscriber to the specified channel.
     *
     * @return the temporary actor that delegates the event to given handler
     */
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
        }));

        eventStream().subscribe(ref, channel);
        return ref;
    }

    /**
     * @return an Akka scheduler service
     */
    public Scheduler scheduler() {
        return system().scheduler();
    }

    /**
     * @return the actor configuration
     */
    public Config config() {
        return system.settings().config();
    }

    /**
     * Creates a TypedActor that intercepts the calls and forwards them as {@link akka.actor.TypedActor.MethodCall}
     * to the provided ActorRef.
     */
    public <T> T typedActorOf(TypedProps<T> props, String name) {
        return typedActorExtension().typedActorOf(props, name);
    }

    /**
     * Creates a TypedActor that intercepts the calls and forwards them as {@link akka.actor.TypedActor.MethodCall}
     * to the provided ActorRef.
     */
    public <T> T typedActorOf(TypedProps<T> props) {
        return typedActorExtension().typedActorOf(props);
    }

    /**
     * Creates a TypedActor that intercepts the calls and forwards them as {@link akka.actor.TypedActor.MethodCall}
     * to the provided ActorRef.
     */
    public <T> T typedActorOf(TypedProps<T> props, ActorRef actorRef) {
        return typedActorExtension().typedActorOf(props, actorRef);
    }

    /**
     * Stop this actor system. This will stop the guardian actor, which in turn
     * will recursively stop all its child actors, then the system guardian and
     * block current thread until the system has been shutdown. This will
     * block until after all on termination callbacks have been run.
     *
     * @param shutdownTimeout the time to wait for actor system until it is shutdown.
     */
    public void shutdownAndAwaitTermination(Duration shutdownTimeout) {
        system.shutdown();
        system.awaitTermination(shutdownTimeout);
    }

    /**
     * Stop this actor system. This will stop the guardian actor, which in turn
     * will recursively stop all its child actors, then the system guardian and
     * block current thread until the system has been shutdown. This will
     * block until after all on termination callbacks have been run.
     */
    public void shutdownAndAwaitTermination() {
        system.shutdown();
        system.awaitTermination();
    }

    /**
     * stops given actor. This asynchronously stops the Actor.
     *
     * @param actor the actor to stop.
     * @throws java.lang.IllegalArgumentException if given actor isn't a typed or untyped actor.
     */
    public void stop(Object actor) {
        if (actor instanceof ActorRef) {
            system.stop((ActorRef) actor);
        } else if (typedActorExtension.isTypedActor(actor)) {
            typedActorExtension.stop(actor);
        } else {
            throw new IllegalArgumentException("given arguments isn't a typed or untyped actor.");
        }
    }

    /**
     * sends a {@link akka.actor.PoisonPill} to given actor. This asynchronously stops the Actor after it's done
     * with all calls that were made prior to this call.
     *
     * @param actor the actor to send poison pill to.
     * @throws java.lang.IllegalArgumentException if given actor isn't a typed or untyped actor.
     */
    public void poisonPill(Object actor) {
        if (actor instanceof ActorRef) {
            ((ActorRef) actor).tell(PoisonPill.getInstance(), ActorRef.noSender());
        } else if (typedActorExtension.isTypedActor(actor)) {
            typedActorExtension.poisonPill(actor);
        } else {
            throw new IllegalArgumentException("given arguments isn't a typed or untyped actor.");
        }
    }
}
package de.agilecoders.wicket.akka.application;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import akka.actor.TypedActorExtension;
import akka.actor.TypedProps;
import akka.event.EventStream;
import com.typesafe.config.Config;

/**
 * base interface for all akka based applications.
 *
 * @author miha
 */
public interface IAkkaApplication {

    /**
     * @return the assigned actor system
     */
    ActorSystem getActorSystem();

    /**
     * @return the typed actor extension, which is responsible of creating and stopping typed actors
     */
    TypedActorExtension getTypedActorExtension();

    /**
     * @return akka based event stream
     */
    EventStream getEventStream();

    /**
     * @return the actor configuration (this can be also used to access custom configurations)
     */
    Config getActorConfig();

    /**
     * @return an Akka scheduler service
     */
    Scheduler getScheduler();

    /**
     * creates a new typed actor
     *
     * @param props the {@link akka.actor.Props} to use to create the actor
     * @param name the name of the actor
     * @param <T> type of the typed actor
     * @return a new typed actor instance
     */
    <T> T typedActorOf(TypedProps<T> props, String name);

    /**
     * creates a new typed actor that is baked by a normal actor. This is usable if you want to communicate remotely
     * with TypedActors on other machines
     *
     * @param props the {@link akka.actor.Props} to use to create the actor
     * @param actorRef the actor that implements the typed actor
     * @param <T> type of the typed actor
     * @return a new typed actor instance
     */
    <T> T typedActorOf(TypedProps<T> props, ActorRef actorRef);

    /**
     * creates a new typed actor
     *
     * @param props the {@link akka.actor.Props} to use to create the actor
     * @param <T> type of the typed actor
     * @return a new typed actor instance
     */
    <T> T typedActorOf(TypedProps<T> props);

    /**
     * stops given actor
     *
     * @param actor the actor stop (untyped or typed)
     */
    void stopActor(Object actor);

    /**
     * sends a poison pill to given actor
     *
     * @param actor the actor stop (untyped or typed)
     */
    void poisonPill(Object actor);

}

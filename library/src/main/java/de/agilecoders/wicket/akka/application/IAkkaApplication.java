package de.agilecoders.wicket.akka.application;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import akka.actor.TypedActorExtension;
import akka.actor.TypedProps;
import akka.event.EventStream;
import com.typesafe.config.Config;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public interface IAkkaApplication {

    ActorSystem getActorSystem();

    TypedActorExtension getTypedActorExtension();

    EventStream getEventStream();

    ActorSystem.Settings getActorSettings();

    Config getActorConfig();

    Scheduler getScheduler();

    <T> T typedActorOf(TypedProps<T> props, String name);

    <T> T typedActorOf(TypedProps<T> props, ActorRef actorRef);

    <T> T typedActorOf(TypedProps<T> props);

}

package de.agilecoders.wicket.akka.application;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import akka.actor.TypedActorExtension;
import akka.actor.TypedProps;
import akka.event.EventStream;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.agilecoders.wicket.akka.Akka;
import org.apache.wicket.Application;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.protocol.http.WebApplication;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Akka based {@link org.apache.wicket.protocol.http.WebApplication} that provides access to
 * {@link de.agilecoders.wicket.akka.Akka}. It's also possible to provide your own {@link akka.actor.ActorSystem} or
 * {@link com.typesafe.config.Config}.
 *
 * <pre>
 * <code>
 * public class MyCustomApplication extends AkkaWebApplication {
 *
 *     {@literal @}Override
 *     protected String newActorSystemName() {
 *         return "my-custom-actor-system-name";
 *     }
 *
 *     {@literal @}Override
 *     protected ActorSystem newActorSystem(String name, Config config) {
 *         return ActorSystem.create(name, config);
 *     }
 *
 *     {@literal @}Override
 *     protected Config newActorConfig() {
 *         return ConfigFactory.load("my-custom-config.json").withFallback(ConfigFactory.load());
 *     }
 *
 *     {@literal @}Override
 *     protected void onTermination() {
 *         logger.info("actor system was stopped.");
 *     }
 *
 *     {@literal @}Override
 *     protected Duration newShutdownTimeout() {
 *         // my.custom.shutdownTimeout = 5 sec
 *         return Duration.apply(getActorConfig().getString("my.custom.shutdownTimeout"));
 *     }
 *
 * }
 * </code>
 * </pre>
 *
 * @author miha
 */
public abstract class AkkaWebApplication extends WebApplication implements IAkkaApplication {

    /**
     * Covariant override for easy getting the current {@link AkkaWebApplication} without having to cast
     * it.
     */
    public static AkkaWebApplication get() {
        Application application = Application.get();

        if (!(application instanceof AkkaWebApplication)) {
            throw new WicketRuntimeException("The application attached to the current thread is not a " +
                                             AkkaWebApplication.class.getSimpleName());
        }

        return (AkkaWebApplication) application;
    }

    private Akka akka;
    private Duration shutdownTimeout;

    @Override
    protected void internalInit() {
        super.internalInit();

        ActorSystem actorSystem = newActorSystem(newActorSystemName(), newActorConfig());
        akka = Akka.initialize(actorSystem);
        shutdownTimeout = newShutdownTimeout();

        actorSystem.registerOnTermination(new Runnable() {
            @Override
            public void run() {
                onTermination();
            }
        });
    }

    @Override
    public void internalDestroy() {
        super.internalDestroy();

        akka.shutdownAndAwaitTermination(shutdownTimeout);
    }

    @Override
    public final ActorSystem getActorSystem() {
        return akka.system();
    }

    @Override
    public final TypedActorExtension getTypedActorExtension() {
        return akka.typedActorExtension();
    }

    @Override
    public final EventStream getEventStream() {
        return akka.eventStream();
    }

    @Override
    public final Scheduler getScheduler() {
        return akka.scheduler();
    }

    @Override
    public final Config getActorConfig() {
        return akka.config();
    }

    @Override
    public final <T> T typedActorOf(TypedProps<T> props, String name) {
        return akka.typedActorOf(props, name);
    }

    @Override
    public final <T> T typedActorOf(TypedProps<T> props) {
        return akka.typedActorOf(props);
    }

    @Override
    public final <T> T typedActorOf(TypedProps<T> props, ActorRef actorRef) {
        return akka.typedActorOf(props, actorRef);
    }

    @Override
    public void stopActor(Object actor) {
        akka.stop(actor);
    }

    @Override
    public void poisonPill(Object actor) {
        akka.poisonPill(actor);
    }

    /**
     * hook that is called after actor system was terminated.
     */
    protected void onTermination() {}

    /**
     * @return a new actor system which is used within this web application
     */
    protected ActorSystem newActorSystem(String name, Config config) {
        return ActorSystem.create(name, config);
    }

    /**
     * @return actor system configuration
     */
    protected Config newActorConfig() {
        return ConfigFactory.load();
    }

    /**
     * @return a new actor system name
     */
    protected String newActorSystemName() {
        return getName();
    }

    /**
     * @return shutdown timeout that is used to await termination of actor system
     */
    protected Duration newShutdownTimeout() {
        return Duration.apply(3, TimeUnit.SECONDS);
    }
}

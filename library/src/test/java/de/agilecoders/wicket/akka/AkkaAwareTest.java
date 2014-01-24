package de.agilecoders.wicket.akka;

import akka.actor.ActorSystem;
import akka.event.EventStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Base akka aware test class that provides access to an akka instance.
 *
 * @author miha
 */
public class AkkaAwareTest {

    private static Akka akka;

    @BeforeClass
    public static void beforeClass() throws Exception {
        akka = Akka.initialize(ActorSystem.create());
    }

    @AfterClass
    public static void afterClass() {
        akka.shutdownAndAwaitTermination();
    }

    protected final Akka akka() {
        return akka;
    }

    protected final ActorSystem system() {
        return akka.system();
    }

    protected final EventStream eventStream() {
        return akka.eventStream();
    }
}

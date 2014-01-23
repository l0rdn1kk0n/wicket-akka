package de.agilecoders.wicket.akka;

import akka.actor.ActorSystem;
import akka.event.EventStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * TODO miha: document class purpose
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
        akka.system().shutdown();
        akka.system().awaitTermination();
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

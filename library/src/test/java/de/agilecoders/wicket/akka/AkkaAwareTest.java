package de.agilecoders.wicket.akka;

import akka.actor.ActorSystem;
import akka.event.EventStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Base akka aware test class that provides access to an akka instance.
 *
 * @author miha
 */
public class AkkaAwareTest {

    private static Akka akka;

    @BeforeClass
    public static void before() throws Exception {
        Akka.reset();
        akka = Akka.initialize(ActorSystem.create());
    }

    @AfterClass
    public static void after() {
        Akka.reset();

        assertThat(akka.system().isTerminated(), is(true));

        akka = null;
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

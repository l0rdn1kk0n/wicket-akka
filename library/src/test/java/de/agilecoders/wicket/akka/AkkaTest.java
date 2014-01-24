package de.agilecoders.wicket.akka;

import akka.actor.ActorSystem;
import akka.actor.TypedProps;
import akka.dispatch.Futures;
import akka.japi.Option;
import de.agilecoders.wicket.akka.util.TProps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the {@link de.agilecoders.wicket.akka.Akka} class
 *
 * @author miha
 */
public class AkkaTest {
    private static ActorSystem system;

    @Before
    public void before() throws Exception {
        Akka.reset();

        system = ActorSystem.create();
    }

    @After
    public void after() {
        Akka.reset();
    }

    @Test
    public void initializationSetsInstance() {
        Akka.initialize(system);

        assertThat(Akka.instance().system(), is(system));
    }

    @Test
    public void typedActorOf() throws Exception {
        Akka akka = Akka.initialize(system);
        Int proxy = akka.typedActorOf(Impl.props());

        assertThat(proxy.value(), is("value"));
        assertThat(proxy.optionValue().get(), is("value"));
        assertThat(Await.result(proxy.futureValue(), Duration.apply("3 sec")), is("value"));
    }

    public static interface Int {
        String value();

        Option<String> optionValue();

        Future<String> futureValue();

        void fireAndForgetValue();
    }

    public static class Impl implements Int {

        private static final class Holder {
            private static final TypedProps<Impl> props = TProps.create(Int.class, Impl.class);
        }

        public static TypedProps<Impl> props() {
            return Holder.props;
        }

        public Impl() {
        }

        @Override
        public String value() {
            return "value";
        }

        @Override
        public Option<String> optionValue() {
            return new Option.Some<String>("value");
        }

        @Override
        public Future<String> futureValue() {
            return Futures.successful("value");
        }

        @Override
        public void fireAndForgetValue() {
            // do nothing
        }
    }
}



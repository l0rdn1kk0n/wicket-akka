package de.agilecoders.wicket.akka.models;

import de.agilecoders.wicket.akka.AkkaAwareTest;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * tests the {@link de.agilecoders.wicket.akka.models.EventModel} class.
 *
 * @author miha
 */
public class EventModelTest extends AkkaAwareTest {

    @Test
    public void eventIsReceived() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        EventModel<String> model = new EventModel<String>(String.class) {
            @Override
            public void setObject(String object) {
                super.setObject(object);
                latch.countDown();
            }
        };

        eventStream().publish("test");

        latch.await(250, TimeUnit.MILLISECONDS);
        assertThat(model.getObject(), is("test"));

        model.detach();
    }
}

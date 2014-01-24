package de.agilecoders.wicket.akka.models;

import akka.dispatch.Futures;
import org.junit.Test;
import scala.concurrent.ExecutionContext$;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * tests the {@link de.agilecoders.wicket.akka.models.ScalaFutureModel}
 *
 * @author miha
 */
public class ScalaFutureModelTest {

    @SuppressWarnings("unchecked")
    @Test
    public void getObjectReturnsFutureValue() throws Exception {
        Future<String> f = (Future<String>) Futures.successful("value");

        assertThat(new ScalaFutureModel<String>(f).getObject(), is("value"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void FutureModelOfgetObjectReturnsFutureValue() throws Exception {
        Future<String> f = (Future<String>) Futures.successful("value");

        assertThat(FutureModel.<String>of(f).getObject(), is("value"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void timeoutIsUsed() throws Exception {
        Future<String> f = (Future<String>) Futures.future(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(500);
                return "";
            }
        }, ExecutionContext$.MODULE$.global());

        try {
            FutureModel.<String>of(f, Duration.apply(100, TimeUnit.MILLISECONDS)).getObject();
            fail("there should be an exception.");
        } catch (RuntimeException e) {
            if (!(e.getCause() instanceof TimeoutException)) {
                fail("there should be an exception of type TimeoutException. Got: " + e);
            }
        }
    }
}

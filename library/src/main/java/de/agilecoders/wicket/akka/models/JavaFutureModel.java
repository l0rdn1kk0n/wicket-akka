package de.agilecoders.wicket.akka.models;

import scala.concurrent.duration.Duration;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A {@link de.agilecoders.wicket.akka.models.FutureModel} that uses java futures to provide the model object.
 *
 * @author miha
 */
public class JavaFutureModel<T> extends FutureModel<T> {

    private final Future<T> future;
    private final Duration timeout;

    /**
     * Construct using default timeout from {@link FutureModel#timeout()}
     *
     * @param future the future that provides the model object
     */
    public JavaFutureModel(Future<T> future) {
        this(future, timeout());
    }

    /**
     * Construct.
     *
     * @param future  the future that provides the model object
     * @param timeout the await future result timeout
     */
    public JavaFutureModel(Future<T> future, Duration timeout) {
        this.future = future;
        this.timeout = timeout;
    }

    @Override
    protected T load() {
        try {
            return future.get(timeout.toSeconds(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

}

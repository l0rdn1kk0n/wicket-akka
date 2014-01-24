package de.agilecoders.wicket.akka.models;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

/**
 * A {@link de.agilecoders.wicket.akka.models.FutureModel} that uses scala futures to provide the model object.
 *
 * @author miha
 */
public class ScalaFutureModel<T> extends FutureModel<T> {

    private final Future<T> future;
    private final Duration timeout;

    /**
     * Construct using default timeout from {@link FutureModel#timeout()}
     *
     * @param future the future that provides the model object
     */
    public ScalaFutureModel(Future<T> future) {
        this(future, timeout());
    }

    /**
     * Construct.
     *
     * @param future  the future that provides the model object
     * @param timeout the await future result timeout
     */
    public ScalaFutureModel(Future<T> future, Duration timeout) {
        this.future = future;
        this.timeout = timeout;
    }

    @Override
    protected T load() {
        try {
            return Await.result(future, timeout);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

package de.agilecoders.wicket.akka.models;

import org.apache.wicket.model.LoadableDetachableModel;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

/**
 * Base class for *FutureModel instances.
 *
 * @author miha
 */
public abstract class FutureModel<T> extends LoadableDetachableModel<T> {

    private static final Duration timeout = Duration.apply("3 sec");

    /**
     * @return await future result timeout
     */
    protected static Duration timeout() {
        return timeout;
    }

    /**
     * creates a new future model instance that handles a scala future.
     *
     * @param future the scala future that contains the model object
     * @param <I>    the type of model object
     * @return new future model instance
     */
    public static <I> FutureModel<I> of(Future<I> future) {
        return new ScalaFutureModel<I>(future);
    }

    /**
     * creates a new future model instance that handles a scala future.
     *
     * @param future  the scala future that contains the model object
     * @param timeout the timeout to use to receive future value
     * @param <I>     the type of model object
     * @return new future model instance
     */
    public static <I> FutureModel<I> of(Future<I> future, Duration timeout) {
        return new ScalaFutureModel<I>(future, timeout);
    }

    /**
     * creates a new future model instance that handles a java future.
     *
     * @param future the scala future that contains the model object
     * @param <I>    the type of model object
     * @return new future model instance
     */
    public static <I> FutureModel<I> of(java.util.concurrent.Future<I> future) {
        return new JavaFutureModel<I>(future);
    }

    /**
     * creates a new future model instance that handles a java future.
     *
     * @param future  the scala future that contains the model object
     * @param timeout the timeout to use to receive future value
     * @param <I>     the type of model object
     * @return new future model instance
     */
    public static <I> FutureModel<I> of(java.util.concurrent.Future<I> future, Duration timeout) {
        return new JavaFutureModel<I>(future, timeout);
    }
}

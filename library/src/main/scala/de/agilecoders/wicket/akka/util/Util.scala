package de.agilecoders.wicket.akka.util

import de.agilecoders.wicket.akka.Akka
import scala.concurrent.{Await, Future}
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

/**
 * helper class to transform scala/java objects
 *
 * @author miha
 */
object Util {
    import java.util.concurrent.{Future => JFuture}

    def toScalaFuture[T](f: Future[AnyRef], func: Handler[T]): Future[T] = {
        implicit val executionContext = defaultExecutionContext()

        f onSuccess {
            case v => func.handle(v.asInstanceOf[T])
        }

        toScalaFuture(f)
    }

    def toScalaFuture[T](f: Future[AnyRef]): Future[T] = {
        implicit val executionContext = defaultExecutionContext()

        f.map(_.asInstanceOf[T])
    }

    def toJavaFuture[T](f: Future[AnyRef]): JFuture[T] = {
        implicit val executionContext = defaultExecutionContext()

        new JavaFuture[T](f)
    }

    def toJavaFuture[T](f: Future[AnyRef], func: Handler[T]): JFuture[T] = {
        implicit val executionContext = defaultExecutionContext()

        f onSuccess {
            case v => func.handle(v.asInstanceOf[T])
        }
        new JavaFuture[T](f)
    }

    class JavaFuture[T](f: Future[AnyRef]) extends JFuture[T] {
        implicit val executionContext = defaultExecutionContext()

        def cancel(mayInterruptIfRunning: Boolean): Boolean = false

        def isCancelled: Boolean = false

        def isDone: Boolean = f.isCompleted

        def get(): T = get(3000, TimeUnit.SECONDS)

        def get(timeout: Long, unit: TimeUnit): T = Await.result(f.map(_.asInstanceOf[T]), Duration(timeout, unit))
    }

    def defaultExecutionContext() = Akka.instance().system().dispatcher

    def globalExecutionContext() = scala.concurrent.ExecutionContext.Implicits.global

}

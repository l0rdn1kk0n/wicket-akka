package de.agilecoders.wicket.akka.models;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import de.agilecoders.wicket.akka.AkkaAwareTest;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public class ActorModelTest extends AkkaAwareTest {

    @Test
    public void ask() throws Exception {
        DAskActorModel<Integer, String> model = new DAskActorModel<Integer, String>() {
            @Override
            protected ActorRef newActor() {
                return system().actorOf(Props.apply(new Creator<Actor>() {
                    @Override
                    public Actor create() throws Exception {
                        return new UntypedActor() {
                            @Override
                            public void onReceive(Object message) throws Exception {
                                if (message instanceof String) {
                                    getSender().tell(Integer.parseInt((String) message), getSelf());
                                } else {
                                    unhandled(message);
                                }
                            }
                        };
                    }
                }));
            }
        };

        assertThat(model.ask("100").get(), is(100));

        model.detach();
    }

    @Test
    public void askByConstructor() throws Exception {
        DAskActorModel<Integer, String> model = new DAskActorModel<Integer, String>() {
            @Override
            protected ActorRef newActor() {
                return system().actorOf(Props.apply(new Creator<Actor>() {
                    @Override
                    public Actor create() throws Exception {
                        return new UntypedActor() {
                            @Override
                            public void onReceive(Object message) throws Exception {
                                if (message instanceof String) {
                                    getSender().tell(Integer.parseInt((String) message), getSelf());
                                } else {
                                    unhandled(message);
                                }
                            }
                        };
                    }
                }));
            }
        };

        assertThat(model.ask("100").get(), is(100));

        model.detach();
    }

    @Test
    public void askWithScalaFuture() throws Exception {
        DAskActorModel<Integer, String> model = new DAskActorModel<Integer, String>() {
            @Override
            protected ActorRef newActor() {
                return system().actorOf(Props.apply(new Creator<Actor>() {
                    @Override
                    public Actor create() throws Exception {
                        return new UntypedActor() {
                            @Override
                            public void onReceive(Object message) throws Exception {
                                if (message instanceof String) {
                                    getSender().tell(Integer.parseInt((String) message), getSelf());
                                } else {
                                    unhandled(message);
                                }
                            }
                        };
                    }
                }));
            }
        };

        Future<Integer> f = model.askWithScalaFuture("100");

        assertThat(Await.result(f, Duration.apply("3 sec")), is(100));
        assertThat(model.getObject(), is(100));

        model.detach();
    }

    @Test
    public void askUpdatesObject() throws Exception {
        DAskActorModel<Integer, String> model = new DAskActorModel<Integer, String>() {
            @Override
            protected ActorRef newActor() {
                return system().actorOf(Props.apply(new Creator<Actor>() {
                    @Override
                    public Actor create() throws Exception {
                        return new UntypedActor() {
                            @Override
                            public void onReceive(Object message) throws Exception {
                                if (message instanceof String) {
                                    getSender().tell(Integer.parseInt((String) message), getSelf());
                                } else {
                                    unhandled(message);
                                }
                            }
                        };
                    }
                }));
            }
        };

        model.ask("100").get();
        assertThat(model.getObject(), is(100));

        model.detach();
    }

    @Test
    public void tell() throws Exception {
        final AtomicBoolean b = new AtomicBoolean(false);

        TellActorModel<Integer, String> model = new TellActorModel<Integer, String>() {
            @Override
            protected ActorRef newActor() {
                return system().actorOf(Props.apply(new Creator<Actor>() {
                    @Override
                    public Actor create() throws Exception {
                        return new UntypedActor() {
                            @Override
                            public void onReceive(Object message) throws Exception {
                                if (message instanceof String) {
                                    b.set(true);
                                } else {
                                    unhandled(message);
                                }
                            }
                        };
                    }
                }));
            }
        };

        model.tell("test");
        Thread.sleep(100);
        assertThat(b.get(), is(true));

        model.detach();
    }

    @Test
    public void detach() throws Exception {
        ActorModel<Integer> model = new ActorModel<Integer>() {
            @Override
            protected ActorRef newActor() {
                return system().actorOf(Props.apply(new Creator<Actor>() {
                    @Override
                    public Actor create() throws Exception {
                        return new UntypedActor() {
                            @Override
                            public void onReceive(Object message) throws Exception {
                                unhandled(message);
                            }
                        };
                    }
                }));
            }
        };

        model.detach();
        Thread.sleep(100);

        assertThat(model.getObject(), is(nullValue()));
        assertThat(model.getActorRef().isTerminated(), is(true));
    }
}

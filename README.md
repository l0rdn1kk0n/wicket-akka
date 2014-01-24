wicket-akka
===========

Integration of Akka for Apache Wicket.

Current build status: [![Build Status](https://buildhive.cloudbees.com/job/l0rdn1kk0n/job/wicket-akka/badge/icon)]
(https://buildhive.cloudbees.com/job/l0rdn1kk0n/job/wicket-akka/)

**wicket-akka** dependes on [akka](http://akka.io).

Documentation:

- [Akka Documentation](http://doc.akka.io/docs/akka/2.2.3/)

Add maven dependency:

```xml
<dependency>
  <groupId>de.agilecoders.wicket.akka</groupId>
  <artifactId>wicket-akka</artifactId>
  <version>0.0.1</version>
</dependency>
```

Installation:

The simplest way to initialize wicket-akka is to extend `AkkaWebApplication`.

```java
    public class MyApplication extends AkkaWebApplication {
        /**
         * @see org.apache.wicket.Application#init()
         */
        @Override
        public void init() {
            super.init();

            // just before init is called, you've access to Akka.
        }
    }
```

Usage
=====

Now you're able to use `Akka.instance()` or `AkkaWebApplicationAwareWebPage` wherever you want:

```java
public MyWebPage extends AkkaWebApplicationAwareWebPage {

  public MyWebPage(PageParameters params) {
      super(params);

      // using a typed actor
      this.myService = getAkka().typedActorOf(MyService.class, MyServiceImpl.class);
      add(new Label("id1", FutureModel.of(myService.getExpensiveStringAsFuture())));

      // using an untyped actor
      this.myServiceActor = getAkka().system().actorOf(Props.create(MyServiceActor.class, "prefix"), "my-service-actor");
      IModel<String> actorModel = FutureModel.of(Patterns.ask(myServiceActor, new GetExpensiveString(), 3000));
      add(new Label("id2", actorModel));
  }

  @Override
  public void onDetach() {
    super.onDetach();

    getAkka().stop(myService);
    getAkka().stop(myServiceActor);
  }

  private static class MyServiceActor extends UntypedActor {
    private final String prefix;

    public MyServiceActor(String prefix) {
        super();

        this.prefix = prefix;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof GetExpensiveString) {
            getSender().tell(prefix + " my expensive string", getSelf());
        } else {
            unhandled(message);
        }
    }
  }
}
```

Authors
-------

[![Ohloh profile for Michael Haitz](https://www.ohloh.net/accounts/235496/widgets/account_detailed.gif)](https://www.ohloh.net/accounts/235496?ref=Detailed)



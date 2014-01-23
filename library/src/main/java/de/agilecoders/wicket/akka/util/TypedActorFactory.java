package de.agilecoders.wicket.akka.util;

import akka.actor.TypedProps;
import de.agilecoders.wicket.akka.Akka;
import org.apache.wicket.util.string.Strings;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public class TypedActorFactory<T> {
    private final Akka akka;
    private final TypedProps<T> props;
    private final String name;

    public TypedActorFactory(Akka akka, TypedProps<T> props) {
        this(akka, props, null);
    }

    public TypedActorFactory(Akka akka, TypedProps<T> props, String name) {
        this.akka = akka;
        this.props = props;
        this.name = name;
    }

    public T create() {
        if (!Strings.isEmpty(name)) {
            return akka.typedActorOf(props, name);
        } else {
            return akka.typedActorOf(props);
        }
    }
}

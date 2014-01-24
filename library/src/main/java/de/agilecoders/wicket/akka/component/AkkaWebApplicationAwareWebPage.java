package de.agilecoders.wicket.akka.component;

import de.agilecoders.wicket.akka.Akka;
import de.agilecoders.wicket.akka.application.AkkaWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Base {@link org.apache.wicket.markup.html.WebPage} that has some Akka access methods.
 *
 * @author miha
 */
public abstract class AkkaWebApplicationAwareWebPage extends WebPage {

    /**
     * Construct.
     */
    protected AkkaWebApplicationAwareWebPage() {
        super();
    }

    /**
     * Construct.
     *
     * @param model The component's model
     */
    protected AkkaWebApplicationAwareWebPage(IModel<?> model) {
        super(model);
    }

    /**
     * Construct.
     *
     * @param parameters Wrapped query string parameters.
     */
    protected AkkaWebApplicationAwareWebPage(PageParameters parameters) {
        super(parameters);
    }

    /**
     * @return the {@link de.agilecoders.wicket.akka.Akka} instance
     */
    protected final Akka getAkka() {
        return Akka.instance();
    }

    /**
     * @return the application casted to {@link de.agilecoders.wicket.akka.application.AkkaWebApplication}
     */
    public final AkkaWebApplication getAkkaWebApplication() {
        return AkkaWebApplication.get();
    }

}

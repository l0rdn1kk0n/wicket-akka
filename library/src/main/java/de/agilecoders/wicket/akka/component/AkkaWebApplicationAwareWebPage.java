package de.agilecoders.wicket.akka.component;

import de.agilecoders.wicket.akka.Akka;
import de.agilecoders.wicket.akka.application.AkkaWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public abstract class AkkaWebApplicationAwareWebPage extends WebPage {

    protected AkkaWebApplicationAwareWebPage() {
        super();
    }

    protected AkkaWebApplicationAwareWebPage(IModel<?> model) {
        super(model);
    }

    protected AkkaWebApplicationAwareWebPage(PageParameters parameters) {
        super(parameters);
    }

    protected Akka getAkka() {
        return Akka.instance();
    }

    public final AkkaWebApplication getAkkaWebApplication() {
        return AkkaWebApplication.get();
    }

}

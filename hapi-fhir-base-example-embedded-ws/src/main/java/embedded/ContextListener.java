package embedded;

import javax.inject.Singleton;

import org.ebaysf.web.cors.CORSFilter;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;

public class ContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {

		return Guice.createInjector(new JerseyServletModule() {

			@Override
			protected void configureServlets() {
				bind(CORSFilter.class).in(Singleton.class);
				filter("/*").through(CORSFilter.class);
				serve("/model/*").with(FhirRestfulServlet.class);
			}
		});
	}
}

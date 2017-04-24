package embedded.example;

import javax.inject.Singleton;
import javax.servlet.ServletContextEvent;

import org.ebaysf.web.cors.CORSFilter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;

public class ContextListener extends GuiceServletContextListener {

	static String username;
	static String password; 
	static String serverAddress;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);

		username = servletContextEvent.getServletContext().getInitParameter("username") != null
				? servletContextEvent.getServletContext().getInitParameter("username")
				: null;
		password = servletContextEvent.getServletContext().getInitParameter("password") != null
				? servletContextEvent.getServletContext().getInitParameter("password")
				: null;
		serverAddress = servletContextEvent.getServletContext().getInitParameter("serverAddress") != null
				? servletContextEvent.getServletContext().getInitParameter("serverAddress")
				: null;
	}

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new JerseyServletModule() {

			@Override
			protected void configureServlets() {

				AnnotationConfigWebApplicationContext webApp = new AnnotationConfigWebApplicationContext();
				webApp.setConfigLocation(FhirTesterConfig.class.getName());
				serve("/*").with(new DispatcherServlet(webApp));
				bind(CORSFilter.class).in(Singleton.class);
				filter("/*").through(CORSFilter.class);
			}
		});
	}
}
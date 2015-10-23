package embedded;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;

import filters.CharsetResponseFilter;
import filters.CorsResponseFilter;

public class ContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {

		return Guice.createInjector(new JerseyServletModule() {

			@Override
			protected void configureServlets() {
				final Map<String, String> params = ImmutableMap
						.<String, String> builder()
						.put(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS,
								Joiner.on(";").join(
										CharsetResponseFilter.class.getName(),
										CorsResponseFilter.class.getName(),
										GZIPContentEncodingFilter.class
												.getName())).build();
				serve("/model/*").with(FhirRestfulServlet.class, params);
			}
		});
	}
}

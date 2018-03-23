package ca.uhn.fhir.jpa.demo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * replace web.xml with Spring web initializer, so that various version specific
 * classes can be loaded on startup dynamically
 * 
 * @author anoushmouradian
 *
 */
public class WebInitializer implements WebApplicationInitializer {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(WebInitializer.class);
	
	@Override
	public void onStartup(ServletContext container) throws ServletException {
		
		ConfigurableApplicationContext propContext = new AnnotationConfigApplicationContext(ImmutablePropertiesConfig.class);
		ImmutablePropertiesConfig props = propContext.getBean(ImmutablePropertiesConfig.class);
		logger.info("This is FHIR_VERSION: " + props.getFhirVersion());
		propContext.close();

		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation(props.getFhirServerConfigClass());

		container.addListener(new ContextLoaderListener(context));
		context.close();

		AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
		dispatcherContext.setConfigLocation(props.getFhirTesterConfigClass());
		ServletRegistration.Dynamic springDispatcher = container.addServlet("spring", new DispatcherServlet(dispatcherContext));
		springDispatcher.setLoadOnStartup(2);
		springDispatcher.addMapping("/");
		dispatcherContext.close();

		ServletRegistration.Dynamic fhirServlet = container.addServlet("fhirServlet", props.getJpaDemoClass());
		fhirServlet.setInitParameter("ImplementationDescription", "FHIR JPA Server");
		fhirServlet.setInitParameter("FhirVersion", props.getFhirVersion());
		fhirServlet.setLoadOnStartup(1);
		fhirServlet.addMapping(props.getJpaDemoMapping());

		FilterRegistration.Dynamic corsFilter = container.addFilter("CORS Filter", "org.ebaysf.web.cors.CORSFilter");
		corsFilter.addMappingForUrlPatterns(null, false, "/*");
		Map<String, String> corsMap = Collections.unmodifiableMap(new HashMap<String, String>() {
			{
				put("cors.allowed.origins", "*");
				put("cors.allowed.methods", "GET,POST,PUT,DELETE,OPTIONS");
				put("cors.allowed.headers",
						"X-FHIR-Starter,Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers");
				put("cors.exposed.headers", "Location,Content-Location");
				put("cors.support.credentials", "true");
				put("cors.logging.enabled", "true");
				put("cors.preflight.maxage", "300");
			}
		});
		corsFilter.setInitParameters(corsMap);
	}

}

package ca.uhn.fhir.to.mvc;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

public class AnnotationMethodHandlerAdapterConfigurer {
	
	@Autowired
	@Qualifier("requestMappingHandlerAdapter")
	private RequestMappingHandlerAdapter adapter;

	@PostConstruct
	public void init() {
		adapter.setWebBindingInitializer(new ToBindingInitializer());
	}
}
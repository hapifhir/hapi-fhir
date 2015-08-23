package ca.uhn.fhir.to.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

public class AnnotationMethodHandlerAdapterConfigurer {
	@Autowired
	private RequestMappingHandlerAdapter adapter;

	public void init() {
		adapter.setWebBindingInitializer(new ToBindingInitializer());
	}
}